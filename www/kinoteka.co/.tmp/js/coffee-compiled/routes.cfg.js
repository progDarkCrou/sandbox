(function(angular) {
  var app;
  app = angular.module('application');
  return app.config([
    '$routeProvider', '$locationProvider', 'ROUTES', function($routeProvider, $locationProvider, ROUTES) {
      $locationProvider.hashPrefix('!');
      $routeProvider.when(ROUTES.home, {
        controller: 'homeCtrl',
        controllerAs: 'hc',
        templateUrl: '/templates/home.tmpl.html'
      });
      $routeProvider.when(ROUTES.film + ':filmId', {
        controller: 'filmCtrl',
        controllerAs: 'fm',
        templateUrl: '/templates/film/film.tmp.html'
      });
      $routeProvider.when(ROUTES.about, {
        templateUrl: '/templates/about.tmpl.html'
      });
      return $routeProvider.otherwise('/');
    }
  ]);
})(angular);
