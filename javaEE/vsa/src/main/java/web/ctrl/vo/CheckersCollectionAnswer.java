package web.ctrl.vo;

import web.vo.Answer;

import java.util.List;

/**
 * Created by avorona on 14.10.15.
 */
public class CheckersCollectionAnswer extends Answer {

    private Integer count;
    private List<CheckerVO> checkers;

    public CheckersCollectionAnswer(List<CheckerVO> result) {
        this.count = result.size();
        this.checkers = result;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CheckerVO> getCheckers() {
        return checkers;
    }

    public void setCheckers(List<CheckerVO> checkers) {
        this.checkers = checkers;
    }
}
