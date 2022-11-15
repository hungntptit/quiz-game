getClientID();

var questions;
var questionNumber = 0;
var client_id;

fetchdata();

window.onbeforeunload = disconnect;
function disconnect(e) {
    let sendData = { "client_id": client_id };
    $.ajax({
        type: "POST",
        url: "/disconnect",
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(sendData),
        success: function (data) {
            console.log(data);
        },
        error: function () { }
    });
    var e = e || window.event;
    if (e) {
        e.returnValue = '';
    }
    return '';
}



$('#form_id').submit(function (e) {
    e.preventDefault(); // avoid to execute the actual submit of the form.
    // Check answer for current question
    var current_q = questions[questionNumber];

    var form = $(this);
    var actionUrl = form.attr('action');
    var formData = new FormData(document.getElementById("form_id"));
    formData.append('questionNumber', questionNumber);
    formData.append('client_id', client_id);

    if (questionNumber >= 1 && questionNumber <= Object.keys(questions).length) {
        console.log(formData.get('answer') + " == " + current_q.correctAnswer + "?");
        if (formData.get('answer') == current_q.correctAnswer) {
            console.log("true");
            formData.append('is_correct', 1);
        } else {
            console.log("false");
            formData.append('is_correct', 0);
        }
        let sendData = {};
        for (let pair of formData.entries()) {
            // console.log(pair[0] + ', ' + pair[1]);
            sendData[pair[0]] = pair[1];
        }
        $.ajax({
            type: "POST",
            url: actionUrl,
            headers: {
                'Content-Type': 'application/json'
            },
            // dataType: 'json',
            data: JSON.stringify(sendData),
            success: function (data) {
                console.log(data);
            },
            error: function () { }
        });
        if (questionNumber == Object.keys(questions).length) {
            complete();
        }
    }

    // update view next question
    $('input[type="radio"]').prop('checked', false);
    if (questionNumber >= 0 && questionNumber < Object.keys(questions).length) {
        questionNumber++;
        let next_q = questions[questionNumber];
        // console.log(next_q.question);
        $('#txtquestionNumber').html("Question " + questionNumber + "/" + Object.keys(questions).length + "<br>");
        $('#txtquestion').html(next_q.question + "<br>" + next_q.options.join("<br>"));
        if (questionNumber == Object.keys(questions).length) {
            $('input[type="submit"]').prop('value', 'Submit');
            $('input[type="submit"]').prop('class', 'btn btn-danger');
        }
    }
});

function complete() {
    $('#txtquestion').html("");
    $('#txtquestionNumber').html("Question " + "<br>");
    if (questionNumber == Object.keys(questions).length) {
        let sendData = {};
        sendData["client_id"] = client_id;
        $.ajax({
            url: '/completed',
            type: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(sendData),
            success: function (data) {
                console.log(data)
            },
            complete: function (data) {
                // setTimeout(complete, 1000);
            }
        });
    }
    questionNumber = -1;
    $('#completedModel').modal('show');
    $('input[type="submit"]').prop('value', 'Submitted');
    $('input[type="submit"]').prop('class', 'btn btn-secondary');
}

function getClientID() {
    $.ajax({
        url: '/client-id',
        type: 'GET',
        datatype: 'json',
        success: function (data) {
            console.log(data)
            jsonMessage = $.parseJSON(data);
            console.log(jsonMessage)
            if (jsonMessage.hasOwnProperty('client_id')) {
                // console.log(jsonMessage.client_id);
                client_id = jsonMessage.client_id;
                $("#clientid").html("Client " + jsonMessage.client_id)
            }
        },
        complete: function (data) {
            // setTimeout(getClientID, 1000);
        }
    });
}

// get update every 1 second
function fetchdata() {
    if (client_id != null) {
        let sendData = {};
        sendData['client_id'] = client_id;
        // console.log(JSON.stringify(sendData));
        $.ajax({
            url: '/update',
            type: 'GET',
            async: true,
            headers: {
                'Content-Type': 'application/json'
            },
            // datatype: 'json',
            data: {
                "client_id": client_id
            },
            success: function (data) {
                // console.log(data)
                jsonMessage = $.parseJSON(data);
                // console.log(jsonMessage)
                if (jsonMessage.hasOwnProperty('questions')) {
                    questions = jsonMessage.questions;
                }
                if (jsonMessage.hasOwnProperty('timer')) {
                    // console.log(jsonMessage.timer)
                    $("#txttimer").html(jsonMessage.timer)
                }
                if (jsonMessage.hasOwnProperty('rank')) {
                    // console.log(jsonMessage.rank);
                    $("#txtrank").html(jsonMessage.rank.split("timer")[0].replaceAll("\n", "<br>"))
                }
                if (jsonMessage.hasOwnProperty('timeout')) {
                    // console.log("timeout: " + jsonMessage.timeout);
                    if (jsonMessage.timeout == true) {
                        console.log("timeout: " + jsonMessage.timeout);
                        complete();
                    }
                }
            },
            complete: function (data) {
                if (jsonMessage.timeout == false) {
                    setTimeout(fetchdata, 1000);
                }
            }
        });
    }
};
$(document).ready(function () {
    setTimeout(fetchdata, 1000);
});
