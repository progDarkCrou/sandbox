/**
 * Created by crou on 25.11.15.
 */

(function () {
  "use strict";

  function Translator() {
  }

  var defaultTransformMatrix = [1, 0, 0, 1, 0, 0];
  var positionRegExp = /(([-+]?[0-9]+)(\.[0-9]+)?)(.{1,2})?/;

  Translator.constructor = Translator;
  Translator.prototype = {
    translationMatrix: function () {
      var ts = this.css('transform');
      if (ts === 'none') {
        return defaultTransformMatrix;
      }
      return ts.split(',').map(function (e) {
        return parseInt(positionRegExp.exec(e)[1]);
      });
    },
    translateToX: function (posX) {
      posX = positionRegExp.exec('' + posX)[1];
      var m = this.translationMatrix();
      m[4] = posX;
      return this.css('transform', 'matrix(' + m.join(',') + ')');
    },
    translateX: function (dist, distType) {
      //If there is no arguments putted - function act like getter
      if (arguments.length === 0) {
        return this.translationMatrix()[4];
      }

      if (!dist) {
        throw new Error('Please enter first argument as string translate configuration for X coordinate, ' +
                        'or first argument as number of move, and second argument as type of move [% or px]');
      }

      //Transform arguments to proper form
      if (typeof dist === 'string') {
        var args = positionRegExp.exec(dist);
        dist = args[1];
        distType = args[3];
      }

      if (this.css) {
        var curPosX = this.translateX();
        var nextPosX = distType === '%' && (dist * this.width() / 100) + curPosX || curPosX + +dist;

        return this.translateToX(nextPosX);
      }
    },
    translateRight: function (dist, distType) {
      return this.translateX(dist, distType);
    },
    translateLeft: function (dist, distType) {
      return this.translateX('-' + dist, distType);
    },
    translateToY: function (posY) {
      posY = positionRegExp.exec('' + posY)[1];
      var m = this.translationMatrix();
      m[5] = posY;
      return this.css('transform', 'matrix(' + m.join(',') + ')');
    },
    translateY: function (dist, distType) {
      //If there is no arguments putted - function act like getter
      if (arguments.length === 0) {
        return this.translationMatrix()[5];
      }

      if (!dist) {
        throw new Error('Please enter first argument as string translate configuration for X coordinate, ' +
                        'or first argument as number of move, and second argument as type of move [% or px]');
      }

      //Transform arguments to proper form
      if (typeof dist === 'string') {
        var args = positionRegExp.exec(dist);
        dist = args[1];
        distType = args[3];
      }

      if (this.css) {
        var curPosY = this.translateY();
        var nextPosY = distType === '%' && (dist * this.width() / 100) + curPosY || curPosY + +dist;

        return this.translateToY(nextPosY);
      }
    },
    translateUp: function (dist, distType) {
      return this.translateY(dist, distType);
    },
    translateDown: function (dist, distType) {
      return this.translateY('-' + dist, distType);
    }
  };

  var $ = window.jQuery || window.$;

  if ($) {
    $.extend($.prototype, new Translator());
  }
})();