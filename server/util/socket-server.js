
var message_queue = require('./message-queue');
var fs = require('fs');

var script_json = fs.readFileSync('script/test.json');
var script = JSON.parse(script_json);
var module_socket = null;
var wakeup = false;

module.exports = {
    socket: function(socket) {
        module_socket = socket;
        socket.on('agent_msg', function(text) {
            console.log("agent_msg: " + text);
            message_queue.agent_to_user.push({text: text});
            // socket.emit('user_msg', text);
        });
        socket.emit('options', script);
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