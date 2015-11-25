/**
 * Created by crou on 25.11.15.
 */

(function () {
  "use strict";

  function Translator() {
  }

  Translator.constructor = Translator;
  Translator.prototype = {
    translateX: function (dist, distType) {
      if (typeof dist === 'string') {
        var args = /([-+]?[0-9]+)(\.[0-9]+)?(.{1,2})?/.exec(dist);
        dist = args[1];
        distType = args[3];
      }

      if (!dist) {
        throw new Error('Please enter first argument as string translate configuration or first argument as number of' +
                        ' move, and second as type of move [% or px]');
      }

      if (this.css) {
        var newTransformString = 'matrix(1,0,0,1,0,0)';
        var oldTransformString = this.css('transform');

        var transformMatrix = oldTransformString !== 'none' ? oldTransformString.split(',') :
                              newTransformString.split(',');
        var curPosX = +transformMatrix[4].trim();

        var nextPosX = distType === '%' && (dist * this.width() / 100) + +curPosX || curPosX + +dist;

        newTransformString =
            transformMatrix.slice(0, 4).join(',') + ',' + nextPosX + ',' + transformMatrix.slice(5).join(',');

        this.css({
          transform: newTransformString
        });
      }
    },
    translateRight: function (dist, distType) {
      this.translateX(dist, distType);
    },
    translateLeft: function (dist, distType) {
      this.translateX('-' + dist, distType);
    }
  };

  var $ = window.jQuery || window.$;

  if ($) {
    $.extend($.prototype, new Translator());
  }
})();