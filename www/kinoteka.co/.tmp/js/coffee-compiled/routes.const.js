(function(angular) {
  return angular.module('application').constant('ROUTES', {
    pathPrefix: '!',
    convert: function(path) {
      return '#' + this.pathPrefix + path;
    },
    film: '/film/',
    home: '/',
    about: '/about/',
    shows: '/shows/'
  });
})(angular);
