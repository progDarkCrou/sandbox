var app = require("express")();

app.use(function(req, resp, next) {
    var content = '';
    var url = req.query['location'];

    if (url) {
	    var phantom = require("child_process").spawn('phantomjs', ['phantom.js', url]);

	    phantom.stdout.setEncoding('utf8');
	    phantom.stdout.on('data', function(data) {
	        content += data.toString();
	    });
	    
	    phantom.on('exit', function(status_code) {
	        if (status_code !== 0) {
	            console.log('error');
	        } else {
	            resp.send(content);
	        }
	    });
    } else {
    	resp.sendStatus(404);
    	resp.send();
    }

})

app.listen(9999);
