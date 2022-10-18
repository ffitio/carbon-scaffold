package scaffold.entity.query;

import io.ffit.carbon.dto.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Demo Query Params
 *
 * @author Lay
 * @date 2022/9/28
 */
@ApiModel("Demo Query Params")
@Data
public class DemoQry extends Query {
    /**
     * name
     */
    @ApiModelProperty("Demo Name")
    private String name;
}
