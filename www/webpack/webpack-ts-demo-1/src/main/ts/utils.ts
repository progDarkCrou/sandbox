/**
 * Created by avorona on 06.06.16.
 */
    
export class HelloSayer {
    private _helloText: string;

    constructor(helloText:string) {
        this._helloText = helloText;
    }

    public sayHello() {
        console.log(`Hello ${this._helloText}`);
    }
}