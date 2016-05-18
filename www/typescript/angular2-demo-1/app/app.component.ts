import {Component} from 'angular2/core';
import {Hero} from './hero.model';
import {HeroEditorComponent} from './hero-editor.component';

@Component({
    moduleId: 'app/',
    selector: 'my-app',
    templateUrl: 'app.component.html',
    directives: [HeroEditorComponent]
})
export class AppComponent {
    hero: Hero = new Hero('Robert');
}
