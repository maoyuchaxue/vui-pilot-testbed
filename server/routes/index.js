var express = require('express');
var path = require('path');
var router = express.Router();

var message_queue = require('../util/message-queue');
var socket_server = require('../util/socket-server');

router.get('/user', function(req, res, next) {
  console.log(req.query.text, req.query.cuid, req.query.completed);
  if (req.query.text) {
    socket_server.send(req.query.text);
  }

  res.send("").status(200);
});


router.get('/user_fetch', function(req, res, next) {
  var agent_res = message_queue.agent_to_user.shift();
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
