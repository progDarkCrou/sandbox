import {Component} from "angular2/core";
import {OnInit} from "angular2/core";
import {NgFor} from "angular2/common";

@Component({
  selector: "my-app",
  templateUrl: "/templates/app.component.html",
  directives: [NgFor]
})
export class AppComponent implements OnInit {

  newHero: Hero = {};

  selectedHero: Hero;

  heroes:Hero[];

  addNewHero() {
    if (this.newHero.name && this.newHero.surname) {
      this.heroes.push(this.newHero);
      this.newHero = {};
    }
  }

  selectHero(hero: Hero) {
    this.selectedHero = hero;
  }

  ngOnInit():any {
    console.log("Hello from the app.component");
    this._initHeroes();
  }

  private _initHeroes() {
    this.heroes = [
      {
        name: "Magenta",
        surname: "Clara"
      }
    ];
  }
}

interface Hero {
  name?: string;
  surname?: string;
  age?: number;
}