# Comments-Thread App

## Description
This is a demo app for a comments thread using push and messaging technologies

## Testing and Getting a Jar
In order to get a tested (unit && integration) `jar`, all you have to do is execute:
`mvn clean package`. An environment will be automatically provided to fill in any dependencies required for testing
purposes.

## Running the App
Before executing `java -jar ...` on the created jar, make sure you provide the required environment.
Do so by running `docker-compose up -d` while in the root directory of the project. Wait for the containers to get ready,
and then execute `java -jar ./target/demo_app_websock_jms-0.0.1-SNAPSHOT.jar` to start the app.

To start "chatting", fire up your browser and connect to `http://localhost:8080/index.html`.

## Regarding the Implementation
A user can connect to the endpoint and receive the pre-existing "comment-feed", as well post a new comment.
In this case, a comment is consisted of an email, and the comment text.

If an error occurs while the user tries to send his/her comment, an alert is sent and the user is notified with an
appropriate error message. If no error occurs then a message appears saying that the comment will be sent for review.

Note the review process happens at near real-time, hence the user is bound to see his/her new comment in the
comment feed immediately.

## Technologies Used
Respecting the requirement to build an app around the spring ecosystem, using
push technologies at the same time, led to the following technologies being used:
- Spring Boot
- STOMP and SockJS
- MongoDB (Spring Data)
- ActiveMQ (Spring JMS)
- Docker Compose Rule (testing scope)
- Mockito (testing scope)
- Hamcrest (testing scope)