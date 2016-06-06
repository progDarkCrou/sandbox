/**
 * Created by avorona on 06.06.16.
 */

require('angular/angular');
require('jquery/dist/jquery');
require('angular-ui-router/release/angular-ui-router');

import {HelloSayer} from "./utils";
import ILogService = angular.ILogService;

var helloSayer = new HelloSayer("My name is Andriy");
helloSayer.sayHello();

// $('body').text('Hello');

let app = angular.module('app', ['ui.router']);

let run = ($log: ILogService)=> {
  $log.debug('Hello from Angular + WebPack');
};

run.$inject = ["$log"];

app.run(run);

angular.bootstrap(window.document, ['app']);
export default app;