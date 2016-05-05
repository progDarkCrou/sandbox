(function(angular) {
  angular.module('app')
    .config(['$stateProvider', function($stateProvider) {
      $stateProvider
        .state('user', {
          url: '/user',
          views: {
            "main": {
            	controller: 'mainCtrl',
              templateUrl: '/templates/user/user-list.html'
            },
            "main.submain": {
            	template: 'this is submain view'
            }
          }
        })
        .state('main', {
          url: '/',
          views: {

          }
        });
    }]);
})(angular);
