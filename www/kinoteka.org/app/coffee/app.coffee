((angular) ->

	directives = angular.module 'directives', []
	app = angular.module 'application', ['ngRoute', 'directives']

	app.run(-> 
		console.log 'Application started'
		);
	)(angular)