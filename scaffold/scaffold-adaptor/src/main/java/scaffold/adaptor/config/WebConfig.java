package scaffold.adaptor.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import io.ffit.carbon.context.handler.CarbonContextInterceptor;
import io.ffit.carbon.i18n4j.springboot.context.I18nSourceHolder;
import io.ffit.carbon.response.HttpError;
import io.ffit.carbon.response.Response;
import io.ffit.carbon.response.SimpleResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Web Config
 *
 * @author Lay
 * @date 2022/9/28
 */
@Configuration
@ControllerAdvice
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
        return new MessageSource() {
            @Override
            public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
                return I18nSourceHolder.get().getMessage(code, args, defaultMessage, locale);
            }

            @Override
            public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
                String msg = I18nSourceHolder.get().getMessage(code, args, locale);
                if (msg == null) {
                    throw new NoSuchMessageException(code, locale);
                }
                return msg;
            }

            @Override
            public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
                String[] codes = resolvable.getCodes();
                if (codes != null) {
                    for (String code : codes) {
                        String message = I18nSourceHolder.get().getMessage(code, resolvable.getArguments(), locale);
                        if (message != null) {
                            return message;
                        }
                    }
                }
                if (resolvable.getDefaultMessage() != null) {
                    return resolvable.getDefaultMessage();
                }
                throw new NoSuchMessageException(!ObjectUtils.isEmpty(codes) ? codes[codes.length - 1] : "", locale);
            }
        };
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        bean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        return bean;
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Throwable ex = super.getError(webRequest);
                Map<String, Object> attrs = super.getErrorAttributes(webRequest, options);
                int status = (int) attrs.get("status");
                HttpError error = HttpError.of(status);
                if (error != null) {
                    Response response = SimpleResponse.error(error);
                    response.setErrMessage(getMessage(webRequest, ex));
                    return  (JSONObject) JSON.toJSON(response);
                }
                return attrs;
            }

            @Override
            protected String getMessage(WebRequest webRequest, Throwable error) {
                if (error instanceof MethodArgumentNotValidException ex) {
                    FieldError err = (FieldError) ex.getAllErrors().get(0);
                    return String.format("[%s] %s", err.getField(), err.getDefaultMessage());
                } else if (error instanceof ConstraintViolationException ex) {
                    ConstraintViolation<?> cv = new ArrayList<>(ex.getConstraintViolations()).get(0);
                    String path = cv.getPropertyPath().toString();
                    int lastDotIndex = path.lastIndexOf('.');
                    if (lastDotIndex > -1) {
                        path = path.substring(lastDotIndex + 1);
                    }
                    return String.format("[%s] %s", path, cv.getMessage());

                }
                return super.getMessage(webRequest, error);
            }
        };
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException e, ServletWebRequest webRequest) throws IOException {
        webRequest.getResponse().sendError(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // FastJson Config
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // writer features
        config.setWriterFeatures(
                // show null fields
                // JSONWriter.Feature.WriteNulls,
                // write default value replace of null
                // Number: null to 0
                // String: null to ""
                // Boolean: null to false
                // Array: null to []
                // JSONWriter.Feature.NullAsDefaultValue,
                JSONWriter.Feature.NotWriteDefaultValue,
                JSONWriter.Feature.WriteNullListAsEmpty,
                JSONWriter.Feature.WriteNullNumberAsZero,
                JSONWriter.Feature.WriteNullBooleanAsFalse,
                // write BigDecimal to Plain
                JSONWriter.Feature.WriteBigDecimalAsPlain,
                // avoid javascript integer out of range
                JSONWriter.Feature.BrowserCompatible
        );

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.add(0, converter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        CarbonContextInterceptor interceptor = new CarbonContextInterceptor();
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}
