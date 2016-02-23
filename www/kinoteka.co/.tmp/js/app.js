/**
 * Created by crou on 25.11.15.
 */
(function () {
  angular.module('directives', []);
  angular.module('ngUtils', []);

  var app = angular.module('application', ['ngRoute', 'ngUtils', 'directives']);

  app.run(['ROUTES', '$rootScope', 'translations', function (ROUTES, $rootScope, translations) {
    $rootScope.ROUTES = ROUTES;
    $rootScope.trans = translations;
    $rootScope.locale = 'rus';

    $rootScope.homeFilter = '';
  }]);
})();