package com.xcore.annotation;

import com.xcore.constants.XHttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.xcore.constants.XHttpMethod.GET;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface XPath {
  String value();

  XHttpMethod method() default GET;
}
