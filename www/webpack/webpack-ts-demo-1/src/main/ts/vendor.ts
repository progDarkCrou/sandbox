/**
 * Created by avorona on 07.06.16.
 */

export default () => {
    global.$ = global.jQuery = require('jquery');
    global.angular = require('angular');
    require('angular-ui-router');
};