package scaffold.adaptor.manage;

import io.ffit.carbon.response.SimpleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import scaffold.entity.cmd.DemoSetCmd;
import scaffold.service.DemoService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Manage Controller
 *
 * @author Lay
 * @date 2022/10/18
 */
@Api(tags = "Demo APIs")
@RestController
@RequestMapping("/manage/demo")
public class ManageDemoController {

    @Resource
    DemoService demoService;

    @ApiOperation("Create or Modify Demo")
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

    @ApiOperation("Delete Demo")
    @DeleteMapping({"{id}"})
    public SimpleResponse delDemo(@ApiParam("Demo Identity") @PathVariable("id") long id) {
        return demoService.del(id);
    }
}
