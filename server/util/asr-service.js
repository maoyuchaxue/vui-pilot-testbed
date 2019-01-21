var fs = require('fs');
var java = require('java');
var path = require('path');
var baseDir = path.join(__dirname, '..', '..');
var dependencies = fs.readdirSync(path.join(baseDir, 'libs', 'runtime-libs'));

java.classpath.push(path.join(baseDir, 'libs', 'ht-java-talker-0.3.2-out.jar'));
java.classpath.push(path.join(baseDir, 'build', 'classes', 'main'));

dependencies.forEach(function(dependency) {
    java.classpath.push(path.join(baseDir, 'libs', 'runtime-libs', dependency));
})

console.log(java.classpath);
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