var express = require('express');
var path = require('path');
var router = express.Router();
var 

var message_queue = require('../util/message-queue');
var socket_server = require('../util/socket-server');
var asr_service = require('../util/asr-service');

router.get('/control', function(req, res, next) {
  console.log(req.query.cuid, req.query.start, req.query.end);
  if (req.query.start == 1) {
    message_queue.agent_to_user = [];
    asr_service.start(req.query.cuid);
  }
  if (req.query.end == 1) {
    asr_service.stop();
  }
  res.status(200);
})

router.post('/raw', function(req, res, next) {
  console.log(req.body.cuid, req.query.payload);
  let buff = new Buffer.from(req.query.payload, 'base64');
  asr_service.sendBytes(buff);
  res.status(200);
})

router.get('/user', function(req, res, next) {
  console.log(req.query.text, req.query.cuid, req.query.completed);
  if (req.query.text) {
    socket_server.send(req.query.text);
  }

  res.send("").status(200);
});


router.get('/user_fetch', function(req, res, next) {
  var agent_res = message_queue.agent_to_user.shift(); // TODO: now message_queue is not multimodal
  if (socket_server.is_wakeup) {
    agent_res.wakeup = 1;
  }
  if (agent_res) {
    console.log("user_fetch: " + agent_res);
    res.send(agent_res);
  } else {
    res.send("");
  }
});

router.get('/agent', function(req, res, next) {
  res.sendFile(path.join(__dirname, "../html/agent.html"));
});

module.exports = router;
