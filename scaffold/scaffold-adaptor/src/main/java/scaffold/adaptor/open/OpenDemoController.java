package scaffold.adaptor.open;

import io.ffit.carbon.response.MultiResponse;
import io.ffit.carbon.response.SingleResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import scaffold.entity.query.DemoQry;
import scaffold.entity.vo.DemoVO;
import scaffold.service.DemoService;

import javax.annotation.Resource;
import javax.validation.constraints.Min;

/**
 * Open Controller
 *
 * @author Lay
 * @date 2022/10/18
 */
@Api(tags = "Demo APIs")
@RestController
@RequestMapping("/open/demo")
public class OpenDemoController {
    @Resource
    DemoService demoService;

    @ApiOperation("Get Demo")
    @GetMapping("{id}")
    public SingleResponse<DemoVO> getDemo(@ApiParam("Demo Identity")
                                          @PathVariable("id")
                                          @Min(1) long id) {
        return demoService.get(id);
    }

    @ApiOperation("Query Demos")
    @GetMapping("query")
    public MultiResponse<DemoVO> queryDemo(@ModelAttribute DemoQry qry) {
        return demoService.query(qry);
    }
}
