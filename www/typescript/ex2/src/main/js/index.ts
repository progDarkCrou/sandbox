///<reference path="../../../typings/angularjs/angular.d.ts"/>

namespace Index {
    import Person = Help.Person;

    console.log('Index module loaded');

    var person:Person  = new Person('Stiven', 'Pitsburg', 100);

    interface Scope extends angular.IScope {
    }

    export var app = angular.module('app', []);

    interface IMainControllerScope extends angular.IScope {
        person: Person;
        sayHello(): string;
        sayHello(firstArg?:string): string;
    }

    app.controller('controller', ['$scope', function ($scope:IMainControllerScope) {
        $scope.person.sayHello();
    }]);

    console.log(person.sayHello());
}
