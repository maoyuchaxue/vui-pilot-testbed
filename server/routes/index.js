var express = require('express');
var path = require('path');
var router = express.Router();

var message_queue = require('../util/message-queue');
var socket_server = require('../util/socket-server');
var asr_service = require('../util/asr-service');
var log = require('../util/logger');

router.get('/control', function(req, res, next) {
  console.log(req.query.cuid, req.query.start, req.query.end);
  if (req.query.start == 1) {
    message_queue.agent_to_user = [];
    asr_service.start(req.query.cuid);

    log('start', 'control', req.query.cuid);
  }
  if (req.query.end == 1) {
    asr_service.stop();
    
    log('end', 'control', req.query.cuid);
  }
  res.send("").status(200);
})

router.post('/raw', function(req, res, next) {
  let buff = new Buffer.from(req.body.payload, 'base64');
  log(req.body.payload, 'raw', req.body.cuid, 'debug');

  asr_service.sendBytes([...buff]);
  res.send("").status(200);
})

router.get('/user', function(req, res, next) {
  if (req.query.text) {
    socket_server.send(req.query.text);
  }

  log(decodeURIComponent(req.query.text), 'user', req.query.cuid);
  res.send("").status(200);
});


router.get('/user_fetch', function(req, res, next) {
  var agent_res = message_queue.agent_to_user.shift();
  if (!agent_res) {
    agent_res = {};
  }

  if (socket_server.is_wakeup()) {
    agent_res.wakeup = '1';
  } else {
    agent_res.wakeup = '0';
  }

  log(JSON.stringify(agent_res), 'fetch', req.query.cuid);
  res.send(agent_res);
});

router.get('/agent', function(req, res, next) {
  res.sendFile(path.join(__dirname, "../html/agent.html"));
});

module.exports = router;
