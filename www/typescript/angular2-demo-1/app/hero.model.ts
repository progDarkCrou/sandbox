

export class Hero {
    private _name: string;

    constructor(name: string) {
        this._name = name;
    }

    getName() {
        return this._name;
    }

    setName(name: string) {
        this._name = name;
    }
}