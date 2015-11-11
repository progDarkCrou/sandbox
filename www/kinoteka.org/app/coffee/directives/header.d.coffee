((angular) ->
  angular.module('directives').directive 'header', ['$log', ($log) ->
    {
    templateUrl: '/templates/header.tmpl.html'
    controller: () ->
      $log.info 'Header loaded'
    }
  ])(angular)