((angular) ->
	module = angular.module 'application'

	module.controller 'homeCtrl', ['$route', '$scope', ($route, $scope) -> 
		$scope.elementsCount = [1..10]
		]
	)(angular)