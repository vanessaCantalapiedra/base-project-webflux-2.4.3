package com.myarch.webflux.web.config;

import com.fasterxml.classmate.TypeResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.function.Function;
import java.util.function.Predicate;

public class SwaggerConfigHelper {

    private static final String HTTP_WWW_COMPANY = "http://www.mycpompany";

    private SwaggerConfigHelper() {
    }

    public static Docket createDocket(
            Function<SwaggerDockerBuilder, Docket> configuration) {
        return configuration
                .apply(new SwaggerDockerBuilder());
    }

    public static class SwaggerDockerBuilder implements SwaggerDocketStepBuilder.TitleStep
            , SwaggerDocketStepBuilder.DescriptionStep
            , SwaggerDocketStepBuilder.BuildStep, SwaggerDocketStepBuilder.BasePackageStep, SwaggerDocketStepBuilder.GroupNameStep,
            SwaggerDocketStepBuilder.VersionStep {

        private final SwaggerUIData swaggerUIData;

        public SwaggerDockerBuilder() {
            swaggerUIData = new SwaggerUIData();
        }

        @Override
        public SwaggerDocketStepBuilder.DescriptionStep addTitle(String title) {
            swaggerUIData.setTitle(title);
            return this;
        }

        @Override
        public SwaggerDocketStepBuilder.VersionStep addDescription(String description) {
            swaggerUIData.setDescription(description);
            return this;
        }

        @Override
        public SwaggerDocketStepBuilder.GroupNameStep addVersion(String version) {
            swaggerUIData.setVersion(version);
            return this;
        }

        @Override
        public SwaggerDocketStepBuilder.BasePackageStep addGroupName(String groupName) {
            swaggerUIData.setGroupName(groupName);
            return this;
        }

        @Override
        public SwaggerDocketStepBuilder.BuildStep addBasePackage(String basePackage) {
            return addBasePackage(basePackage, PathSelectors.any());
        }

        @Override
        public SwaggerDocketStepBuilder.BuildStep addBasePackage(String basePackage, Predicate<String> pathSelector) {
            swaggerUIData.setPathSelectors(pathSelector);
            swaggerUIData.setBasePackage(basePackage);
            return this;
        }

        @Override
        public Docket build() {
            return new Docket(DocumentationType.SWAGGER_2).enable(true)
                    .groupName(swaggerUIData.getGroupName())
                    .useDefaultResponseMessages(false)
                    .apiInfo(apiInfo())
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(swaggerUIData.getBasePackage()))
                    .paths(swaggerUIData.getPathSelectors())
                    .build();
        }

        //to be removed when fixed
        // workaround: https://github.com/springfox/springfox/issues/3646
        // currently there is no solution, the error types are not resolved for swagger 3.0
        @Override
        public Docket buildWithCustomErrorResponse(Class<?> errorClass) {
            return this.build()
                    .additionalModels(new TypeResolver().resolve(errorClass));
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title(swaggerUIData.getTitle())
                    .description(swaggerUIData.getDescription())
                    .termsOfServiceUrl(HTTP_WWW_COMPANY)
                    .contact(new Contact("MY_COMPANY_NAME", HTTP_WWW_COMPANY, "MY_COMPANY_EMAIL"))
                    .licenseUrl(HTTP_WWW_COMPANY)
                    .version(swaggerUIData.getVersion())
                    .build();
        }
    }
}
