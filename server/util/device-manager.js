const net = require('net');
const woz_device = require('../devices/woz-device');

var input_device_lists = {
    "big-screen": {
        id: "big-screen",
        name: "触屏（大）",
        triggers: {},
        online: false
    },
    "screen": {
        id: "screen",
        name: "触屏（小）",
        triggers: {},
        online: false
    },
    "woz": {
        id: "woz",
        name: "wizard",
        triggers: {},
        online: false
    },
    "button": {
        id: "button",
        name: "实体按钮",
        triggers: {},
        online: false
    }
}

var output_device_lists = {
    "screen": {
        id: "screen",
        name: "屏幕反馈(小)",
        continuing: true,
        online: false,
        socket: null
    },
    "big-screen": {
        id: "big-screen",
        name: "屏幕反馈(大)",
        continuing: true,
        online: false,
        socket: null
    },
    "vibrate": {
        id: "vibrate",
        name: "振动反馈",
        continuing: false,
        online: false,
        socket: null
    },
    "voice": {
        id: "voice",
        name: "声音反馈",
        continuing: false,
        online: false,
        socket: null
    },
    "motion": {
        id: "motion",
        name: "动作反馈",
        continuing: true,
        online: false,
        socket: null
    }
}

var module_agent_socket = null;

function get_devices() {
    return [input_device_lists, output_device_lists];
}

function init_triggers() {
    for (var i in input_device_lists) {
        for (var j in output_device_lists) {
            input_device_lists[i].triggers[output_device_lists[j].id] = false;
        }
    }
}

function set_triggers(triggers) {
    for (var i in triggers) {
        if (input_device_lists[i] != null) {
            input_device_lists[i].triggers = triggers[i];
        }
    }
}

function trigger_output_devices(input_dev_name) {
    var dev = input_device_lists[input_dev_name];
    if (dev == null) return;
    var triggered_devices = [];
    var is_continuing = false;
    for (var i in dev.triggers) {
        if (dev.triggers[i]) {
            var output_device = output_device_lists[i];
            if (output_device.online) {
                output_device.socket.write("1");
                triggered_devices.push(output_device.name);
                if (output_device.continuing) {
                    is_continuing = true;
                }
            }
        }
    }

    if (is_continuing && triggered_devices.length) {
        module_agent_socket.set_wakeup(true);
    }

    if (module_agent_socket != null) {
        agent_feedback_msg = triggered_devices.length ?
            "进行唤醒,触发了 " + triggered_devices.join(",") + (is_continuing ? ",由于保持着被唤醒状态，需要wizard手动取消唤醒":"") : "进行唤醒,但未设置反馈或设备不在线";

        console.log("agent feedback msg:" + agent_feedback_msg);
        module_agent_socket.send(agent_feedback_msg);
    }
}

function unwakeup_output_devices() {
    for (var i in output_device_lists) {
        var dev = output_device_lists[i];
        if (dev.online && dev.socket != null) {
            dev.socket.write("0");
        }
    }
}

function init(agent_socket) {
    module_agent_socket = agent_socket;
    var input_devices = [];
    var output_devices = [];
    init_triggers();

    var input_device_server = net.createServer(function (socket) {
        console.log('input device connected');
    
        socket.dev_id = input_devices.length;
        input_devices.push(socket);
        socket.dev = null;
    
        socket.on('data', (data) => {
            console.log('device input:' + data.length + " " + data);
            if (socket.dev == null) {
                socket.dev = data;
                if (input_device_lists[socket.dev] != null) {
                    input_device_lists[socket.dev].online = true;
                }
            }
            if (data == "1") {
                trigger_output_devices(socket.dev);
            }
        });
        
        socket.on('end', () => {
            if (input_device_lists[socket.dev] != null) {
                input_device_lists[socket.dev].online = false;
            }
            console.log('input device disconnected');
        });

        socket.on('error', () => {
            if (input_device_lists[socket.dev] != null) {
                input_device_lists[socket.dev].online = false;
            }
            console.log('input device failed');
        });
    });

    var output_device_server = net.createServer(function (socket) {
        console.log('output device connected');
    
        socket.dev_id = output_devices.length;
        output_devices.push(socket);
        socket.dev = null;
    
        socket.on('data', (data) => {
            console.log('device input:' + data);
            if (socket.dev == null) {
                socket.dev = data;
                if (output_device_lists[socket.dev] != null) {
                    output_device_lists[socket.dev].online = true;
                    output_device_lists[socket.dev].socket = socket;
                }
            }
        });
        
        socket.on('end', () => {
            if (output_device_lists[socket.dev] != null) {
                output_device_lists[socket.dev].online = false;
            }
            console.log('output device disconnected');
        });

        socket.on('error', () => {
            if (output_device_lists[socket.dev] != null) {
                output_device_lists[socket.dev].online = false;
            }
            console.log('output device failed');
        });
    });
    
    input_device_server.on('error', (err) => {
        throw err;
    });
    
    input_device_server.listen(9000, () => {
        console.log('input device server bound');
    });
    
    output_device_server.listen(9001, () => {
        console.log('output device server bound');
    });

    woz_device.init();
}

device_manager = {
    init: init,
    unwakeup_output_devices: unwakeup_output_devices,
    set_triggers: set_triggers,
    get_devices: get_devices
}


module.exports = device_manager;

