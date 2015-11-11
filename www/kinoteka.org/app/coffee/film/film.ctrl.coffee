((angular) ->
	app = angular.module 'application'

	app.controller 'filmCtrl', ['$route', ($route) -> 
		console.log $route

		]
	
	)(angular)