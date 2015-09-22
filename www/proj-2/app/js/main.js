(function (angular) {
	angular.module('app', []);
	angular.module('app').run(['$http', '$rootScope', function ($http, $rootScope) {
		console.log('--- App started ---');
		console.log('Run cofiguration');
		$rootScope._configServerURL = '/config'; //static fongid
		$rootScope._config = {
			user: {
				base: 'user'
			}
		};
	}]);
})(angular);