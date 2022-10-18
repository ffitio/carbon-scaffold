package scaffold.entity.cmd;

import io.ffit.carbon.dto.Command;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Add Demo Params
 *
 * @author Lay
 * @date 2022/9/28
 */
@ApiModel("Demo")
@Data
public class DemoSetCmd extends Command {
    /**
     * identity
     */
    @ApiModelProperty("Demo Identity")
    @Min(value = 0)
    private long id;

    /**
     * name
     */
    @ApiModelProperty("Demo Name")
    @NotBlank
    private String name;
}
