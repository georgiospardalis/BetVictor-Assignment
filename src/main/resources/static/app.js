var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/assignment-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);

        console.log('Connected: ' + frame);

        stompClient.subscribe("/thread/comment_action", function (status) {
            $("#commentStatus").text(status.body);
        });

        stompClient.subscribe('/thread/comments', function (comments) {
            var resp = JSON.parse(comments.body);

            if (Array.isArray(resp)) {
                for (var i = 0; i < resp.length; i++) {
                    showComment(resp[i]);
                }
            } else {
                showComment(resp);
            }
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendComment() {
    stompClient.send("/discussion/comment", {}, JSON.stringify({'email': $("#mail").val(), 'comment-text': $("#comment").val()}));
}

function showComment(comment) {
    var dt = getDateFromMilliTs(comment["timestamp"]);

    $("#greetings").append("<tr><td>" + dt + " " + comment["email"] + " said:</td><td>" + comment["comment-text"] +"</td></tr>");
}

function getDateFromMilliTs(milliTs) {
    var dateFromTs = new Date(milliTs);
    var date = dateFromTs.getFullYear() + '-' + (dateFromTs.getMonth()+1) + '-' + dateFromTs.getDate();
    var time = dateFromTs.getHours() + ":" + dateFromTs.getMinutes() + ":" + dateFromTs.getSeconds();

    return  date + ' ' + time;
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendComment(); });
});