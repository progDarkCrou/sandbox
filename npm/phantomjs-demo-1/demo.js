var page = require('webpage').create();
page.open('http://kinoteka.co/#!/serial/abbatstvo-downtown/baibako/s6/e8', function(status) {
  console.log("Status: " + status);
  if(status === "success") {
    page.render('example.png');
  }
  phantom.exit();
});