var fs = require('fs');
var java = require('java');
var baseDir = "../libs/runtime-libs";
var dependencies = fs.readdirSync(baseDir);

dependencies.forEach(function(dependency) {
    java.classpath.push(baseDir + "/" + dependency);
})

java.classpath.push('../libs/ht-java-talker-0.3.2-out.jar');
java.classpath.push('../bin');
java.options.push("-Djava.library.path=../libs");

var asrService = java.newInstanceSync("com.baidu.aip.demotest.ASRService")

module.exports = {
    start: function(cuid) {
        console.log("start: begin");
        java.callMethodSync(asrService, "start", cuid);
        console.log("start: end");
    },
    stop: function() {
        console.log("stop: begin");
        java.callMethodSync(asrService, "stop");
        console.log("stop: end");
    },
    sendBytes: function(data) {
        console.log("sendByte: begin");
        java.callMethodSync(asrService, "sendBytes", java.newArray(
            "byte", data.map(function(c) { return java.newByte(c) })
        ));
        console.log("sendByte: end");
    }
}