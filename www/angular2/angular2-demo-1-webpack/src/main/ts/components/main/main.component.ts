/**
 * Created by avorona on 08.06.16.
 */
import {Component} from "@angular/core";
import {HighlightDirective} from "../../directive/highlight.directive";
import {FORM_DIRECTIVES} from "@angular/common";
import {UnlessDirective} from "../../directive/unless.directive";

@Component({
    selector: 'av-main-component',
    template: require('./main-component.template.html'),
    directives: [FORM_DIRECTIVES, [HighlightDirective], [UnlessDirective]]
})
export class MainComponent {

    public color = 'lightblue';

    constructor() {
    }
}