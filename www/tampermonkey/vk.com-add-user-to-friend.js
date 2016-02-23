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

$startClickingButton.innerText = 'Start adding';

var $stopClickingButton = document.createElement('button');

$stopClickingButton.innerText = 'Stop slicking';
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
	waitingForClick.forEach(function (e) {
		clearTimeOut(e);
		removeFrom(waitingForClick, e);
	});
}

function startChosing() {
	document.onmousedown = selectElement;
}

function selectElement(event) {
	if (event.target.tagName === 'BUTTON') {
		event.preventDefault();
		startClicking();
	}
	
	document.onmousedown = null;
}

var defaultCaptchaWaiting = 5000;
var defaultElementAmount = 40;

function startClicking(exampleButton) {
	var buttons = document.querySelectorAll(getSelector(exampleButton));

	buttons = buttons.slice(0, defaultElementAmount);

	buttons.forEach(click);

	$stopClickingButton.style.display = 'inline-block';
}

function click(button) {
	if (isCaptcha()) {
		setTimeout(click.bind(null, button));
	} else {
		button.click();
	}
}

function isCaptcha() {
	var captcha = document.querySelectorAll('#box_layer_wrap #box_layer .box_layout .box_body .captcha');
	return captcha && captcha.length && true || false;
}

//==== Utils 

function getSelector(elem) {
	if (elem === document.body) {
		return '';
	}

	if (!elem) {
		return null;
	}

	if (elem ) {

	}

	if (elem.id) {
		return getSelector(elem.parentElement) + ' #' + elem.id;
	} else if (elem.classList.length) {
		return getSelector(elem.parentElement) + ' .' + Array.prototype.reduce.call(elem.classList, function (a, b) {
			return a + '.' b;
		});
	}
	
	return elem.tagName.toLowerCase();
}

function removeFrom(array, elem) {
	var index = array.indexOf(elem);
	if (index > -1) {
		array.splice(index, 1);
		return array;
	}
}
