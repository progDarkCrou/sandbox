((angular) ->
  app = angular.module 'application'

  app.controller 'filmCtrl', ['$route', '$log', ($route, $log) ->
    $log.info('Film controller loaded')

  ])(angular)