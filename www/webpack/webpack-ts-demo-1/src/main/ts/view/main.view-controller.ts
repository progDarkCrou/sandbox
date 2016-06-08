import ILogService = angular.ILogService;
import {WindowManagerService, WindowDecision} from "../service/window-manager.service";
/**
 * Created by avorona on 07.06.16.
 */

export class MainViewController {

    public static _name = 'MainViewController';
    public static $inject = ['$log', WindowManagerService._name];

    constructor(private $log: ILogService, private window: WindowManagerService) {
        this.init();
    }

    private init() {
        this.$log.debug(`${MainViewController._name} initialized`);
    }
    
    public open() {
        this.window.open({
            template: <string>require('./window.template.html'),
            controllerAs: 'vm',
            backgroundCanClose: true,
            controller: ['_window', function (modal: WindowDecision) {
                this.close = ()=> {
                  modal.exit(null);
                };
            }]
        })
    }
}