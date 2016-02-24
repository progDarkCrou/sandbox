/**
 * Created by crou on 25.11.15.
 */

(function () {
  angular.module('ngUtils').directive('ngOnrepeat', [ngOnrepeat]);

  function ngOnrepeat() {
    return {
      restrict: 'A',
      priority: -1,
      compile: function (elem, attrs) {
        if (!attrs.ngRepeat) {
          throw new Error('ngOnrepeat requires to be used on the DOM element whit ngRepeat directive');
        }
        var on = attrs.ngOnrepeat && attrs.ngOnrepeat.split(/[\s\,]+/);
        var eventName = attrs.ngOnRepeatEvent || 'onrepeat';

        return function ($scope) {
          if (!on) {
            throw new Error('No arguments added in the ngOnRepeat attribute. Please put at least on of the "start",' +
                            ' or' +
                            ' "end"');
          }

          if ($scope.$first && on.indexOf('start') > -1) {
            produceEvent('start');
          }

          if ($scope.$last && on.indexOf('end') > -1) {
            produceEvent('end');
          }

          function produceEvent(suffix) {
            var event = eventName + (eventName !== 'onrepeat' && '.repeat.' || '.') + suffix;
            $scope.$emit(event);
            $scope.$broadcast(event);
          }
        };
      }
    };
  }
})();