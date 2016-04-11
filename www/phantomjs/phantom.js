var page = require('webpage').create(),
  system = require('system');

if (system.args.length === 1) {
  console.log('Usage: loadspeed.js <some URL>');
  phantom.exit(1);
}

address = system.args[1];
page.open(address, function(status) {
  setTimeout(function() {
    if (status !== 'success') {
      console.log('Failed to load the address: ' + address);
    } else {
      console.log(page.content);
    }
    phantom.exit();
  }, 10000);
});