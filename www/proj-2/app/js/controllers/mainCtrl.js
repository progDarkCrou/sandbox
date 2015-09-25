(function(angular) {
  angular.module('app')
    .controller('mainCtrl', ['$scope', '$http', 'myAppLogger', '$timeout', function($scope, $http, myAppLogger, $timeout) {
      var logger = new myAppLogger('mainCtrl');
      $scope.users = [];
      $scope.newUser = {};
      var self = this;

      $scope.users.load = function() {
        logger.log('Loading users...')
        $scope.users.loading = true;
        $http.get($scope._config.user.base).then(function(resp) {
          $scope.users.clear();
          $scope.users.addAll(resp.data);
          $scope.users.loading = false;
        }).catch(function() {
          logger.log('Cannot load user...');
          logger.log('Set users to default');
          // $scope.users.clear();
          $scope.users.loading = false;
        });
      };

      $scope.users.create = function(user) {
        $http.post($scope._config.user.base, {
          data: user
        }).then(function(resp) {
          $scope.users.push(resp.data);
        });
      };

      $scope.users.delete = function (user) {
      	logger.log('Deleting user '+ user.id +' from users');
      	$http.delete($scope._config.user.base + user.id).then(function (resp) {
      		if (resp.data === true) {
      			$scope.users.remove(user);
      		}
      	});
      };

      //Init
      $scope.$on('configLoaded', $scope.users.load);

    }]);
})(angular);
