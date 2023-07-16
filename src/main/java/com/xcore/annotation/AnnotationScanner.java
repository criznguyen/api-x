package com.xcore.annotation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnnotationScanner {

  public static List<Class<?>> scanClassesWithAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
    List<Class<?>> classesWithAnnotation = new ArrayList<>();

    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      for (Class<?> clazz : getClasses(packageName, classLoader)) {
        if (clazz.isAnnotationPresent(annotationClass)) {
          classesWithAnnotation.add(clazz);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return classesWithAnnotation;
  }

  private static List<Class<?>> getClasses(String packageName, ClassLoader classLoader) throws Exception {
    List<Class<?>> classes = new ArrayList<>();

    String path = packageName.replace('.', '/');
    java.net.URL resource = classLoader.getResource(path);
    if (resource != null) {
      java.io.File directory = new java.io.File(resource.getFile());
      if (directory.exists() && directory.isDirectory()) {
        for (String fileName : Objects.requireNonNull(directory.list())) {
          if (fileName.endsWith(".class")) {
            String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
            Class<?> clazz = Class.forName(className);
            classes.add(clazz);
          }
        }
      }
    }

    return classes;
  }
}
