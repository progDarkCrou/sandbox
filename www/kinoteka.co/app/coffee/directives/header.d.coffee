((angular) ->
  angular.module('directives').directive 'header', ['$log', '$location', 'ROUTES', ($log, $location, ROUTES) ->
    templateUrl: '/templates/header.tmpl.html'
    controller: () ->
      $log.info 'Header loaded'

    link: (scope, elem) ->
      scope.isActive = (url) ->
                         url == ROUTES.convert(ROUTES.home) == ROUTES.convert($location.path()) or
                             url != ROUTES.convert(ROUTES.home) and -1 != $location.absUrl().indexOf(url)

      $menuToggle = angular.element 'a.menu-toggle'
      $menu = angular.element $menuToggle.attr 'data-toggle'

      $menuToggle.on 'click', () ->

        $menu.toggleClass 'hidden'
        return

      $document = angular.element document

      $document.on 'click', (e) ->
        if $menu.has(e.target).length
          setTimeout () ->
            $menu.addClass 'hidden'
            return
          , 120
        else if !($menuToggle.is(e.target) or $menuToggle.has(e.target).length)
          $menu.addClass 'hidden'
        return

      angular.element(window).resize () ->
        if $document.width() >= 720
          $menu.addClass 'hidden'
        return

      return
  ])(angular)