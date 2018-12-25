
var message_queue = require('./message-queue');
var fs = require('fs');

var script_json = fs.readFileSync('script/test.json');
var scripts = JSON.parse(script_json);

var sections = scripts["sections"];

var module_socket = null;
var wakeup = false;

module.exports = {
    socket: function(socket) {
        module_socket = socket;
        socket.on('agent_msg', function(msg) {
            console.log("agent_msg: " + msg);
            message_queue.agent_to_user.push(msg);
            // socket.emit('user_msg', text);
        });
        socket.on('wakeup', function(new_wakeup) {
            wakeup = new_wakeup;
            console.log("wakeup " + new_wakeup);
        });
        socket.on('set_section', function(section) {
            socket.emit('options', scripts[section]);
        });
        socket.on('voice-wakeup', function() {
            message_queue.agent_to_user.push({text: "在"});
        });
        socket.on('vibrate-wakeup', function() {
            message_queue.agent_to_user.push({vibrate: true});
        })
        socket.emit('sections', sections);
        socket.emit('options', scripts["default"]);
    },
    send: function(text) {
        module_socket.emit('user_msg', text);
    },
    set_options: function(options) {
        module_socket.emit('options', options);
    },
    set_wakeup: function(new_wakeup) {
        wakeup = new_wakeup;
    },
    is_wakeup: function() {
        return wakeup;
    }
}