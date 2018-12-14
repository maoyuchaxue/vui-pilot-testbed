var express = require('express');
var path = require('path');
var router = express.Router();

var message_queue = require('../util/message-queue');
var socket_server = require('../util/socket-server');

router.get('/user', function(req, res, next) {
  console.log(req.query.text, req.query.cuid);
  if (req.query.text) {
    socket_server.send(req.query.text);
  }

  var agent_res = message_queue.agent_to_user.shift();
  if (agent_res) {
    res.send(agent_res);
  } else {
    res.send("");
  }
});


router.get('/user_fetch', function(req, res, next) {
  console.log(req.query.cuid);

  var agent_res = message_queue.agent_to_user.shift();
  if (agent_res) {
    res.send(agent_res);
  } else {
    res.send("");
  }
});

router.get('/agent', function(req, res, next) {
  res.sendFile(path.join(__dirname, "../html/agent.html"));
});

module.exports = router;
