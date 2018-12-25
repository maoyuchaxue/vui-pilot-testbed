const { createLogger, format, transports } = require('winston');
const { combine, timestamp, label, printf } = format;

const logFormat = printf(info => {
  return `${info.timestamp} [${info.cuid}] ${info.method}: ${info.message}`;
});

const logger = createLogger({
  format: combine(
    label({ cuid: 'default' }),
    timestamp(),
    logFormat
  ),
  transports: [
    new transports.File({ filename: "../logs/server.log" })
  ]
});

function log(message, method, cuid) {
  logger.log({
    level: 'info',
    message: message,
    method: method,
    cuid: cuid
  });
};

module.exports = log;