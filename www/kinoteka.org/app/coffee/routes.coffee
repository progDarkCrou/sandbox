((angular) ->
	app = angular.module 'application'

	app.config ['$routeProvider', ($routeProvider) -> 
		$routeProvider.when '/',
			controller: 'homeCtrl'
			controllerAs: 'hc'
			templateUrl: '/templates/home.tmp.html'
		$routeProvider.when '/film/:filmId',
			controller: 'filmCtrl'
			controllerAs: 'fm'
			templateUrl: '/templates/film/film.tmp.html'

		$routeProvider.otherwise '/'
		]
	)(angular)