var page = require('webpage').create(),
    system = require('system');

if (system.args.length === 1) {
    console.log('Usage: loadspeed.js <some URL>');
    phantom.exit(1);
}

var address = system.args[1];

var urlPattern = new RegExp('^http(s)?\:\/\/([a-z0-9]{1,}\.){1,}[a-z0-9]', 'i');

page.onConsoleMessage = function (msg) {
    'use strict';
    console.log(msg);
};

page.open(address, function (status) {
    'use strict';
    if (status !== 'success') {
        console.log('Failed to load the address: ' + address);
        phantom.exit(1);
    } else {
        setTimeout(function () {
            page.evaluate(function (urlPatternString, urlCompletionFunc) {
                eval(urlCompletionFunc);
                var urlPattern = new RegExp(urlPatternString);

                var config = {
                    host: window.location.host,
                    protocol: window.location.protocol
                };

                if ($ || window.$) {
                    $('link[href]').each(urlCompletion('href', config));
                    $('script').remove();
                }
            }, urlPattern.source, urlCompletion.toString());
            console.log(page.content);
            phantom.exit(0);
        }, 3 * 1000);
    }
});

function urlCompletion(attrName, config) {
    return function (n, elem) {
        var attr = elem.attributes[attrName].value;
        if (attr && !urlPattern.test(attr)) {
            elem[attrName] = config.protocol + '//' + config.host + (attr.charAt(0) === '/' ? '' : '/') + attr;
        }
    };
}
