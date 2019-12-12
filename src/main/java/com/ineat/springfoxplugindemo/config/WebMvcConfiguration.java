package com.ineat.springfoxplugindemo.config;

import com.fasterxml.classmate.TypeResolver;
import com.ineat.springfoxplugindemo.SpringfoxPluginDemoApplication;
import com.ineat.springfoxplugindemo.dto.ErrorDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    /**
     * Create the global configuration for Springfox.
     */
    @Bean
    public Docket apiDocket(TypeResolver tr) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metadata())
                // Do not automatically add 401, 403 and 404 responses to every operation
                .useDefaultResponseMessages(false)
                // Since this isn't explicitly mentioned in any Controller, we have to add it manually
                .additionalModels(tr.resolve(ErrorDTO.class))
                .select()
                    .apis(RequestHandlerSelectors.basePackage(SpringfoxPluginDemoApplication.class.getPackage().getName()))
                    .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Frisbee throw calculator")
                .version("0.0.1-SNAPSHOT")
                .contact(new Contact("Ineat", "https://ineat-group.com/", "contact@ineat.fr"))
                .build();
    }

    /**
     * This is necessary to ensure that the swagger UI resources are available.
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
