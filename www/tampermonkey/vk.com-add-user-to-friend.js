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

var $addButton = document.createElement('button');

$addButton.innerText = 'Start adding';
$addButton.style.position = 'fixed';
$addButton.style.top = '10px';
$addButton.style.right = '10px';

//Appending 
document.body.appendChild($addButton);
document.body.insertBefore(document.body.children[0], $addButton);