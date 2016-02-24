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
      controller: ['$scope', function ($scope) {

      }],
      link: function ($scope, elem, $attrs) {
        $scope.active = null;
        $scope.items = [
          {
            title: 'Hunger Games: Mockingjay',
            img: 'http://www.bringthenoiseuk.com/wordpress/wp-content/uploads/Hunger-Games-Catching-Fire-Quarter-Quell-Katniss-and-Peeta-640x480.jpg'
          },
          {
            title: 'Hunger Games: Mockingjay, part II',
            img: 'http://uk.eonline.com/eol_images/Entire_Site/201568/rs_634x977-150708125711-634.Jennifer-Lawrence-Katnis-Hunger-Games-Mickingjay-2.jl.070815.jpg'
          },
          {
            title: 'Notitle picture 2',
            img: 'none'
          },
          {
            title: 'Notitle picture',
            img: 'none'
          }
        ];

        $scope.active = $scope.items[Math.abs(Math.floor(Math.random() * $scope.items.length))];

        $scope.items.isActive = function (item) {
          return $scope.active === item;
        };

        $scope.items.setActive = function (item) {
          $scope.active = item;
        };

        $scope.$on('onrepeat.end', function () {
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
              $e.removeClass('invisible');
            });
          });

          var $slidesContainer = elem.find('.slider .slider-items > ul');

          var width = parseInt($slidesContainer.children('li').outerWidth(true)) || 0;

          var $active = $($slidesContainer.children().get($scope.$eval($attrs.getActive)));

          var $prev = $active.prev();
          var $next = $active.next();

          $prev.bind('click', leftPressed);
          $next.bind('click', rightPressed);

          //TODO: change this to more normal behavior
          $slidesContainer.translateToX(-($active.index() > -1 && $active.index() || 0) * width);

          function leftPressed(e) {
            var $this = $(this);
            $this.unbind('click', leftPressed);
            $this.next().next().unbind('click', rightPressed);

            $slidesContainer.translateRight(width);

            $this.prev().on('click', leftPressed);
            $this.next().on('click', rightPressed);
          }

          function rightPressed(e) {
            var $this = $(this);

            $this.unbind('click', rightPressed);
            $this.prev().prev().unbind('click', leftPressed);

            $slidesContainer.translateLeft(width);

            $this.prev().on('click', leftPressed);
            $this.next().on('click', rightPressed);
          }
        });
      }
    };
  }
})();