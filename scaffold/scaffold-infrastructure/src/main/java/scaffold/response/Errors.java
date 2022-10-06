package scaffold.response;

import io.ffit.carbon.i18n4j.springboot.context.I18nSourceHolder;
import io.ffit.carbon.response.ResponseError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Response Errors
 *
 * @author Lay
 * @date 2022/9/28
 */
@Getter
@AllArgsConstructor
public enum Errors implements ResponseError {
    /**
     * Demo Not Found
     */
    DemoNotFound("scaffold.demo.notFound"),
    ;

    private final String code;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return I18nSourceHolder.get().getMessage(code, LocaleContextHolder.getLocale());
    }
}
