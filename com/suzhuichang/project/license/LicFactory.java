package com.suzhuichang.project.license;

import com.suzhuichang.project.license.utils.XMLException;

import javax.servlet.ServletContext;

public interface LicFactory {
  void saveToFile(License paramLicense, String paramString) throws XMLException;

  String getWebContextState(ServletContext paramServletContext);

  License fromXML(String paramString);

  void refreshLicense(License paramLicense, ServletContext paramServletContext);
}
