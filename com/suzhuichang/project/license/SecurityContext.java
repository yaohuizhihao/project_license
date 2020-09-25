package com.suzhuichang.project.license;

import javax.servlet.ServletContext;

public class SecurityContext {
  private static ServletContext webctx;

  public static void bindServletContext(ServletContext ctx) {
    webctx = ctx;
  }

  public static ServletContext getServletContext() {
    return webctx;
  }
}
