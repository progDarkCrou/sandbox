((angular) ->
  angular.module('directives').directive 'header', ['$log', ($log) ->
    {
    templateUrl: '/templates/header.tmpl.html'
    controller: () ->
      $log.info 'Header loaded'

    compile: () ->
      $menuToggle = angular.element '.menu-toggle'
      $menu = angular.element '.navigation-menu'

      $menuToggle.on 'click', () ->
        $menu.toggleClass 'hidden'
        return

      angular.element(document).on 'click', (e) ->
        if !($menuToggle.is(e.target) or $menuToggle.has(e.target).length)
          $menu.addClass 'hidden'
        return
    }
  ])(angular)