package com.xcore.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RoutePathException extends Exception {
  public RoutePathException(String message) {
    super(message);
  }
}
