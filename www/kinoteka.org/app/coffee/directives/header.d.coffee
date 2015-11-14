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
        console.log 'Clicked'
        if !$menuToggle.has e.target && !$menuToggle.is e.target
          $menu.addClass 'hidden'
        return
    }
  ])(angular)