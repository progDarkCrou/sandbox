package web.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.CheckerManager;
import web.ctrl.vo.CheckerVO;
import web.ctrl.vo.CheckersCollectionAnswer;
import web.vo.Answer;
import web.vo.ErrorAnswer;
import web.vo.InitCheckerVO;
import web.vo.SuccessAnswer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by avorona on 05.10.15.
 */
@RestController
@RequestMapping("checker")
public class MainCtrl {

    @Autowired
    private CheckerManager runner;

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public Answer init(@RequestBody(required = true) InitCheckerVO vo) {
        String res = runner.run(vo.getData(), vo.getUrl(), vo.getReferer(),
                vo.getName(), vo.getEmail());
        return res != null ? new SuccessAnswer(res) : new ErrorAnswer("Cannot create checker. Please try again.");
    }

    @RequestMapping(value = "/stop/{checkerId}", method = RequestMethod.DELETE)
    public Answer stop(@PathVariable String checkerId) {
        return runner.stop(checkerId) ? new SuccessAnswer(Boolean.toString(false)) :
                new ErrorAnswer(Boolean.toString(false));
    }

    @RequestMapping(value = "/info/{checkerId}", method = RequestMethod.GET)
    public Answer info(@PathVariable String checkerId) {
        return runner.stop(checkerId) ? new SuccessAnswer(Boolean.toString(false)) :
                new ErrorAnswer(Boolean.toString(false));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Answer checkers(@RequestParam(value = "from", required = false) Long from,
                           @RequestParam(value = "to", required = false) Long to) {
        List<CheckerVO> result = runner.getCheckers()
                .stream()
                .sorted((o1, o2) -> o1.getId().compareTo(o2.getId()))
                .map(checker -> new CheckerVO(checker))
                .collect(Collectors.toList());
        if (from != null) result = result.subList(from.intValue(), result.size());
        if (to != null) result = result.subList(0, to.intValue());
        return new CheckersCollectionAnswer(result);
    }

}
