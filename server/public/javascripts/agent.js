
var data = {
    msgs: [],
    options: [],
    reply: [],
    sections: [],
    wakeup: false,
    wakeupButtonClass: "btn-info",
    unwakeupButtonClass: "btn-primary",
    buttonClass: "btn"
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
    data.options = options.map(function(cur, ind) {return {content:cur, id:ind}});
}

receive_sections = function(sections) {
    console.log(sections);
    data.sections = sections;
}

clear_msg = function() {
    data.msgs = [];
}

send_agent_msg = function(msg) {
    data.msgs.push({text:msg.text, is_user:false});
    var ele = document.getElementById('msg-list');
    ele.scrollTop = ele.scrollHeight;
    console.log("send: ", msg);
    socket.emit('agent_msg', msg);
    data.reply = [];
}

add_agent_msg = function(content) {
    texts = content.text.split('_');
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
        id: data.reply.length,
        img: content.img
    })
}

remove_slice = function(slice_id) {
    data.reply.splice(slice_id, 1);
}

submit_reply = function() {
    var res = {};
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

        if (data.reply[i].img) {
            res.img = data.reply[i].img;
        }
    }
    res.text = text;

    send_agent_msg(res);
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

choose_section = function(section) {
    socket.emit('set_section', section);
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
            filterSlot: filter_slot,
            chooseSection: choose_section,
            triggerWakeup: function() {
                data.wakeup = !data.wakeup;
                socket.emit('wakeup', data.wakeup);
            },
            voiceWakeup: function() { socket.emit('voice-wakeup'); },
            vibrateWakeup: function() { socket.emit('vibrate-wakeup'); }
        }
    })
    
    socket = io();
    socket.on('user_msg', receive_user_msg);
    socket.on('disconnect', disconnect_from_server);
    socket.on('options', receive_options);
    socket.on('sections', receive_sections);
}

