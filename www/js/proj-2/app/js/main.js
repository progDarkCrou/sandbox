(function(angular) {
  angular.module('app', ['ui.router']);
  angular.module('app').run(['$http', '$rootScope', '$location', 'myAppLogger', function($http, $rootScope, $location, myAppLogger) {
    var logger = new myAppLogger('app configuration');
    logger.log('App started');
    logger.log('Run cofiguration');

    $rootScope._configServerURL = '/config.json'; //static config
    $rootScope._config = {};
    $http.get($rootScope._configServerURL).then(function (resp) {
    	$rootScope._config = angular.recursiveCopy({}, resp.data);
    	$rootScope.$broadcast('configLoaded');
    });
    $rootScope.$on('$locationChangeSuccess', function (e, nl, ol) {
    	logger.log(nl);
    	logger.log(ol);
    	logger.log(e);
    });
  }]);
})(angular);
