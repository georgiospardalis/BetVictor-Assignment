package com.pardalis.demo_app_websock_jms.integration;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.waiting.HealthChecks;
import com.pardalis.demo_app_websock_jms.web.dto.CommentDTO;
import com.pardalis.demo_app_websock_jms.web.dto.DisplayableCommentDTO;
import com.pardalis.demo_app_websock_jms.messaging.model.AcceptedComment;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EndToEndTest.class);

    private static String WS_ENDPOINT;
    private static final String SUB_FOR_COMMENTS = "/thread/comments";
    private static final String ENDPOINT_FOR_COMMENTING = "/discussion/comment";

    private StompSession stompSession;
    private BlockingQueue<DisplayableCommentDTO> receivedMsgs;
    private List<Transport> transportList;

    @LocalServerPort
    private int localServerPort;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @ClassRule
    public static DockerComposeRule docker = DockerComposeRule.builder()
            .file("src/test/resources/docker-compose.yml")
            .waitingForService("mongodb", HealthChecks.toHaveAllPortsOpen())
            .waitingForService("activemq", HealthChecks.toHaveAllPortsOpen())
            .build();

    @Test
    public void casualUse() throws Exception {
        if (mongoOperations.collectionExists("accepted_comments")) {
            mongoOperations.dropCollection("accepted_comments");
        }

        mongoOperations.save(
                new AcceptedComment(
                        "myid",
                        new Long("1234567890123"),
                        "email@mail.com",
                        "ignored",
                        new Long("1234567890123")),
                "accepted_comments");

        WS_ENDPOINT = "ws://localhost:" + localServerPort + "/assignment-websocket";
        receivedMsgs = new LinkedBlockingQueue<>();
        transportList = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(transportList));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompSession = stompClient.connect(WS_ENDPOINT, new DummySessionHandler()).get(5, TimeUnit.SECONDS);

        CommentDTO commentDTO = new CommentDTO("mail@mail.com", "ignoreme");
        stompSession.send(ENDPOINT_FOR_COMMENTING, commentDTO);
        Thread.sleep(10000);
        DisplayableCommentDTO response = receivedMsgs.poll(5, SECONDS);
        Assert.assertEquals("email@mail.com", response.getEmail());
        Assert.assertEquals("ignored", response.getCommentText());

        List<AcceptedComment> acceptedCommentList = mongoOperations.findAll(AcceptedComment.class, "accepted_comments");
        Assert.assertEquals(2, acceptedCommentList.size());
        AcceptedComment ac0, ac1;
        ac0 = acceptedCommentList.get(0);
        ac1 = acceptedCommentList.get(1);
        Assert.assertEquals("ignored", ac0.getCommentText());
        Assert.assertEquals("email@mail.com", ac0.getEmail());
        Assert.assertEquals("ignoreme", ac1.getCommentText());
        Assert.assertEquals("mail@mail.com", ac1.getEmail());
    }

    private class DummySessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe(SUB_FOR_COMMENTS, this);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            LOGGER.warn("Stomp error", exception);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            super.handleTransportError(session, exception);
            LOGGER.warn("Stomp transport error:", exception);
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return DisplayableCommentDTO[].class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            LOGGER.info("Handle Frame with payload: {}", payload);

            try {
                DisplayableCommentDTO[] displayableComments = (DisplayableCommentDTO[]) payload;

                for (DisplayableCommentDTO dcDTO : displayableComments) {
                    receivedMsgs.offer(dcDTO, 500, MILLISECONDS);
                }
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}