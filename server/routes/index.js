var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/user', function(req, res, next) {
  console.log(req.query.text, req.query.cuid);
  res.send("call back to user");
});

router.get('/agent', function(req, res, next) {
  res.send("todo");
});

module.exports = router;
