const net = require('net');
const process = require('process');

const host = "localhost";
const idev_port = 9000;

var idev_socket = net.createConnection(idev_port, host);
idev_socket.write("screen");

process.stdin.resume();
process.stdin.setRawMode(true);

process.stdin.on('data', function (k) {
    idev_socket.write("1");
});