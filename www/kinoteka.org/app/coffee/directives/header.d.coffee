((angular) ->
  angular.module('directives').directive 'header', ['$log', ($log) ->
    {
    templateUrl: '/templates/header.tmpl.html'
    controller: () ->
      $log.info 'Header loaded'

      $menuToggle = angular.element '.menu-toggle'
      $menu = angular.element '.navigation-menu'

      $menuToggle.on 'click', () ->
        $menu.toggleClass 'visible'
        $menu.toggleClass 'hidden'
    }
  ])(angular)