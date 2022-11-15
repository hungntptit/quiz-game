const BiMap = require("bidirectional-map");

const express = require('express');
const app = express();
const port = 3030;

// var bodyParser = require('body-parser');
// app.use(bodyParser.urlencoded({extended : true}));
// app.use(bodyParser.json());

// Parse URL-encoded bodies when sent by HTML forms
app.use(express.urlencoded({ extended: true }));
// Parse JSON bodies when sent by clients
app.use(express.json());

app.use(express.static('public'));

var net = require('net');

var jsonMessage = {};
jsonMessage["timeout"] = false;

var tcpClientMap = new BiMap();
var onlineList = [];

// Return page with form
app.get('/', (req, res) => {
    console.log(req.body);
    console.log();

    var txtOnlineList, rank, timer, questions;
    const tcpClient = new net.Socket();
    tcpClient.connect(7778, '127.0.0.1', function () {
        console.log('Connected');
        tcpClient.write('From NodeJS with love.\n');
    });

    tcpClient.on('data', function (data) {
        // split first comma occurence
        data = String(data);
        // console.log(data);
        var splits = [];
        tmp = data.split(',');
        splits.push(tmp.shift());
        splits.push(tmp.join(','));

        if (splits[0] == "get-id") {
            jsonMessage["client_id"] = splits[1].split("\r\n")[0];
            let client_id = jsonMessage["client_id"];
            if (tcpClientMap.has(client_id)) {
                tcpClientMap.get(client_id).destroy();
            }
            tcpClientMap.set(client_id, tcpClient);
            // console.log(tcpClientMap.has(client_id));
        }
        else if (splits[0] == "update-online-list") {
            onlineList = [];
            var online = "";
            var onlineSplit = splits[1].split("-");
            for (let i = 0; i < onlineSplit.length; i++) {
                onlineList.push(onlineSplit[i]);
                online += "Client " + onlineSplit[i] + " đang online\n";
            }
            txtOnlineList = online;
        }
        else if (splits[0] == "global-message") {

        }
        else if (splits[0] == "list-questions") {
            questions = JSON.parse(splits[1]);
            jsonMessage["questions"] = questions;
        }

        else if (splits[0] == "update-online-list") {
            onlineList = [];
            var online = "";
            var onlineSplit = splits[1].split("-");
            for (let i = 0; i < onlineSplit.length; i++) {
                onlineList.push(onlineSplit[i]);
                online += "Client " + onlineSplit[i] + " đang online\n";
            }
            txtOnlineList = online;
        }
        else if (splits[0] == "update-points") {
            pointStrings = splits[1].split("-");
            rank = pointStrings.join("\n");
            jsonMessage["rank"] = rank
        }

        else if (splits[0] == "timer") {
            timer = splits[1].split('\r\n')[0];
            jsonMessage["timer"] = timer;
            if (questions == null) {
                console.log("start quizz");
                tcpClient.write("start-quizz\n");
            }
        }
        else if (splits[0] == "completed") {
            jsonMessage["timeout"] = true;
            console.log("timeout: " + tcpClientMap.getKey(tcpClient));
            tcpClient.write("Write-file-update-point," + tcpClientMap.getKey(tcpClient) + "\n");
            tcpClient.end();
        }
    });

    tcpClient.on('close', function () {
        jsonMessage = {}
        console.log('Connection closed');
    });

    tcpClient.on('error', function (ex) {
        console.log("handled error");
        console.log(ex);
    });

    res.sendFile(__dirname + "/index.html");
});

app.get('/update', (req, res) => {
    let client_id = req.query.client_id;
    // console.log("/update: " + client_id);
    res.json(JSON.stringify(jsonMessage));
});

app.get('/client-id', (req, res) => {
    // console.log(req.body);
    res.json(JSON.stringify(jsonMessage));
});

app.post('/completed', (req, res) => {
    // console.log(req.body);
    let client_id = req.body.client_id;
    console.log("client " + client_id + " completed");
    tcpClientMap.get(client_id).write("Write-file-update-point" + "," + client_id + "\n");
    res.json("{completed: success}");
});

app.post('/disconnect', (req, res) => {
    let client_id = req.body.client_id;
    console.log("/disconnect: " + client_id);
    tcpClientMap.get(client_id).write("disconnect" + "," + client_id + "\n");
    res.json("{disconnect: success}");
});

app.post('/next', (req, res) => {
    console.log("/next: " + req.body.client_id);
    let client_id = req.body.client_id;
    let is_correct = req.body.is_correct;
    if (is_correct == 1) {
        tcpClientMap.get(client_id).write("send-update-point" + "," + client_id + "\n");
    } else if (is_correct == 0) {
        tcpClientMap.get(client_id).write("send-update-point" + "," + -1 + "\n");
    }
    res.json("{next: success}");
});

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
});



