(function(angular) {
  var app;
  app = angular.module('application');
  return app.controller('filmCtrl', [
    '$route', '$log', function($route, $log) {
      return $log.info('Film controller loaded');
    }
  ]);
})(angular);
