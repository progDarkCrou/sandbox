/**
 * Created by avorona on 09.02.16.
 */
module Upload {
    import IDirective = angular.IDirective;
    import app = Index.app;

    app.directive('appUpload', function (): IDirective {
       return {
           restrict: 'A',
           link: function () {
               console.log('Upload directive linked');
           }
       };
    });
}