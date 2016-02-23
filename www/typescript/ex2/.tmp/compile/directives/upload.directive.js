/**
 * Created by avorona on 09.02.16.
 */
var Upload;
(function (Upload) {
    var app = Index.app;
    app.directive('appUpload', function () {
        return {
            restrict: 'A',
            link: function () {
                console.log('Upload directive linked');
            }
        };
    });
})(Upload || (Upload = {}));
