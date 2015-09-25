(function(angular) {
  angular.module('app', ['ui.router']);
  angular.module('app').run(['$http', '$rootScope', '$location', 'myAppLogger', function($http, $rootScope, $location, myAppLogger) {
    var logger = new myAppLogger('app configuration');
    logger.log('App started');
    logger.log('Run cofiguration');

    $rootScope._configServerURL = '/config.json'; //static config
    $rootScope._config = {};
    $http.get($rootScope._configServerURL).then(function (resp) {
    	$rootScope._config = angular.recursiveCopy({}, resp.data);
    	$rootScope.$broadcast('configLoaded');
    });
    $rootScope.$on('$locationChangeSuccess', function (e, nl, ol) {
    	logger.log(nl);
    	logger.log(ol);
    	logger.log(e);
    });
  }]);
})(angular);

(function(angular) {
	
  angular.className = function(obj, toLowerCase) {
    var className = Object.prototype.toString.call(obj).slice(8, -1);
    return !toLowerCase ? className : className.toLowerCase();
  };

  angular.recursiveCopy = function recCopy(dest, from) {
    var to = {};
    if (arguments.length === 0) {
      throw new Error("RecursiveCopy: nothing to copy.");
    } else if (arguments.length === 1) {
      from = dest;
    } else if (arguments.length === 2) {
      to = dest;
    } else if (arguments.length > 2) {
      Array.prototype.forEach.call(arguments, function(e) {
        to = recCopy(to, e);
      });
    }

    for (var key in from) {
      if (from.hasOwnProperty(key)) {
        var f = from[key];
        switch (angular.className(f)) {
          case 'Object':
            to[key] = {};
            recCopy(to[key], f);
            break;
          case 'Array':
            to[key] = f.slice();
            break;
          default:
            to[key] = f;
        }
      }
    }
    return to;
  };

  Array.prototype.clear = function () {
    this.length = 0;
  };

  Array.prototype.addAll = function (array) {
    Array.prototype.push.apply(this, array);
  }
})(angular);

(function(angular) {
  angular.module('app')
    .config(['$stateProvider', function($stateProvider) {
      $stateProvider
        .state('user', {
          url: '/user',
          views: {
            "main": {
            	controller: 'mainCtrl',
              templateUrl: '/templates/user/user-list.html'
            },
            "main.submain": {
            	template: 'this is submain view'
            }
          }
        })
        .state('main', {
          url: '/',
          views: {

          }
        });
    }]);
})(angular);

(function(angular) {
  angular.module('app')
    .controller('mainCtrl', ['$scope', '$http', 'myAppLogger', '$timeout', function($scope, $http, myAppLogger, $timeout) {
      var logger = new myAppLogger('mainCtrl');
      $scope.users = [];
      $scope.newUser = {};
      var self = this;

      $scope.users.reload = function() {
        logger.log('Loading users...')
        $scope.users.loading = true;
        $http.get($scope._config.user.base).then(function(resp) {
          $scope.users.clear();
          $scope.users.addAll(resp.data);
          $scope.users.loading = false;
        }).catch(function() {
          logger.log('Cannot load user...');
          logger.log('Set users to default');
          // $scope.users.clear();
          $scope.users.loading = false;
        });
      };

      $scope.users.add = function(user) {
        $http.post($scope._config.user.base + 'create', {
          data: user
        }).then(function(resp) {
          $scope.users.push(resp.data);
        });
      };

      $scope.users.remove = function (user) {
      	logger.log('Deleting user '+ user.id +' from users');
      };

      //Init
      $scope.$on('configLoaded', $scope.users.reload);

    }]);
})(angular);

(function(angular) {
  angular.module('app')
    .service('myAppLogger', function() {
      var logger = function(name) {
        this.name = name;
        Object.defineProperty(this, 'name', {
          "enumerable": true,
          "configurable": false,
          "writable": false
        });
      };
      logger.prototype.log = function(out) {
        console.log('[LOG] - ' + curTimeString() + ' - "' + this.name + '": ', out);
      }
      logger.prototype.err = function(out) {
        console.error('[ERR] -  ' + curTimeString() + ' - "' + this.name + '": ', out);
      }

      return logger;
    });

  function curTimeString() {
    return new Date().toTimeString();
  }
})(angular);
