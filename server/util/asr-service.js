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
        java.callMethodSync(asrService, "start", cuid);
    },
    stop: function() {
        java.callMethodSync(asrService, "stop");
    },
    sendBytes: function(data) {
        java.callMethodSync(asrService, "sendBytes", java.newArray(
            "byte", data.map(function(c) { return java.newByte(c) })
        ));
    }
}