import {Component} from 'angular2/core';
import {OnInit} from 'angular2/core';
import {NgFor} from 'angular2/common';

@Component({
  selector: 'my-app',
  templateUrl: '/templates/app.component.html',
  directives: [NgFor]
})
export class AppComponent implements OnInit {
  elements:any[];

  ngOnInit():any {
    this.elements = [
      1, 2, 3, 4
    ];
    console.log('Hello from the app.component');
  }
}