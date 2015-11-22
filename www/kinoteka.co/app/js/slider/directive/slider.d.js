/**
 * Created by crou on 21.11.15.
 */

(function () {
  "use strict";

  angular
      .module('directives')
      .directive('appSlider', [SliderDirective]);

  function SliderDirective() {
    return {
      restrict: 'A',
      templateUrl: 'templates/slider.html',
      link: function (scope, elem) {
        elem.find('.slider').find('img').each(function (n, e) {
          var $e = $(e);
          $e.one('load', function () {
            var ratio = $e.width() / $e.height();
            var containerRatio = 16 / 9.0;
            if (ratio < containerRatio) {
              $e.addClass('full-width');
            } else if (ratio >= containerRatio) {
              $e.addClass('full-height');
            }
          });
        });

        var $slidesContainer = elem.find('.slider').find('.slider-items > ul');

        var width = 0;
        var curPos = 0;

        var $active = initPosition();

        var $prev = $active.prev();
        var $next = $active.next();

        $prev.bind('click', rightPressed);
        $next.bind('click', leftPressed);

        $(window).on('resize', initPosition);

        function initPosition() {
          var $tmp = $slidesContainer.children('li');

          var $active = $tmp.filter('.active');
          var activeNumber = Math.floor($tmp.length / 2.0);

          if ($active.length) {
            activeNumber = $tmp.index($active.get(0));
          }

          $active = $($tmp.get(activeNumber)).addClass('active');

          width = $slidesContainer.children().outerWidth(true);
          curPos = -activeNumber * width;

          $slidesContainer.css({
            left: curPos
          });

          elem.find('.slider').find('img.invisible').each(function (n, e) {
            var $e = $(e).on('load', function () {
              $e.removeClass('invisible');
            });
          });

          return $active;
        }

        function leftPressed(e) {
          var $active = $slidesContainer.children('li.active:not(:last-child)');

          if ($active.length) {
            console.log(angular.element($active).scope());
            $prev.unbind('click', rightPressed);
            $prev.unbind('click', leftPressed);
            $next.unbind('click', leftPressed);
            $next.unbind('click', rightPressed);

            $active = $active.removeClass('active').next().addClass('active');

            $slidesContainer.css({
              left: curPos -= width
            });

            $prev = $active.prev().bind('click', rightPressed);
            $next = $active.next().bind('click', leftPressed);
          }
        }

        function rightPressed(e) {
          var $active = $slidesContainer.children('li.active:not(:first-child)');

          if ($active.length) {
            $prev.unbind('click', rightPressed);
            $prev.unbind('click', leftPressed);
            $next.unbind('click', leftPressed);
            $next.unbind('click', rightPressed);

            $active = $active.removeClass('active').prev().addClass('active');

            $slidesContainer.css({
              left: curPos += width
            });

            $prev = $active.prev().bind('click', rightPressed);
            $next = $active.next().bind('click', leftPressed);
          }
        }

      }
    };
  }
})();