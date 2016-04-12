var page = require('webpage').create(),
  system = require('system');

if (system.args.length === 1) {
  console.log('Usage: loadspeed.js <some URL>');
  phantom.exit(1);
}

address = system.args[1];

var urlPattern = /^http(s)?\:\/\/([a-z0-9]{2,}\.){2,}/i;

page.open(address, function(status) {
  if (status !== 'success') {
    console.log('Failed to load the address: ' + address);
    phantom.exit(1);
  } else {
    // page.evaluate(function () {
    //   var config = {
    //     host: window.location.host,
    //     protocol: window.location.protocol
    //   }

    //   if ($ || window.$) {
    //     $('link').filter(function(n, elem) {
    //       var href = $(elem).prop('href');
    //       return !!href? !urlPattern.test(href): false;
    //     }).each(urlCompletion('href'));
    //     $('script').filter(function(n, elem) {
    //       var src = $(elem).prop('src');
    //       return !!src? !urlPattern.test(src): false;
    //     }).each(urlCompletion('src'));
    //   }
    // });
    console.log(page.content);
    phantom.exit(0);
  }
});

function urlCompletion(attrName, config) {
  function (n, elem) {
    var $this = $(elem);
    var attr = $this.prop(attrName);

    if (attr && urlPattern.test(attr)) {
      $this.prop(attrName, config.protocol + '//' + config.host + (attr.charAt(0) === '/'?'':'/') + attr);
    }
  }
}