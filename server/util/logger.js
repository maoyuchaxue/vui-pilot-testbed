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
    new transports.File({ filename: "../logs/server.log", level: "info" }),
    new transports.File({ filename: "../logs/audio.log", level: "debug" }),
  ]
});

function log(message, method, cuid, level) {
  if (!level) {
    level = 'info';
  };

  logger.log({
    level: level,
    message: message,
    method: method,
    cuid: cuid
  });
};

module.exports = log;