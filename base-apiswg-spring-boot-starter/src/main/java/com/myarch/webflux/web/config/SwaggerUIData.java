package com.myarch.webflux.web.config;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class SwaggerUIData {
    private String title;
    private String description;
    private String version;
    private String groupName;
    private String basePackage;
    private Predicate<String> pathSelectors;
}
