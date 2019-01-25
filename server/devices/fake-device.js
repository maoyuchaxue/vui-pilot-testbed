const net = require('net');
const process = require('process');

const host = "localhost";
const idev_port = 9000;
const odev_port = 9001;

var idev_socket = net.createConnection(idev_port, host);
idev_socket.write("screen");

var odev_socket = net.createConnection(odev_port, host);
odev_socket.write("screen");

odev_socket.on('data', function (data) {
    console.log(data);
})

process.stdin.resume();

process.stdin.on('data', function () {
    idev_socket.write("1");
});