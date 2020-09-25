package com.suzhuichang.project.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Component
public class SecurityContextListener implements ServletContextListener {

  private SecurityContextLoader contextLoader;
  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextListener.class);

  @Override
  public void contextDestroyed(ServletContextEvent evnt) {
    System.out.println("2====================================");
    LOGGER.info("安全框架正常退出...");
  }

  @Override
  public void contextInitialized(ServletContextEvent evnt) {
    System.out.println("1====================================");
    LOGGER.info("安全框架初始化...");
    this.contextLoader = createContextLoader(evnt.getServletContext());
    this.contextLoader.initContext();
    SecurityContext.bindServletContext(evnt.getServletContext());
  }

  private SecurityContextLoader createContextLoader(ServletContext ctx) {
    return new SecurityContextLoader(ctx);
  }
}
