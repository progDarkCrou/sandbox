var http = require('http');
var Static = require('node-static');

var fileServer = new Static.Server('.');
var port = 80;
http.createServer(function (req, res) {
  
  fileServer.serve(req, res);

}).listen(port);

console.log("Сервер запущен на порте " + port);