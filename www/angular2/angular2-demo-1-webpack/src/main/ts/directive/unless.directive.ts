import {Directive, ViewContainerRef, TemplateRef} from "@angular/core";
/**
 * Created by avorona on 09.06.16.
 */

@Directive({
    selector: '[avUnless]',
    inputs: ['condition: avUnless']
})
export class UnlessDirective {

    constructor(private el:TemplateRef<any>,
                private view:ViewContainerRef) {
    }

    set condition(val:any) {
        if (!!val) {
            this.view.createEmbeddedView(this.el);
        } else if (!val) {
            this.view.clear();
        }
    }
}