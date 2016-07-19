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
    public modalContainer:ViewContainerRef;

    public numberToRemove: number;

    constructor(private componentResolver:ComponentResolver, private componentBuilder:ComponentBuilder) {

    }

    ngOnInit():any {
        this.createComponent();
    }

    createComponent() {
        var customComponent = this.componentBuilder.createComponent('<span>{{number + ": " + text}}</span>');
        this.componentResolver
            .resolveComponent(customComponent)
            .then((factory:ComponentFactory<CustomComponent>)=> {
                var component = this.modalContainer.createComponent(factory);
                var inst = component.instance;
                inst.text = 'hello';
                inst.number = this.modalContainer.length;
            });
    }

    removeAllComponents() {
        this.modalContainer.clear();
    }
}