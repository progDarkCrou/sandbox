import {Injectable} from "@angular/core";
import {Component} from "@angular/core";
import {Type} from "@angular/core";
import {CustomComponent} from "./CustomComponent";
/**
 * Created by avorona on 19.07.16.
 */

@Injectable()
export class ComponentBuilder {
    public createComponent(template:string):Type {

        @Component({
            template: template
        })
        class CustomComponentClass implements CustomComponent {
            text:string;
        }

        return CustomComponentClass;
    }
}