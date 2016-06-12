import {WindowManagerService} from "./window-manager.service";
/**
 * Created by avorona on 07.06.16.
 */

export default angular.module('app.services', [])
    .service(WindowManagerService._name, WindowManagerService);