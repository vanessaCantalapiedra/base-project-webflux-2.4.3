package com.myarch.webflux.rest.annotations;

import com.myarch.webflux.rest.config.WebclientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({WebclientConfig.class})
public @interface EnableWebClientSupport {
}
