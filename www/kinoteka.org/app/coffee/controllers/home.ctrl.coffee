((angular) ->
  module = angular.module 'application'

  module.controller 'homeCtrl', ['$route', '$scope', ($route, $scope) ->
    $scope.sections = [
      {
        name: 'New on site'
        items: [0..6]
      }
      {
        name: 'Last added'
        items: [0..6]
      }
    ]

    ])(angular)