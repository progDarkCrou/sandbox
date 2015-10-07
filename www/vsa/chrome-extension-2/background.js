var url = 'http://localhost:8080/checker/init';

chrome.runtime.onInstalled.addListener(function() {
  chrome.declarativeContent.onPageChanged.removeRules(undefined, function() {
    chrome.declarativeContent.onPageChanged.addRules([{
      conditions: [
        new chrome.declarativeContent.PageStateMatcher({
          pageUrl: {
            urlContains: 'AppScheduling.aspx'
          },
        }),
        new chrome.declarativeContent.PageStateMatcher({
          pageUrl: {
            urlContains: 'polandvisa-ukraine.com'
          },
          css: ['iframe[src*="polandonline.vfsglobal.com"]']
        })
      ],
      actions: [new chrome.declarativeContent.ShowPageAction()]
    }]);
  });
});


chrome.pageAction.onClicked.addListener(function(tab) {
  if (tab.url.match(/https:\/\/polandonline\.vfsglobal\.com\/.*\/AppScheduling.aspx.*/g)) {
    chrome.tabs.sendMessage(tab.id, {
      type: 'gatherRequest'
    });
  }
  chrome.tabs.sendMessage(tab.id, {
    type: 'getUrlToOpen'
  });
});

chrome.runtime.onMessage.addListener(function(msg) {
  if (msg.type === 'openTab') {
    chrome.tabs.create({
      url: msg.url
    });
  }
});

chrome.runtime.onMessage.addListener(function(msg) {
  if (msg.type === 'gatheredRequest') {
    var xhtml = new XMLHttpRequest();
    xhtml.open('POST', url, true);
    xhtml.onloadend = function() {
      if (xhtml.response) {
        var data = JSON.parse(xhtml.response);
        if (data.result) {
          chrome.notifications.create({
            type: 'basic',
            iconUrl: 'vsa.png',
            title: 'Successfuly created checker',
            message: 'Checker id: ' + data.result
          });
        } else if (data.error) {
          chrome.notifications.create({
            type: 'basic',
            iconUrl: 'vsa.png',
            title: 'Error while creating checker',
            message: 'Error message: ' + data.error
          });
        }
      }
    };
    xhtml.setRequestHeader('Content-Type', 'application/json');
    xhtml.send(JSON.stringify(msg.request));
  }
});
