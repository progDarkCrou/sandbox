(function (angular) {
	angular.module('app', []);
	angular.module('app').run(['$http', '$rootScope', function ($http, $rootScope) {
		console.log('--- App started ---');
		console.log('Run cofiguration');
		$rootScope.configServerURL = '/config'; //static fongid
		$rootScope.config = {
			user: {
				base: 'user'
			}
		};
	}]);
})(angular);