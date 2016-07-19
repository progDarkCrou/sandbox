import {Component} from "@angular/core";
import {TemplateRef} from "@angular/core";
import {ViewContainerRef} from "@angular/core";
import {ViewChild} from "@angular/core";
import {OnInit} from "@angular/core";
import {ComponentResolver} from "@angular/core";
import {ComponentBuilder} from "../commons/ComponentBuilder";
import {ComponentFactory} from "@angular/core";
import {CustomComponent} from "../commons/CustomComponent";
import {ComponentRef} from "@angular/core";
/**
 * Created by avorona on 19.07.16.
 */


@Component({
    template: require('./main.component.template.html'),
    selector: 'app-main',
    providers: [ComponentBuilder]
})
export class MainComponent implements OnInit {

    @ViewChild('modalContainer', {read: ViewContainerRef})
    private modalContainer:ViewContainerRef;

    constructor(private componentResolver:ComponentResolver, private componentBuilder:ComponentBuilder) {

    }

    ngOnInit():any {
        this.createComponent();
    }

    createComponent() {
        var customComponent = this.componentBuilder.createComponent('hello from the custom built component');
        this.componentResolver
            .resolveComponent(customComponent)
            .then((factory:ComponentFactory<CustomComponent>)=> {
                var component = this.modalContainer.createComponent(factory);
                component.instance.text = 'hello';
            });
    }

    removeAllComponents() {
        this.modalContainer.clear();
    }
}