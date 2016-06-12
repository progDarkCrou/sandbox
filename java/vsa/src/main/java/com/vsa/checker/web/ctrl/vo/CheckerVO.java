package com.vsa.checker.web.ctrl.vo;

import com.vsa.checker.model.Checker;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by avorona on 14.10.15.
 */
public class CheckerVO {

    private final List<CheckerResultVO> checkerResults;
    private Long id;
    private String name;

    public CheckerVO(Checker checker) {
        this.id = checker.getId();
        this.name = checker.getName();
        this.checkerResults = checker.getResults().stream()
                .map(CheckerResultVO::new)
                .collect(Collectors.toList());
    }

    public List<CheckerResultVO> getCheckerResults() {
        return checkerResults;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
