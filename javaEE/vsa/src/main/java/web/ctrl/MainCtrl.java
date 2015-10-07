package web.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.CheckManager;
import web.vo.Answer;
import web.vo.ErrorAnswer;
import web.vo.InitCheckerVO;
import web.vo.SuccessAnswer;

/**
 * Created by avorona on 05.10.15.
 */
@RestController
@RequestMapping("checker")
public class MainCtrl {

    @Autowired
    private CheckManager runner;

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public Answer init(@RequestBody(required = true) InitCheckerVO vo) {
        String res = runner.run(vo.getUrl(), vo.getReferer(), vo.getData());
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

}
