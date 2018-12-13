
var message_queue = require('./message-queue');

module.exports = {
    socket: function(socket) {
        this.socket = socket;
        socket.on('agent_msg', function(text) {
            message_queue.agent_to_user.push(text);
            socket.emit('user_msg', text);
        });
        socket.emit('options', ['option 1', 'option2', 'another option']);
    },
    send: function(text) {
        this.socket.emit('user_msg', text);
    },
    set_options: function(options) {
        this.socket.emit('options', options);
    }
}