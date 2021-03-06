var express = require('express');
var path = require('path');
var router = express.Router();

var message_queue = require('../util/message-queue');
var socket_server = require('../util/socket-server');
var device_manager = require('../util/device-manager');
var asr_service = require('../util/asr-service');
var log = require('../util/logger');

device_manager.init(socket_server);

router.get('/control', function(req, res, next) {
  console.log(req.query.cuid, req.query.start, req.query.end);
  if (req.query.start == 1) {
    message_queue.agent_to_user = [];
    socket_server.notify_start(req.query.cuid);
    asr_service.start(req.query.cuid);

    log('start', 'control', req.query.cuid);
  }
  if (req.query.end == 1) {
    asr_service.stop();
    socket_server.notify_end();
    log('end', 'control', req.query.cuid);
  }
  res.send("").status(200);
})

router.post('/raw', function(req, res, next) {
  let buff = new Buffer.from(req.body.payload, 'base64');
  log(req.body.payload, 'raw', req.body.cuid, 'debug');

  if (req.body.msg) {
    log(req.body.msg, 'msg', req.body.cuid, 'debug');
    if (req.body.msg == "wakeup") {
      socket_server.send("（按下唤醒按钮）");
      socket_server.set_wakeup(true);
    }
  }

  asr_service.sendBytes([...buff]);
  res.send("").status(200);
})

router.get('/user', function(req, res, next) {
  if (req.query.text) {
    socket_server.send(req.query.text);
  }

  log(req.query.text, 'user', req.query.cuid);
  res.send("").status(200);
});


router.get('/user_fetch', function(req, res, next) {
  var agent_res = message_queue.agent_to_user.shift();
  if (!agent_res) {
    agent_res = {};
  }

  log(JSON.stringify(agent_res), 'fetch', req.query.cuid);
  res.send(agent_res);
});

router.get('/agent', function(req, res, next) {
  res.sendFile(path.join(__dirname, "../html/agent.html"));
});

router.get('/devices', function(req, res, next) {
  res.sendFile(path.join(__dirname, "../html/devices.html"));
});

router.get('/get_devices', function(req, res, next) {
  res.send(JSON.stringify(device_manager.get_devices()));
});

router.post('/set_trigger', function(req, res, next) {
  console.log(req.body.triggers);
  trigger = JSON.parse(req.body.triggers);
  device_manager.set_triggers(trigger);
});

module.exports = router;
