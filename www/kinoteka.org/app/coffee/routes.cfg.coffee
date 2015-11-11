((angular) ->
	app = angular.module 'application'

	app.config ['$routeProvider', '$locationProvider', 'routes', ($routeProvider, $locationProvider, routes) ->
		$locationProvider.hashPrefix '!'

		$routeProvider.when routes.home,
			controller: 'homeCtrl'
			controllerAs: 'hc'
			templateUrl: '/templates/home.tmpl.html'
		$routeProvider.when routes.film + ':filmId',
			controller: 'filmCtrl'
			controllerAs: 'fm'
			templateUrl: '/templates/film/film.tmp.html'
		$routeProvider.when routes.about,
			templateUrl: '/templates/about.tmpl.html'

		$routeProvider.otherwise '/'
	])(angular)