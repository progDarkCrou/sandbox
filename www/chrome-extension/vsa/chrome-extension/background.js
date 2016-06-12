(function() {

  chrome.pageAction.onClicked.addListener(checkOut);

  var checkingOut = false;

  var mainSiteUrl = 'http://www.polandvisa-ukraine.com/English/scheduleappointment_2.html';

  var notificationsImage = 'vsa.png';
  var bp = chrome.extension.getBackgroundPage();

  function startCheckOut() {
    var req = new XMLHttpRequest();
    req.open('GET', mainSiteUrl, true);
    req.onloadend = function() {
      if (req.status === 200) {
        var html = wrapResponse(req.response);
        var iframe = html.querySelector('iframe');
        if (iframe.hasAttribute('src')) {
          var url = iframe.getAttribute('src');
          firstStep(url);
        }
      } else {
        sendNotification('Start check out', 'Cannot rich main page');
      }
    };
    req.send();
  }

  function firstStep(url) {
    var baseUrl = url.substring(0, url.lastIndexOf('/') + 1);

    var req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.onloadend = function() {
      var html = req.response;
      if (req.status === 200) {
        var html = wrapResponse(req.response);
        var form = html.querySelector('form');

        var nextUrl = baseUrl + html.getAttribute('action');

        var dataPOSTCollection = [];

        dataPOSTCollection.push({
          name: '__EVENTTARGET',
          value: 'ctl00$plhMain$lnkSchApp'
        });
        dataPOSTCollection.push({
          name: '__EVENTARGUMENT',
          value: ''
        });
        dataPOSTCollection.push({
          name: '__VIEWSTATE',
          value: form.querySelector('input[name="__VIEWSTATE"]').value
        });
        dataPOSTCollection.push({
          name: '____Ticket',
          value: form.querySelector('input[name="____Ticket"]').value
        });
        dataPOSTCollection.push({
          name: '__VIEWSTATEENCRYPTED',
          value: form.querySelector('input[name="__VIEWSTATEENCRYPTED"]').value
        });
        dataPOSTCollection.push({
          name: '__EVENTVALIDATION',
          value: form.querySelector('input[name="__EVENTVALIDATION"]').value
        });
        dataPOSTCollection.push({
          name: 'ctl00$hidCSRF',
          value: form.querySelector('input[name="ctl00$hidCSRF"]').value
        });

        var data = dataPOSTCollection.map(function(e) {
          return window.encodeURIComponent(e.name) + '=' + window.encodeURIComponent(e.value);
        }).reduce(function(a, b) {
          return a + '&' + b;
        });

        // secondStep(baseUrl, url, data);
        html.querySelector('a#ctl00_plhMain_lnkSchApp').click();
      } else {
        sendNotification('Step 1', 'Cannot rich first page');
      }
    };
    req.send();
  }

  function secondStep(baseUrl, url, data) {
    var req = new XMLHttpRequest();
    req.open('POST', url, true);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.setRequestHeader('Origin', null);
    req.onloadend = function() {
      if (req.status === 200) {
        var html = wrapResponse(req.response);
        if (html.querySelector('#ctl00_plhMain_lblSchedule')) {
          sendNotification('Step 2', 'Successfuly riched second page');
        } else {
          sendNotification('Step 2', 'Cannot rich second page');
        }
      }
    };
    req.send(data);
  }

  function checkOut() {
    if (checkingOut) {
      checkingOut = false;
    } else {
      startCheckOut();
      checkingOut = true;
    }
  }

  function wrapResponse(requestResponse) {
    var html = document.createElement('html');
    html.innerHTML = requestResponse;
    return html;
  }

  function sendNotification(title, message) {
    chrome.notifications.create({
      iconUrl: notificationsImage,
      type: 'basic',
      title: title,
      message: message
    });
  }

})();
