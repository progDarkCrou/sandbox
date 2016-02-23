(function(angular) {
  return angular.module('directives').directive('header', [
    '$log', '$location', 'ROUTES', function($log, $location, ROUTES) {
      return {
        restrict: 'A',
        templateUrl: '/templates/header.tmpl.html',
        controller: function() {
          return $log.info('Header loaded');
        },
        link: function(scope, elem) {
          var $document, $menu, $menuToggle, $searchForm, invisibleClass;
          scope.isActive = function(url) {
            var ref;
            return (url === (ref = ROUTES.convert(ROUTES.home)) && ref === ROUTES.convert($location.path())) || url !== ROUTES.convert(ROUTES.home) && -1 !== $location.absUrl().indexOf(url);
          };
          $menuToggle = angular.element('a.menu-toggle');
          $menu = angular.element($menuToggle.attr('data-toggle'));
          $searchForm = angular.element('.search-form');
          invisibleClass = 'invisible';
          $menuToggle.on('click', function() {
            $menu.toggleClass(invisibleClass);
          });
          $document = angular.element(document);
          $document.on('click', function(e) {
            if (!$searchForm.is(e.target) && !$searchForm.has(e.target).length) {
              if ($menu.has(e.target).length) {
                setTimeout(function() {
                  $menu.addClass(invisibleClass);
                }, 120);
              } else if (!($menuToggle.is(e.target) || $menuToggle.has(e.target).length)) {
                $menu.addClass(invisibleClass);
              }
            }
          });
          angular.element(window).resize(function() {
            if ($document.width() >= 720) {
              $menu.addClass(invisibleClass);
            }
          });
        }
      };
    }
  ]);
})(angular);
