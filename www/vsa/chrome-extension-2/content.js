chrome.runtime.onMessage.addListener(function(msg, sender, sendResponse) {
  if (msg.text && (msg.text == 'sendRequest')) {
    chrome.runtime.sendMessage({
      openTabUrl: document.querySelector('iframe').getAttribute('src')
    });
  }
});

chrome.runtime.onMessage.addListener(function(msg, sender, sendResponse) {
	if (msg.type === 'getUrlToOpen') {
		chrome.runtime.sendMessage({
			type: 'openTab',
			url: document.querySelector('iframe').getAttribute('src')
		});
	}
});
