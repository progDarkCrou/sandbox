/**
 * Created by avorona on 06.06.16.
 */

declare var global:{
  angular:any,
  $:any,
  jQuery:any,
};

declare var require:{
  <T>(path:string):T;
  (paths:string[], callback:(...modules:any[]) => void):void;
  ensure:(paths:string[], callback:(require:<T>(path:string) => T) => void) => void;
};