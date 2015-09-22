(function(angular) {
  angular.module('app')
    .controller('mainCtrl', ['$scope', '$http', function($scope, $http) {
    	$scope.users = {};
    	$http.get($scope.config.user.base).then(function (resp) {
    		$scope.users = resp.data? [{id: 1, name: 'Andri'}];
    	})
    }]);
})(angular);
