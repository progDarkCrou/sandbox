(function () {
  //  ***********************************Initialization*******************************
  document.body.style.margin = "0";

  var visaTypeSelect = document.querySelector('select#ctl00_plhMain_cboVisaCategory');
  var request = {};

  var personalDataContainer = document.createElement('div');
  personalDataContainer.id = 'personalDataContainer';
  personalDataContainer.hidden = true;

  var wraper = document.createElement('div');
  wraper.id = 'wraper';

  var nameInput = document.createElement('input');
  nameInput.placeholder = 'Name...';
  nameInput.id = 'nameInput';

  var emailInput = document.createElement('input');
  emailInput.placeholder = 'Email (@gmail.com)...';
  emailInput.id = 'emailInput';

  document.body.appendChild(personalDataContainer);
  personalDataContainer.appendChild(wraper);
  wraper.appendChild(nameInput);
  wraper.appendChild(emailInput);

  personalDataContainer.hide = function () {
    nameInput.value = '';
    emailInput.value = '';
    personalDataContainer.hidden = true;
  };

  personalDataContainer.onkeydown = function (e) {
    if(e.keyCode === 27) {
      personalDataContainer.hide();
    }
    if(e.keyCode === 13) {
      confirm();
    }
  };

  personalDataContainer.onclick = function (e) {
    if(e.target === personalDataContainer) {
      personalDataContainer.hide();
    }
  };

  // ***********************************************************************************

  emailInput.onkeyup = function () {
    request.email = emailInput.value;
  };

  nameInput.onkeyup = function () {
    request.name = nameInput.value;
  };

  function confirm() {
    if(!nameInput.value) {
      nameInput.focus();
    } else if(!emailInput.value || !emailInput.value.match(/gmail\.com/g)) {
      emailInput.focus();
    } else {
      chrome.runtime.sendMessage({
        type: 'gatheredRequest',
        request: request
      });
      personalDataContainer.hide();
    }
  }

  function getEmail() {
    personalDataContainer.hidden = false;
  }

  function gatherRequest(e) {

    if(e.target.value > -1) {

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
      request.data = arrayOfInputs.map(function (e) {
          if(e) {
            var name = e.getAttribute('name');
            var value = e.value ? e.value : '';
            switch(name) {
            case '__EVENTTARGET':
              value = 'ctl00$plhMain$cboVisaCategory';
              break;
            case '__EVENTARGUMENT':
              value = '';
              break;
            };
            return encodeURIComponent(name) + '=' + encodeURIComponent(value);
          }
        })
        .reduce(function (a, b) {
          return a + '&' + b;
        });

      getEmail();

    }
  };

  chrome.runtime.onMessage.addListener(function (msg) {
    if(msg.type === 'gatherRequest') {
      console.log('Gathering requerst');
      visaTypeSelect.removeAttribute('onchange');
      var parent = visaTypeSelect.parentElement;
      var anotherSelect = document.createElement('select');
      anotherSelect.onchange = gatherRequest;
      anotherSelect.name = visaTypeSelect.name;

      for(var i = 0; i < visaTypeSelect.children.length; i++) {
        var s = document.createElement('option');
        anotherSelect.appendChild(s);
        s.outerHTML = visaTypeSelect.children.item(i)
          .outerHTML;
      }

      parent.appendChild(anotherSelect);
      parent.removeChild(visaTypeSelect);
    }
  });
})();
