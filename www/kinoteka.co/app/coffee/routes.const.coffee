((angular) ->
  angular.module('application').constant 'ROUTES', {
    pathPrefix: '!'
    convert: (path) ->
                   return '#' + this.pathPrefix + path
    film: '/film/'
    home: '/'
    about: '/about/'
    shows: '/shows/'
  })(angular)