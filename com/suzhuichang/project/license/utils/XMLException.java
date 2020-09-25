package com.suzhuichang.project.license.utils;

public final class XMLException extends Exception {
  private static final long serialVersionUID = 1L;

  public XMLException() {
    super("XML 处理异常");
  }

  public XMLException(String strMsg) {
    super(strMsg);
  }

  public XMLException(Throwable cause) {
    super(cause);
  }
}
