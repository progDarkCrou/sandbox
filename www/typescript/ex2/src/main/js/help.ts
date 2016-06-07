module Help {
    export function main() {
        console.log('I am main function');
    }

    export class Person {
        constructor(private name: string, private surname: string, private age?: number) {
        }

        getFullName(): string {
            return this.name + this.surname;
        }

        public sayHello(text: string = 'simple text') {
            console.log("My name is " + this.getFullName() + ', and I say you ' + (text? text: ' hello!!!') );
        }
    }
}
