(function() { // body...
  var style = document.createElement('style');
  style.innerText = 'html, body {width: 100%; height: 100%; margin: 0;}';
  document.head.appendChild(style);

  var visaTypeSelect = document.querySelector('select#ctl00_plhMain_cboVisaCategory');

  var emailContainer = document.createElement('div');
  emailContainer.style.width = '100%';
  emailContainer.style.height = '100%';
  emailContainer.style.backgroundColor = 'rgba(0,0,0,.7)';
  emailContainer.style.position = 'fixed';
  emailContainer.style.top = '0';
  emailContainer.style.left = '0';

  emailContainer.hidden = true;

  document.body.appendChild(emailContainer);

  var emailInput = document.createElement('input');
  emailInput.placeholder = 'Write email and pres enter...';
  emailInput.style.border = 'none';
  emailInput.style.backgroundColor = 'rgba(0,0,0,.5)';
  emailInput.style.position = 'fixed';
  emailInput.style.width = '100%';
  emailInput.style.height = '10%';
  emailInput.style.textAlign = 'center';
  emailInput.style.top = '50%';
  emailInput.style.transform = 'translateY(-50%);';
  emailInput.style.fontSize = '40px';
  emailInput.style.color = 'rgba(255,255,255,.7)';

  emailContainer.appendChild(emailInput);

  var request = {};

  function getEmail() {
    emailInput.onkeyup = function(e) {
      if (e.keyCode === 13) {
      	request.email = emailInput.value;
        chrome.runtime.sendMessage({
          type: 'gatheredRequest',
          request: request
        });
        emailContainer.hidden = true;
      	emailInput.value = '';
      } else if (e.keyCode === 27) {
      	emailContainer.hidden = true;
      	emailInput.value = '';
      }
    };
    emailContainer.hidden = false;
  }

  function gatherRequest(e) {

    if (e.target.value > -1) {

      var form = document.querySelector('form');

      var arrayOfInputs = [];
      arrayOfInputs.push(document.querySelector('input[name*="__EVENTTARGET"]'));
      arrayOfInputs.push(document.querySelector('input[name*="__EVENTARGUMENT"]'));
      arrayOfInputs.push(document.querySelector('input[name*="__LASTFOCUS"]'));
      arrayOfInputs.push(document.querySelector('input[name*="__VIEWSTATE"]'));
      arrayOfInputs.push(document.querySelector('input[name*="____Ticket"]'));
      arrayOfInputs.push(document.querySelector('input[name*="__VIEWSTATEENCRYPTED"]'));
      arrayOfInputs.push(document.querySelector('input[name*="__EVENTVALIDATION"]'));
      arrayOfInputs.push(document.querySelector('input[name*="ctl00$hidCSRF"]'));
      arrayOfInputs.push(document.querySelector('input[name*="ctl00$plhMain$tbxNumOfApplicants"]'));
      arrayOfInputs.push(document.querySelector('input[name*="ctl00$plhMain$txtChildren"]'));
      arrayOfInputs.push(document.querySelector('input[name*="recaptcha_challenge_field"]'));
      arrayOfInputs.push(document.querySelector('input[name*="recaptcha_response_field"]'));
      arrayOfInputs.push(document.querySelector('select[name*="ctl00$plhMain$cboVisaCategory"]'));

      request.referer = document.location.href;
      request.url = document.location.href.substring(0, document.location.href.lastIndexOf('/') + 1) + form.getAttribute('action');
      request.data = arrayOfInputs.map(function(e) {
        if (e) {
          var name = e.getAttribute('name');
          var value = e.value ? e.value : '';
          switch (name) {
            case '__EVENTTARGET':
              value = 'ctl00$plhMain$cboVisaCategory';
              break;
            case '__EVENTARGUMENT':
              value = '';
              break;
          };
          return encodeURIComponent(name) + '=' + encodeURIComponent(value);
        }
      }).reduce(function(a, b) {
        return a + '&' + b;
      });

      getEmail();

    }
  };

  chrome.runtime.onMessage.addListener(function(msg) {
    if (msg.type === 'gatherRequest') {
      console.log('Gathering requerst');
      visaTypeSelect.removeAttribute('onchange');
      var parent = visaTypeSelect.parentElement;
      var anotherSelect = document.createElement('select');
      anotherSelect.onchange = gatherRequest;

      for (var i = 0; i < visaTypeSelect.children.length; i++) {
        var s = document.createElement('option');
        anotherSelect.appendChild(s);
        s.outerHTML = visaTypeSelect.children.item(i).outerHTML;
      }

      parent.appendChild(anotherSelect);
      parent.removeChild(visaTypeSelect);
    }
  });
})();
