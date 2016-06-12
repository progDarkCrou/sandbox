define(["require", "exports", "./help", "angular"], function (require, exports, help_1, angular) {
    var Index;
    (function (Index) {
        console.log('Index module loaded');
        var person = new help_1.Person('Stiven', 'Pitsburg', 100);
        Index.app = angular.module('app', []);
        Index.app.controller('controller', ['$scope', function ($scope) {
                $scope.person.sayHello();
            }]);
        console.log(person.sayHello());
    })(Index = exports.Index || (exports.Index = {}));
});
