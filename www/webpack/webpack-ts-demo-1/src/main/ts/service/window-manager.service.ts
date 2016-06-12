import IAugmentedJQuery = angular.IAugmentedJQuery;
/**
 * Created by avorona on 07.06.16.
 */

interface WindowCloseListener {
    onWindowClose(window:Window):any;
}

class Window {
    private _windowElem:any;
    private _scope:any;
    private _config:WindowConfig;
    private _visible:boolean;

    private _closeListeners:WindowCloseListener[] = [];

    constructor(config:WindowConfig) {
        this._config = config;
    }

    addCloseListener(listener:WindowCloseListener) {
        this._closeListeners.push(listener);
    }

    set scope(scope:any) {
        this._scope = scope;
    }

    set windowElem(element:any) {
        this._windowElem = element;
    }

    set config(config:WindowConfig) {
        this._config = config;
    }

    get config() {
        return this._config;
    }

    close() {
        this._scope.$destroy();
        this._windowElem.remove();
        this._closeListeners.forEach((l)=> l.onWindowClose(this));
    }

    get visible() {
        return this._visible;
    }
}

interface WindowConfig {
    templateUrl?:string,
    template?:string,
    controller:string | Function | (string | Function)[],
    controllerAs?:string,
    resolve?:any,
    backgroundCanClose?:boolean,
    hideOnNew?:boolean,
    hidePrevious?:boolean
}

class WindowResult {
    constructor(public resultPromise:any, private _window:any) {
    }

    public close() {
        this._window.close();
    }
}

export class WindowDecision {
    constructor(private _defer:any, private _window:Window) {
    }

    public exit(result:any) {
        this._window.close();
        this._defer.resolve(result);
    }

    public close(reason:any) {
        this._window.close();
        this._defer.reject(reason);
    }
}

export class WindowManagerService implements WindowCloseListener {

    public static _name = 'WindowManagerService';

    private _windowWrapperPrototype:JQuery;

    private _windowHolder:JQuery;

    private _defaultZIndex = 1000;

    private _windowStack:Window[] = [];
    private _wrapperStack:JQuery[] = [];

    //noinspection JSUnusedGlobalSymbols
    public static $inject = ['$compile', '$controller', '$q', '$rootScope', '$http'];

    constructor(private _compile:any,
                private _controller:any,
                private _q:any,
                private _rootScope:any,
                private _http:any) {
        this._windowHolder = $('<div></div>');
        this._windowHolder.hide()
            .addClass('app-window-holder')
            .css({
                position: 'fixed',
                top: '0',
                left: '0',
                right: '0',
                wigth: '0',
                bottom: '0',
                height: '100%',
                'z-index': this._defaultZIndex
            });

        this._windowWrapperPrototype = this._windowHolder.clone().css('position', 'abstract');
        angular.element(document.body).append(this._windowHolder);
    }

    private _templateLoaded(template:string) {
        return this._compile(template);
    }

    public open(config:WindowConfig) {
        if (!config.template && !config.templateUrl) {
            throw new Error('cannot open window without template. please provide .template or .templateUrl');
        }

        let defer = this._q.defer();
        let window:Window = new Window(config);

        this._windowStack.push(window);

        let scope = this._rootScope.$new();
        window.scope = scope;

        let windowElem = this._templateLoaded(config.template)(scope);
        let wrapper = this._wrapp(windowElem);
        window.windowElem = windowElem;

        let locals = angular.extend({}, config.resolve || {});
        locals['$scope'] = scope;
        locals['_window'] = new WindowDecision(defer, window);

        let configControllerType = $.type(config.controller);

        switch (configControllerType) {
            case 'array':
            {
                let localsWithController = <(string | Function)[]>config.controller;

                let controllerFn = <Function>localsWithController[localsWithController.length - 1];
                controllerFn.$inject = <string[]>localsWithController.slice(0, -1);

                let ctrl = this._controller(controllerFn, locals);
                if (angular.isString(config.controllerAs)) {
                    scope[config.controllerAs.trim()] = ctrl;
                }
                break;
            }
            case 'function':
            {
                let controller = this._controller(config.controller, locals);
                if (angular.isString(config.controllerAs)) {
                    scope[config.controllerAs.trim()] = controller;
                }
                break;
            }
            case 'string':
            {
                let controllerAs = config.controllerAs;
                let controllerName:string = <string>config.controller;
                let controller = `${controllerName.trim()} as `;
                if (angular.isString(config.controllerAs)) {
                    controller = `${controllerName.trim()} as ${controllerAs}`;
                }
                this._controller(controller, locals);
            }
        }

        window.addCloseListener(this);
        wrapper.show();
        if (config.backgroundCanClose) {
            wrapper.on('click', (e)=> {
                if (wrapper.is(e.target)) {
                    window.close()
                }
            });
        }
        windowElem.show();
        this._windowHolder.show();

        return new WindowResult(defer.promise, window);
    }

    private _wrapp(elem:JQuery) {
        let wrapper = this._windowWrapperPrototype.clone();
        wrapper.hide();
        let wrapperZIndex = this._defaultZIndex + this._windowStack.length * 2;
        wrapper.css('z-index', wrapperZIndex);
        elem.css('z-index', wrapperZIndex + 1);
        wrapper.append(elem);
        this._wrapperStack.unshift(wrapper);
        this._windowHolder.append(wrapper);
        return wrapper;
    }

    public onWindowClose(window:Window) {
        let windowIndex = this._windowStack.indexOf(window);
        if (~windowIndex) {
            this._windowStack.splice(windowIndex, 1);

            let wrapper = this._wrapperStack[windowIndex];
            wrapper.remove();
            this._wrapperStack.splice(windowIndex, 1);

            if (windowIndex === 0) {
                this._windowHolder.hide();
            }
        }
    }

    private _currentZIndex() {
        return this._defaultZIndex + 1 + this._windowStack.length;
    }
}