(function(angular) {
  angular.module('app')
    .controller('mainCtrl', ['$scope', '$http', function($scope, $http) {
      $scope.users = {};
      $http.get($scope._config.user.base).then(function(resp) {
        $scope.users = resp.data;
      }).catch(function() {
        console.log('Cannot load user...');
        console.log('Set users to default');
        $scope.users = [{
          id: 0,
          name: '?',
          surname: '?',
          login: '?'
        }];
      });
    }]);
})(angular);
