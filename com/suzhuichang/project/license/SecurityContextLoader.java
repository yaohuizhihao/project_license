package com.suzhuichang.project.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

public class SecurityContextLoader {
  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextLoader.class);
  private ServletContext servletContext;
  private LicFactory factory;

  public SecurityContextLoader(ServletContext servletContext) {
    this.servletContext = servletContext;
    this.factory = LicFactoryImpl.getInstance();
  }

  public void initContext() {
    String licPath = KEY.LICENSE_PATH;
    License lic = this.factory.fromXML(licPath);
    if (lic == null) {
      this.servletContext.setAttribute(ServerState.WEB_KEY, "limit");
      LOGGER.info("-------------------------------");
      LOGGER.info("未找到有效的证书...");
    } else {
      this.factory.refreshLicense(lic, this.servletContext);
      String state = this.factory.getWebContextState(this.servletContext);
      this.servletContext.setAttribute(ServerState.WEB_KEY, state);
    }
  }

  public ServletContext getServletContext() {
    return this.servletContext;
  }

  public LicFactory getFactory() {
    return this.factory;
  }
}
