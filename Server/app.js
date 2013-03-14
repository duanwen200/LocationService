/**
 * app.js
 * by vampirefan
 */

var express = require('express');
var http = require('http');
var path = require('path');
var routes = require('./routes');
var settings = require('./settings');

var app = express();

app.configure(function() {
  app.set('port', process.env.PORT || 3000);
  app.set('views', __dirname + '/views');
  app.set('view engine', 'ejs');
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.bodyParser({
    uploadDir: __dirname + '/public/uploadTemp',
    keepExtensions: true,
    limit: 10000000 //10M limit
    // defer: true //enable event
  }));
  app.use(express.methodOverride());
  app.use(express.cookieParser('vampirefan presents'));
  app.use(express.session({
    secret: settings.cookieSecret
  }));
  app.use(app.router);
  app.use(express.static(path.join(__dirname, 'public')));
});

app.configure('development', function() {
  app.use(express.errorHandler());
});

routes(app);

var appServer = http.createServer(app);
appServer.listen(app.get('port'), function() {
  console.log("-------------------------------------->");
  console.log("Location Service listening on port " + app.get('port'));
});

var io = require('socket.io').listen(appServer);
io.set('log level', 1);
io.sockets.on('connection', function(socket) {
  socket.on('send:coords', function(data) {
    socket.broadcast.emit('load:coords', data);
  });
});