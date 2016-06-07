///<reference path="../../../typings/angularjs/angular.d.ts"/>
var Index;
(function (Index) {
    var Person = Help.Person;
    console.log('Index module loaded');
    var person = new Person('Stiven', 'Pitsburg', 100);
    Index.app = angular.module('app', []);
    Index.app.controller('controller', ['$scope', function ($scope) {
            $scope.person.sayHello();
        }]);
    console.log(person.sayHello());
})(Index || (Index = {}));
