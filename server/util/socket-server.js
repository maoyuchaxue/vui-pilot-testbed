
var message_queue = require('./message-queue');
var log = require('./logger');
var fs = require('fs');
var path = require('path');

var info_json = fs.readFileSync('script/info.json');
var summary = JSON.parse(info_json);

var scripts = []
for (i in summary) {
    var script_json = fs.readFileSync(path.join('script', summary[i].filename));
    var script_content = JSON.parse(script_json);
    scripts[summary[i].filename] = script_content;
}

var cur_script = scripts[summary[0].filename];
var sections = cur_script["sections"];

var module_socket = null;
var wakeup = false;

module.exports = {
    socket: function(socket) {
        module_socket = socket;
        socket.on('agent_msg', function(msg) {
            message_queue.agent_to_user.push(msg);
        });
        socket.on('wakeup', function(new_wakeup) {
            wakeup = new_wakeup;
        });
        socket.on('set_script', function(script) {
            cur_script = scripts[script];
            sections = cur_script["sections"];
            socket.emit('sections', sections);
            socket.emit('options', cur_script[sections[0].name]);
        });
        socket.on('set_section', function(section) {
            socket.emit('options', cur_script[section]);
        });
        socket.on('voice-wakeup', function() {
            message_queue.agent_to_user.push({text: "在", hide:'1'});
        });
        socket.on('vibrate-wakeup', function() {
            message_queue.agent_to_user.push({vibrate: true});
        })
        socket.on('count_error', function(cuid, error_count) {
            log(error_count, 'error', cuid);
        })
        socket.emit('scripts', summary);
        socket.emit('sections', sections);
        socket.emit('options', cur_script[sections[0].name]);
    },
    send: function(text) {
        module_socket.emit('user-msg', text);
    },
    set_options: function(options) {
        module_socket.emit('options', options);
    },
    set_wakeup: function(new_wakeup) {
        wakeup = new_wakeup;
        module_socket.emit('set-wakeup', wakeup);
    },
    is_wakeup: function() {
        return wakeup;
    },
    notify_start: function(cuid) {
        module_socket.emit('start', cuid);
    },
    notify_end: function() {
        module_socket.emit('end');
    }
}