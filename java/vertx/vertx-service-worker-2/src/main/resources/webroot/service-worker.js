/**
 * Created by avorona on 2016-08-04.
 */
"use strict";

var messageSender = {
  timeout: 1000,
  messaging: false,
  _timer: null,
  _sendMessage: function (client) {
    var self = this;
    this._timer = setTimeout(function () {
      client.postMessage({body: 'Hello from the Service Worker'});
      if (self.messaging) {
        self._sendMessage(client);
      }
    }, this.timeout);
  },
  startMessaging: function (client) {
    this.messaging = true;
    this._sendMessage(client);
  },
  stopMessaging: function () {
    this.messaging = false;
    clearTimeout(this._timer);
  }
};

self.addEventListener('install', function (e) {
  console.log('Server worker: installing... ');
  console.log(e);
});

self.addEventListener('activate', function (e) {
  console.log('Server worker: activating... ');
  console.log(e);
});

self.addEventListener('fetch', function (e) {
  console.log('Server worker: fetching... ');
  console.log(e);
  e.respondWith(fetch(e.request));
});

self.addEventListener('message', function (e) {
  console.log('Message...');
  if (e.data.startMessaging) {
    clients.matchAll()
        .then(function (cls) {
          messageSender.startMessaging(cls[0]);
        });
  }
});
