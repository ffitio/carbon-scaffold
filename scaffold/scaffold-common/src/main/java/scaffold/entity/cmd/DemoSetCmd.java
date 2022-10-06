package scaffold.entity.cmd;

import io.ffit.carbon.dto.Command;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Add Demo Params
 *
 * @author Lay
 * @date 2022/9/28
 */
@Data
public class DemoSetCmd extends Command {
    /**
     * identity
     */
    @Min(value = 0)
    private long id;

    /**
     * name
     */
    @NotBlank
    private String name;
}
