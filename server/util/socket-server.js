
var message_queue = require('./message-queue');
var fs = require('fs');

var script_json = fs.readFileSync('script/test.json');
var script = JSON.parse(script_json);

module.exports = {
    socket: function(socket) {
        this.socket = socket;
        socket.on('agent_msg', function(text) {
            message_queue.agent_to_user.push(text);
            socket.emit('user_msg', text);
        });
        socket.emit('options', script);
    },
    send: function(text) {
        this.socket.emit('user_msg', text);
    },
    set_options: function(options) {
        this.socket.emit('options', options);
    }
}