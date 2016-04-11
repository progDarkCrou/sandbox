// ==UserScript==
// @name         vk - friends adding
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        http*://vk.com/*
// @grant        none
// ==/UserScript==
/* jshint -W097 */
'use strict';

//Starting information
console.log('@crou - Add friend script loaded');

var $container = document.createElement('div');

var $startClickingButton = document.createElement('button');

$startClickingButton.innerText = 'Start clicking';

var $stopClickingButton = document.createElement('button');

$stopClickingButton.innerText = 'Stop clicking';
$stopClickingButton.style.display = 'none';

$container.style.position = 'fixed';
$container.style.top = '10px';
$container.style.right = '10px';

$startClickingButton.onclick = startChosing;
$stopClickingButton.onclick = stopClicking;

//Appending 
$container.appendChild($startClickingButton);
$container.appendChild($stopClickingButton);

document.body.appendChild($container);
document.body.insertBefore(document.body.children[0], $container);

var waitingForClick = [];

function stopClicking() {
    waitingForClick.forEach(function(e) {
        clearTimeout(e);
    });
    waitingForClick.length = 0;
    if (waitingForClick.length) {
        throw new Error('Failed to clear all clickers, left ' + waitingForClick);
    } else {
        console.log('Clicking stoped');
    }
    $stopClickingButton.style.display = 'none';
}

function startChosing() {
    // document.onmousedown = selectElement;
    document.onmousedown = selectElement;
}

function selectElement(event) {
    if (event.target.tagName === 'BUTTON' || event.target.tagName === 'A') {
        event.preventDefault();
        startClicking(event.target);
    }

    document.onmousedown = null;
}

var defaultCaptchaWaiting = 5000;
var defaultElementAmount = 40;
var defaultTimeout = 1000;

function startClicking(exampleButton) {
    if (!exampleButton) {
        throw new Error('Illegal example element, Got ' + exampleButton);
    }
    var selector = getSelector(exampleButton);

    console.log('Selector for finding - ' + selector);

    var buttons = document.querySelectorAll(selector);

    buttons = Array.prototype.filter.call(buttons, function(button) {
        return button.innerText === exampleButton.innerText && !button.attributes.style;
    }).slice(0, defaultElementAmount);

    console.log('Finded ' + buttons.length + ' the buttons to click');

    click(buttons[0], buttons);

    $stopClickingButton.style.display = 'inline-block';
}

function click(button, buttonsArray) {
    var index = buttonsArray.indexOf(button);
    var nextButton = buttonsArray[index + 1];
    waitingForClick.push(setTimeout(function() {
        if (isCaptcha()) {
            console.log('Clicker ' + index + ' Waiting for captcha exit');
            click(button, buttonsArray);
        } else {
            button.click();
            console.log('Clicker ' + index + ' clicked successful');
            if (nextButton) {
                click(nextButton, buttonsArray);
            } else {
                alert('Clicking ended');
                $stopClickingButton.click();
            }
        }
    }, defaultTimeout));
}

function isCaptcha() {
    var captcha = document.querySelectorAll('#box_layer_wrap #box_layer .box_layout .box_body .captcha');
    return captcha && captcha.length ? true : false;
}

//==== Utils 
function getSelector(elem) {
    if (!elem) {
        throw new Error('Illegal element to get selector. Got ' + elem);
    }

    if (elem === document.body) {
        return '';
    }

    if (elem.classList.length) {
        return getSelector(elem.parentElement) + ' .' + Array.prototype.reduce.call(elem.classList, function(a, b) {
            return a + '.' + b;
        });
    } else if (elem.id) {
        return getSelector(elem.parentElement) + ' #' + elem.id;
    }

    return elem.tagName.toLowerCase();
}

function removeFrom(array, elem) {
    var index = array.indexOf(elem);
    if (index > -1) {
        array.splice(index, 1);
    }
    return array;
}