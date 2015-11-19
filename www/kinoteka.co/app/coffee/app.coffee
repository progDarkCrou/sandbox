((angular) ->
  directives = angular.module 'directives', []
  app = angular.module 'application', ['ngRoute', 'directives']

  app.run(['ROUTES', '$rootScope', 'translations', (ROUTES, $rootScope, translations) ->
    $rootScope.ROUTES = ROUTES
    $rootScope.trans = translations
    $rootScope.locale = 'rus'

    $rootScope.homeFilter = '';

  ]))(angular)