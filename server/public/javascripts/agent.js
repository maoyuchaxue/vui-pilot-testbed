
var data = {
    msgs: [],
    options: [],
    reply: []
}

var socket = null;

receive_user_msg = function(user_msg) {
    user_msg = decodeURI(user_msg);
    if (user_msg.length > 0) {
        data.msgs.push({text:user_msg, is_user:true});
    }
    var ele = document.getElementById('msg-list');
    ele.scrollTop = ele.scrollHeight;
}

disconnect_from_server = function() {
    console.log("disconnected");
}

receive_options = function(options) {
    console.log(options);
    data.options = options.map(function(cur, ind) {return {text:cur, id:ind}});
}

clear_msg = function() {
    data.msgs = [];
}

send_agent_msg = function(text) {
    data.msgs.push({text:text, is_user:false});
    var ele = document.getElementById('msg-list');
    ele.scrollTop = ele.scrollHeight;
    console.log("send: "+ text);
    socket.emit('agent_msg', text);
    data.reply = [];
}

add_agent_msg = function(text) {
    texts = text.split('_');
    spans = [];
    for (i in texts) {
        if (texts[i][0] == '{') {
            spans.push({
                text: "",
                hint: texts[i].substr(1, texts[i].length-2),
                isSlot: true
            })
        } else {
            spans.push({
                text: texts[i],
                hint: "",
                isSlot: false
            })
        }
    }

    data.reply.push({
        spans: spans,
        id: data.reply.length
    })
}

remove_slice = function(slice_id) {
    data.reply.splice(slice_id, 1);
}

submit_reply = function() {
    var text = "";
    for (i in data.reply) {
        for (j in data.reply[i].spans) {
            var span = data.reply[i].spans[j];
            text += span.text;
            if (span.text.length == 0) {
                console.log("slot not finished!");
                return ;
            }
        }
    }
    send_agent_msg(text);
}

filter_slot = function(text) {
    var texts = text.split('_');
    var result_text = "";
    for (i in texts) {
        if (texts[i][0] == '{') {
            result_text += "_____";
        } else {
            result_text += texts[i];
        }
    }
    return result_text;
}

agent = function() {
    var vm = new Vue({
        el: '#root',
        data: data,
        methods: {
            sendAgentMsg: send_agent_msg,
            addAgentMsg: add_agent_msg,
            removeSlice: remove_slice,
            submitReply: submit_reply,
            filterSlot: filter_slot
        }
    })
    
    socket = io();
    socket.on('user_msg', receive_user_msg);
    socket.on('disconnect', disconnect_from_server);
    socket.on('options', receive_options);
}

