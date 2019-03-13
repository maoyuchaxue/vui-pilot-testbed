const net = require('net');

const host = "localhost";
const idev_port = 9000;

var idev_socket = null;

function init() {
    idev_socket = net.createConnection(idev_port, host);
    idev_socket.write("woz");
}

function wakeup() {
    idev_socket.write('1');
}

function end() {
    idev_socket.end();
}

module.exports = {
    init: init,
    wakeup: wakeup,
    end: end
}
