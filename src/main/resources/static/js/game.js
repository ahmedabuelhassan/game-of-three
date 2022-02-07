let autoPlay = false;
const socket = new SockJS('/game-of-three-websocket');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {

    stompClient.subscribe('/topic/game/' + gameId, function (message) {
        let messageJson = JSON.parse(message.body);
        appendEvent(messageJson.event);
        handleAutoPlay(messageJson.turn, messageJson.autoPlay);
        playAgain(messageJson.playAgain);
    });

    stompClient.subscribe('/user/queue/errors', function (message) {
        alert(message.body);
    });
});

function handleAutoPlay(turn, gameAutoPlay) {
    if (gameTurn === turn && autoPlay && gameAutoPlay) {
        play(randomNumber(-1, 1));
    }
}

function playAgain(playAgain) {
    if (playAgain) {
        let r = $('<button class="btn btn-success" id="playAgain" name="play" type="submit">Play Again</button>');
        if ($('#playAgain').length) {
            console.log("the button already existed");
        } else {
            $("#buttons").append(r);
            $("#play").remove();
            $('#playAgain').click(function () {
                window.location.href = window.location.href;
            });
        }

    }
}

function play(value) {
    stompClient.send("/game/play", {}, JSON.stringify({
        'addedValue': value
    }));
}

function appendEvent(event) {
    $("#events").append("<tr><td>" + event + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#play").click(function () {
        let addedValue = $("#addedValue").val();
        if (autoPlay) {
            if (addedValue) {
                alert("you already entered a value, clear the value or uncheck play automatically");
            } else {
                play(randomNumber(-1, 1));
            }
        } else {
            if (validateAddedValue(parseInt(addedValue))) {
                play(addedValue);
            } else {
                alert("please enter a valid number!")
            }
        }
    });
    $('#autoPlay').change(function () {
        autoPlay = !!$(this).is(":checked");
    });
});

function randomNumber(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function validateAddedValue(addedValue) {
    return addedValue === -1 || addedValue === 0 || addedValue === 1;
}