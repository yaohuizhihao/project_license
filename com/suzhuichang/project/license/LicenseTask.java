package com.suzhuichang.project.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

@Component("licenseTask")
public class LicenseTask {
  private static final Logger log = LoggerFactory.getLogger(LicenseTask.class);

  public void checkLincense() {
    log.debug("开始校验认证信息...");

    ServletContext servletContext = SecurityContext.getServletContext();

    String state = LicFactoryImpl.getInstance().getWebContextState(servletContext);
    servletContext.setAttribute(ServerState.WEB_KEY, state);
    if (state.equals("limit")) {
      log.info("------------------------------------");
      log.info("由于以上原因,服务器将停止服务...");
    }
  }
}
