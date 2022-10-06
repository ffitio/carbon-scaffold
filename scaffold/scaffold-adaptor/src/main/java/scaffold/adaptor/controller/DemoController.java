package scaffold.adaptor.controller;

import io.ffit.carbon.response.MultiResponse;
import io.ffit.carbon.response.SimpleResponse;
import io.ffit.carbon.response.SingleResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import scaffold.entity.cmd.DemoSetCmd;
import scaffold.entity.query.DemoQry;
import scaffold.entity.vo.DemoVO;
import scaffold.service.DemoService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * Demo Controller
 *
 * @author Lay
 * @date 2022/9/28
 */
@RestController
@RequestMapping("demo")
@Validated
public class DemoController {

    @Resource
    DemoService demoService;

    @PutMapping
    public SimpleResponse setDemo(@Valid @RequestBody DemoSetCmd cmd) {
        if (cmd.getId() > 0) {
            // modify
            return demoService.modify(cmd);
        } else {
            // create
            return demoService.create(cmd);
        }
    }

    @GetMapping("{id}")
    public SingleResponse<DemoVO> getDemo(@PathVariable("id") @Min(1) long id) {
        return demoService.get(id);
    }

    @GetMapping("query")
    public MultiResponse<DemoVO> queryDemo(@ModelAttribute DemoQry qry) {
        return demoService.query(qry);
    }

    @DeleteMapping({"{id}"})
    public SimpleResponse delDemo(@PathVariable("id") long id) {
        return demoService.del(id);
    }
}
