/**
 * Created by avorona on 06.06.16.
 */

import app from "./main";

import ILogService = angular.ILogService;

export class MainController {

    public static $inject = ['$log'];
    constructor(private _log: ILogService) {
        _log.debug('Main controller initialized');
    }
}

export default app.controller('main.controller', MainController);