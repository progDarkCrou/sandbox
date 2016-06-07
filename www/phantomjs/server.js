var http = require('http');
var app = require("express")();
var Log = require('log');
var childProcess = require("child_process");
var cmdArgs = require('command-line-args');
var port = 9999;

var args = cmdArgs([{
	name: 'timeout',
	alias: 't',
	type: Number,
	defaltValue: 60
}, {
	name: 'debug',
	alias: 'd',
	defaltValue: false,
	type: Boolean
}, {
	name: 'redirect',
	alias: 'r',
	defaltValue: null,
	type: String
}]).parse();

var defaultTimeout = args.timeout * 1000;
var logger = new Log(args.debug? 'debug': 'info');

logger.info("Default timeout set to " + args.timeout + " seconds");

app.get('/', function(req, resp, next) {
    var url = req.query['location'];
    logger.info('Location requested: ' + url);

    if (url) {
    	var content = '';
	    var phantom = childProcess.spawn('phantomjs', ['phantom.js', url]);
	    logger.debug('Started PhantomJS PID: ' + phantom.pid);

	    phantom.stdout.setEncoding('utf8');
	    phantom.stdout.on('data', function(data) {
	        content += data.toString();
	        logger.debug('Retrived data from PhantomJS process by length: ' + data.length);
	    });
	    
	    phantom.on('exit', function(status_code) {
	        if (status_code !== 0) {
    			logger.info('Phantom exited with non-zero code. Some error occured.');
    			logger.debug('Sendind server error status: 500');
    			resp.sendStatus(500);
	        } else {
	        	logger.info('Sending content by length: ' + content.length);
	            resp.send(content);
	        }
	    });

	    phantom.on('error', function () {
	    	logger.info('Some error occured in the PhantomJS proccess. Clearing remaining resources.');
	    	phantom.kill('SIGKILL');
	    });

	    setTimeout(function () {
	    	phantom.kill('SIGKILL');
	    	logger.info('PhantomJS process reacher default timeout (' + defaultTimeout / 1000  + 's). Going to kill it');	
	    	logger.debug('Killed PhantomJS PID: ' + phantom.pid);
	    }, defaultTimeout);
    } else {
    	if (!args.redirect) {
    		logger.info('No location provided for proxy to.');
    		logger.debug('Sending not find status: 404');
    		resp.sendStatus(404);
    	} else {
    		next();
    	}
    }
});

if (args.redirect) {
	logger.info('Redirection enabled on: ' + args.redirect);
	app.use(function (req, resp, next) {
		var redirectURI = req.path + (Object.keys(req.query).length? '?' + Object.keys(req.query).map(function (key) {
			return key + '=' + req.query[key];
		}).join('&'): '');
		if (!!redirectURI) {
			logger.info('Redirection to path: ' + redirectURI);
			http.get(args.redirect + '/' + redirectURI, function (r) {
				var result = '';
				r.setEncoding('utf8');
				r.on('data', function (chunk) {
					result += chunk;
					logger.debug('Redirection retrived data chunk by length: ' + chunk.length);
				});

				r.on('end', function () {
					logger.debug('Redirection send content by length: ' + result.length);
					resp.send(result);
				});

				r.on('error', function (e) {
					logger.info('Redirection failed. Error status: ' + e.status);
				});
			});
		} else {
			next();
		}
	});
}

logger.info('Started listening on port ' + port);
app.listen(port);
