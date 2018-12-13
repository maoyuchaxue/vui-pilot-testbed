
var data = {
    msgs: [],
    options: []
}

var socket = null;

receive_user_msg = function(user_msg) {
    if (user_msg.length > 0) {
        data.msgs.push({text:user_msg, is_user:true});
    }
}

disconnect_from_server = function() {
    console.log("disconnected");
}

receive_options = function(options) {
    console.log(options);
    data.options = options.map(function(cur, ind) {return {text:cur, id:ind}});
}

send_agent_msg = function(text) {
    data.msgs.push({text:text, is_user:false});
    socket.emit('agent_msg', text);
}

agent = function() {
    var vm = new Vue({
        el: '#root',
        data: data,
        methods: {
            sendAgentMsg: send_agent_msg
        }
    })
    
    socket = io();
    socket.on('user_msg', receive_user_msg);
    socket.on('disconnect', disconnect_from_server);
    socket.on('options', receive_options);
}

