
var data = {
    msgs: [],
    options: [],
    reply: [],
    sections: [],
    scripts: [],
    error_count: 0,
    test_started: false,
    cuid: "none",
    wakeup: false,
    wakeupButtonClass: "btn-info",
    unwakeupButtonClass: "btn-primary",
    buttonClass: "btn"
}

var socket = null;

receive_user_msg = function(user_msg) {
    user_msg = decodeURIComponent(user_msg);
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
    data.options = options.map(function(cur, ind) {return {content:cur, id:ind}});
}

receive_sections = function(sections) {
    data.sections = sections;
}

receive_scripts = function(scripts) {
    data.scripts = scripts;
}

clear_msg = function() {
    data.msgs = [];
}

send_agent_msg = function(msg) {
    if (msg.text.length == 0) {
        data.msgs.push({text:"(语音停止)", is_user:false});    
    } else {
        data.msgs.push({text:msg.text, is_user:false});
    }
    var ele = document.getElementById('msg-list');
    ele.scrollTop = ele.scrollHeight;
    socket.emit('agent_msg', msg);
    data.reply = [];
}

submit_error = function() {
    if (!data.test_started) {
        return ;
    }

    data.error_count += 1;
    socket.emit('count_error', data.cuid, data.error_count);
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

    if (!data.test_started) {
        return ;
    }

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

choose_script = function(script) {
    socket.emit('set_script', script);
}

receive_test_start = function(cuid) {
    data.test_started = true;
    data.msgs = [];
    data.error_count = 0;
    data.cuid = cuid;
}

receive_test_end = function() {
    data.test_started = false;
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
            submitError: submit_error,
            filterSlot: filter_slot,
            chooseSection: choose_section,
            chooseScript: choose_script,
            triggerWakeup: function() {
                // if (!data.test_started) {
                //     return ;
                // }
                data.wakeup = !data.wakeup;
                socket.emit('wakeup', data.wakeup);
            // },
            // voiceWakeup: function() { 
            //     if (!data.test_started) {
            //         return ;
            //     }
            //     socket.emit('voice-wakeup');
            // },
            // vibrateWakeup: function() {
            //     if (!data.test_started) {
            //         return ;
            //     }
            //     socket.emit('vibrate-wakeup');
            }
        },
        computed: {
            replyIsEmpty: function() {
                return this.reply.length == 0;
            }
        }
    })
    
    socket = io();
    socket.on('user-msg', receive_user_msg);
    socket.on('disconnect', disconnect_from_server);
    socket.on('options', receive_options);
    socket.on('sections', receive_sections);
    socket.on('scripts', receive_scripts);
    socket.on('start', receive_test_start);
    socket.on('end', receive_test_end);
    socket.on('set-wakeup', function(wakeup) {
        data.wakeup = wakeup;
    })
}

