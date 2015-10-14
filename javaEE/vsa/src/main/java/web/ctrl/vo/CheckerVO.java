package web.ctrl.vo;

import model.Checker;

/**
 * Created by avorona on 14.10.15.
 */
public class CheckerVO {

    private Long id;
    private String name;

    public CheckerVO(Checker checker) {
        this.id = checker.getId();
        this.name = checker.getName();

    }
}
