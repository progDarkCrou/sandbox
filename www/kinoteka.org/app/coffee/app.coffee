((angular) ->
  directives = angular.module 'directives', []
  app = angular.module 'application', ['ngRoute', 'directives']

  app.run(['routes', '$rootScope', 'translations', (routes, $rootScope, translations) ->
    $rootScope.routes = routes
    $rootScope.trans = translations
    $rootScope.locale = 'eng'

  ]))(angular)