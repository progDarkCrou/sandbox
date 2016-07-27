/**
 * Created by avorona on 06.06.16.
 */

import viewModule from "./view.module";
import serviceModule from "./service/service.module";

import ILogService = angular.ILogService;

let app = angular.module('app', [
  'ui.router',
  viewModule.name,
  serviceModule.name
]);

let run = ($log:ILogService)=> {
  $log.debug('Hello World, from Angular + WebPack');
};
run.$inject = ["$log"];

app.run(run);

angular.bootstrap($(document), [app.name]);
export default app;