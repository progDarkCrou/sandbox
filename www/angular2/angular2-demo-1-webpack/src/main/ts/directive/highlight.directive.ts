import {Directive, Input, ElementRef, OnInit, OnDestroy} from "@angular/core";
/**
 * Created by avorona on 08.06.16.
 */


@Directive({
    selector: '[avHighlight]',
    host: {
        '(mouseenter)': 'switch()',
        '(mouseleave)': 'switch()'
    }
})
export class HighlightDirective implements  OnInit, OnDestroy {

    @Input('avHighlight')
    private defaultColor: string;

    private isColorized: boolean = false;

    constructor(private _el: ElementRef){
    };

    public switch() {
        if (this.defaultColor) {
            if (this.isColorized) {
                this._el.nativeElement.style.backgroundColor = null;
                this.isColorized = false;
            } else {
                this._el.nativeElement.style.backgroundColor = this.defaultColor;
                this.isColorized = true;
            }
        }
    }

    ngOnInit():any {
        console.log('Highlight directive initialized');
    }


    ngOnDestroy():any {
        console.log('Highlight directive destroying');
    }
}