((angular) ->
  module = angular.module 'application'

  module.controller 'homeCtrl', ['$route', '$scope', ($route, $scope) ->
    $scope.sections = [
      {
        name: 'New on site'
        items: [
          {
            title: 'Iron man'
            year: 2010
          }
          {
            title: 'Iron man 2'
            year: 2011
          }
          {
            title: 'Iron man 3'
            year: 2012
          }
          {
            title: 'Thor'
            year: 2013
          }
          {
            title: 'Avengers'
          }
          {
            title: 'Avengers 2'
          }
          {
            title: 'Hulk'
          }
        ]
      }
    ]

  ])(angular)