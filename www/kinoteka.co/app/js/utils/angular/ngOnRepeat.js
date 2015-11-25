/**
 * Created by crou on 25.11.15.
 */

(function () {
  angular.module('ngUtils').directive('ngOnrepeat', [ngOnrepeat]);

  function ngOnrepeat() {
    return {
      restrict: 'A',
      priority: -1,
      terminal: true,
      require: 'ngRepeat',
      compile: function (elem, attrs) {
        var on = attrs.ngOnrepeat && attrs.ngOnrepeat.split(/[\s\,]+/);
        var eventName = attrs.ngOnRepeatEvent || 'onrepeat';

        return function ($scope) {
          if (!on) {
            throw new Error('No arguments added in the ngOnRepeat attribute. Please put at least on of the "start",' +
                            ' or' +
                            ' "end"');
          }

          if (on.indexOf('start')) {
            $scope.$watch(function () {
              return $scope.$first;
            }, function () {
              produceEvent('start');
            });
          }

          if (on.indexOf('end')) {
            $scope.$watch(function () {
              return $scope.$last;
            }, function () {
              produceEvent('end');
            });
          }

          function produceEvent(suffix) {
            var event = eventName + (eventName !== 'onrepeat' && '.repeat.' || '') + suffix;
            $scope.$emit(event);
            $scope.$broadcast(event);
          }
        };
      }
    };
  }
})();