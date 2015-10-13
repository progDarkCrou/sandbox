(function () {
  var availablePhrase = 'The next available slot';
  var unavailablePhrase = 'No date(s)';
  var invalidAttemptPhrase = 'Invalid';
  var frequency = 10000;
  var defaultV = '235';
  var defaultVName = 'National Visa';

  // *** initializing necessary fielrd int he view
  var initButtonInitInnerText = 'Click on me to init process';
  var initButtonClickedInnerText = 'I was clicked. To stop timer click on me again.';
  var initButton = document.createElement('button');
  initButton.innerText = initButtonInitInnerText;
  initButton.style.width = '100%';

  var vType = document.createElement('input');
  vType.id = 'vType';
  vType.value = defaultVName;
  vType.style.width = '100%';

  var vTypeLabel = document.createElement('label');
  vTypeLabel.setAttribute('for', vType.id);
  vTypeLabel.innerText = 'Write here visa type, or leave unchanged:';
  vTypeLabel.style.width = '100%';
  // ***

  var timer = function reload(url, data, time) {
    reload.timer = setTimeout(function t() {
      var xhtml = new XMLHttpRequest();
      xhtml.open('POST', url, true);
      xhtml.onloadend = onPostLoad(xhtml, function () {
        reload(url, data, time);
      });
      xhtml.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      xhtml.send(data);
    }, time);
  };

  var closeTimer = function () {
    clearTimeout(timer.timer);
    delete timer.timer;
    console.log('Check timer is off now.');
  };

  var onPostLoad = function (ajaxRequest, callback) {
    return function () {
      if(ajaxRequest.response) {
        if(ajaxRequest.response.indexOf(availablePhrase) !== -1) {
          callback = null;
          closeTimer();
          alert('Yeah!!! There is some date available. Please reload page, chose the category you need, answer captcha and click "Submit" button. Enjoy!!!');
        } else if(ajaxRequest.response.indexOf(unavailablePhrase) !== -1) {
          console.log('I am sorry, but no dates available now.');
        } else if(ajaxRequest.response.indexOf(invalidAttemptPhrase) !== -1) {
          alert('Something bad occured while posting, please reload page, and reinit me!!!');
        }
        if(callback) {
          callback();
        }
      }
    };
  };

  initButton.onclick = function () {

    if(timer.timer) {
      closeTimer();
      initButton.innerText = initButtonInitInnerText;
    } else {

      var vCategorySelect = document.querySelector('select[name*="ctl00$plhMain$cboVisaCategory"]');

      for(var i = 0; i < vCategorySelect.children.length; i++) {
        var child = vCategorySelect.children.item(i);
        if(child.innerText === vType.value) {
          defaultV = child.value;
        }
      }

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
      arrayOfInputs.push(vCategorySelect);

      var data = arrayOfInputs.map(function (e) {
          if(e) {
            var name = e.getAttribute('name');
            var value = e.getAttribute('value') ? e.getAttribute('value') : '';
            switch(name) {
            case '__EVENTTARGET':
              value = 'ctl00$plhMain$cboVisaCategory';
              break;
            case '__EVENTARGUMENT':
              value = '';
              break;
            case 'ctl00$plhMain$cboVisaCategory':
              value = defaultV;
              break;
            }
            return window.encodeURIComponent(name) + '=' + window.encodeURIComponent(value);
          }
        })
        .reduce(function (a, b) {
          return a + '&' + b;
        });

      timer(document.aspnetForm.getAttribute('action'), data, frequency);
      console.log('Timer is runnig now...');
      this.innerText = initButtonClickedInnerText;
    }


  };

  document.body.insertBefore(initButton, document.body.childNodes.item(0));
  document.body.insertBefore(vType, initButton);
  document.body.insertBefore(vTypeLabel, vType);

})();
