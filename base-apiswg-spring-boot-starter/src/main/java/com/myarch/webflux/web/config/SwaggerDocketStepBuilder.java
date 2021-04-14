package com.myarch.webflux.web.config;

import springfox.documentation.spring.web.plugins.Docket;

import java.util.function.Predicate;

public class SwaggerDocketStepBuilder {
    /**
     * Next Step available : DescriptionStep
     */
    public interface TitleStep {

        DescriptionStep addTitle(String title);
    }

    /**
     * Next Step available : VersionStep
     */
    public interface DescriptionStep {

        VersionStep addDescription(String description);
    }

    /**
     * Next Step available : GroupNameStep
     */
    public interface VersionStep {

        GroupNameStep addVersion(String version);
    }

    /**
     * Next Step available : BasePackageStep
     */
    public interface GroupNameStep {

        BasePackageStep addGroupName(String groupName);
    }

    /**
     * Next Step available : Build or addPathSelectors
     */
    public interface BasePackageStep {

        BuildStep addBasePackage(String basePackage, Predicate<String> pathSelector);
        BuildStep addBasePackage(String basePackage);
    }

    public interface BuildStep {
        Docket build();
        //TODO temporaryFix
        Docket buildWithCustomErrorResponse(Class<?> errorClass);
    }
}
