(function(angular) {
  angular.module('app')
    .service('myAppLogger', function() {
      var logger = function(name) {
        this.name = name;
        Object.defineProperty(this, 'name', {
          "enumerable": true,
          "configurable": false,
          "writable": false
        });
      };
      logger.prototype.log = function(out) {
        console.log('[LOG] - ' + curTimeString() + ' - "' + this.name + '": ', out);
      }
      logger.prototype.err = function(out) {
        console.error('[ERR] -  ' + curTimeString() + ' - "' + this.name + '": ', out);
      }

      return logger;
    });

  function curTimeString() {
    return new Date().toTimeString();
  }
})(angular);
