package com.suzhuichang.project.license;

import java.text.SimpleDateFormat;
import java.util.Date;

public class License {
  public static final String WEB_KEY = License.class.getName();

  private String product;

  private String company;

  private String version;

  private String expiresDate;

  private String signature;

  public String getOriginalAuth() {
    StringBuilder sb = new StringBuilder();
    sb.append(getCompany());
    sb.append(";");
    sb.append(getVersion());
    sb.append(";");
    sb.append(getExpiresDate());
    sb.append(";");
    sb.append(getProduct());
    return sb.toString();
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("产品信息：" + getProduct() + "\n");
    sb.append("公司名称：" + getCompany() + "\n");
    sb.append("软件版本：" + getVersion() + "\n");
    sb.append("有效期至：" + getExpiresDate() + "\n");
    return sb.toString();
  }

  public long getExpireDays() {
    long now = (new Date()).getTime();
    Date d = null;
    try {
      SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      d = e.parse(getExpiresDate());
      return (d.getTime() - now);
    } catch (Exception arg4) {
      arg4.printStackTrace();
      return -1L;
    }
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getExpiresDate() {
    return expiresDate;
  }

  public void setExpiresDate(String expiresDate) {
    this.expiresDate = expiresDate;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }
}
