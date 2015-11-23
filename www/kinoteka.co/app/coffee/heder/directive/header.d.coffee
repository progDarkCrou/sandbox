((angular) ->
  angular.module('directives').directive 'header', ['$log', '$location', 'ROUTES', ($log, $location, ROUTES) ->
    restrict: 'A'
    templateUrl: '/templates/header.tmpl.html'
    controller: () ->
      $log.info 'Header loaded'

    link: (scope, elem) ->
      scope.isActive = (url) ->
                         url == ROUTES.convert(ROUTES.home) == ROUTES.convert($location.path()) or
                             url != ROUTES.convert(ROUTES.home) and -1 != $location.absUrl().indexOf(url)

      $menuToggle = angular.element 'a.menu-toggle'
      $menu = angular.element $menuToggle.attr 'data-toggle'
      $searchForm = angular.element '.search-form'
      invisibleClass = 'invisible'

      $menuToggle.on 'click', () ->
        $menu.toggleClass invisibleClass
        return

      $document = angular.element document

      $document.on 'click', (e) ->
        if !$searchForm.is(e.target) and !$searchForm.has(e.target).length
          if $menu.has(e.target).length
            setTimeout () ->
              $menu.addClass invisibleClass
              return
            , 120
          else if !($menuToggle.is(e.target) or $menuToggle.has(e.target).length)
            $menu.addClass invisibleClass
          return

      angular.element(window).resize () ->
        if $document.width() >= 720
          $menu.addClass invisibleClass
        return

      return
  ])(angular)