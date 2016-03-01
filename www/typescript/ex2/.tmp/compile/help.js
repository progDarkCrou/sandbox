var Help;
(function (Help) {
    function main() {
        console.log('I am main function');
    }
    Help.main = main;
    var Person = (function () {
        function Person(name, surname, age) {
            this.name = name;
            this.surname = surname;
            this.age = age;
        }
        Person.prototype.getFullName = function () {
            return this.name + this.surname;
        };
        Person.prototype.sayHello = function (text) {
            if (text === void 0) { text = 'simple text'; }
            console.log("My name is " + this.getFullName() + ', and I say you ' + (text ? text : ' hello!!!'));
        };
        return Person;
    }());
    Help.Person = Person;
})(Help || (Help = {}));
