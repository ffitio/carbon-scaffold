package scaffold.adaptor.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

/**
 * Swagger Config
 *
 * @author Lay
 * @date 2022/10/18
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Resource
    BuildProperties buildProperties;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public Docket docketOpen() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .groupName("Open API")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.ant(contextPath + "/open/**"))
                .build();
    }

    @Bean
    public Docket docketManage() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .groupName("Manage API")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.ant(contextPath + "/manage/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(buildProperties.getName())
                .version(buildProperties.getVersion())
                .build();
    }
}
