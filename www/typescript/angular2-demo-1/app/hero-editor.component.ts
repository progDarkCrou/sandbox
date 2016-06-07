import { Component, OnInit, Input } from 'angular2/core';
import { Hero } from './hero.model';

@Component({
    moduleId: 'app/',
    selector: 'hero-editor',
    templateUrl: 'hero-editor.component.html'
})
export class HeroEditorComponent implements OnInit {

    @Input()
    hero: Hero;

    constructor() { }

    ngOnInit() { 
        console.log('HeroEditorComponent loaded')
    }
}