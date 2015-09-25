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
  Array.prototype.remove = function (e) {
    this.splice(this.indexOf(e), 1);
  }
})(angular);
