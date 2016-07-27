import IAugmentedJQuery = angular.IAugmentedJQuery;
import IControllerService = angular.IControllerService;
import ICompileService = angular.ICompileService;
import IQService = angular.IQService;
import IRootScopeService = angular.IRootScopeService;
import IHttpService = angular.IHttpService;
import IScope = angular.IScope;
/**
 * Created by avorona on 07.06.16.
 */

interface WindowCloseListener {
  onWindowClose(window:Window):any;
}

class Window {
  private _windowElem:JQuery;
  private _scope:IScope;
  private _config:WindowConfig;
  private _visible:boolean;

  private _closeListeners:WindowCloseListener[] = [];

  constructor(config:WindowConfig, private _windowMngr:WindowManagerService) {
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
    this._windowMngr.close(this);
  }

  get visible() {
    return this._visible;
  }
}

interface WindowConfig {
  templateUrl?:string,
  template?:string,
  controller:string | Function | [string | Function],
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

  constructor(private _compile:ICompileService,
      private _controller:IControllerService,
      private _q:IQService,
      private _rootScope:IRootScopeService,
      private _http:IHttpService) {
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
    let window:Window = new Window(config, this);

    let scope = this._rootScope.$new();
    window.scope = scope;

    let windowElem = this._templateLoaded(config.template)(scope);
    let windowWrapper = this._wrapp(windowElem);
    window.windowElem = windowElem;

    let locals = angular.extend({}, config.resolve || {});
    locals['$scope'] = scope;
    locals['_window'] = new WindowDecision(defer, window);

    let configControllerType = $.type(config.controller);

    switch (configControllerType) {
      case 'array': {
        let localsWithController = <[string | Function]>config.controller;

        let controllerFn = <Function>localsWithController[localsWithController.length - 1];
        controllerFn.$inject = <string[]>localsWithController.slice(0, -1);

        let ctrl = this._controller(controllerFn, locals);
        if (angular.isString(config.controllerAs)) {
          scope[config.controllerAs.trim()] = ctrl;
        }
        break;
      }
      case 'function': {
        let controller = this._controller(<Function>config.controller, locals);
        if (angular.isString(config.controllerAs)) {
          scope[config.controllerAs.trim()] = controller;
        }
        break;
      }
      case 'string': {
        let controllerAs = config.controllerAs;
        let controllerName:string = <string>config.controller;
        let controller = `${controllerName.trim()} as `;
        if (angular.isString(config.controllerAs)) {
          controller = `${controllerName.trim()} as ${controllerAs}`;
        }
        this._controller(controller, locals);
      }
    }

    if (config.backgroundCanClose) {
      windowWrapper.on('click', (e)=> {
        if (windowWrapper.is(e.target)) {
          window.close()
        }
      });
    }

    let lastWindow = this._windowStack.length > 0? this._windowStack[this._windowStack.length - 1]: null;
    if (lastWindow && lastWindow.config.hideOnNew) {
      this._wrapperStack[this._wrapperStack.length - 1].hide();
    }

    if (config.hidePrevious && lastWindow) {
      this._wrapperStack[this._wrapperStack.length - 1].hide();
    }

    windowWrapper.show();
    windowElem.show();
    this._windowHolder.show();

    this._windowStack.push(window);
    this._wrapperStack.push(windowWrapper);

    return new WindowResult(defer.promise, window);
  }

  private _wrapp(elem:JQuery) {
    let wrapper = this._windowWrapperPrototype.clone();
    wrapper.hide();
    let wrapperZIndex = this._defaultZIndex + this._windowStack.length * 2;
    wrapper.css('z-index', wrapperZIndex);
    elem.css('z-index', wrapperZIndex + 1);
    wrapper.append(elem);
    this._windowHolder.append(wrapper);
    return wrapper;
  }

  public onWindowClose(window:Window) {
    this.close(window);
  }

  public close(window:Window) {
    let windowIndex = this._windowStack.indexOf(window);
    if (~windowIndex) {
      let prevWindow = windowIndex > 0? this._wrapperStack[windowIndex - 1]: null;

      this._windowStack.splice(windowIndex, 1);

      let wrapper = this._wrapperStack[windowIndex];
      wrapper.remove();
      this._wrapperStack.splice(windowIndex, 1);

      if (prevWindow) {
        prevWindow.show();
      }

      if (windowIndex === 0) {
        this._windowHolder.hide();
      }
    }
  }
}