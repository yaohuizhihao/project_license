package com.suzhuichang.project.license;

import com.suzhuichang.project.license.utils.DESedeUtil;
import com.suzhuichang.project.license.utils.XMLDocument;
import com.suzhuichang.project.license.utils.XMLException;
import com.suzhuichang.project.license.utils.XMLNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

public class LicFactoryImpl implements LicFactory {
  private static final Logger log = LoggerFactory.getLogger(LicFactoryImpl.class);

  private static LicFactory factory;

  public static LicFactory getInstance() {
    if (factory == null) {
      factory = new LicFactoryImpl();
    }
    return factory;
  }

  public void saveToFile(License lic, String filename) throws XMLException {
    XMLDocument doc = new XMLDocument();
    try {
      doc.create("license");
      XMLNode arg4 = doc.getRoot();
      arg4.createChildNode("signature");
      arg4.getChildNode("signature").setText(lic.getSignature());
      doc.saveToFile(filename);
    } catch (XMLException arg41) {
      log.error("保存License文件失败/" + arg41.getMessage());
      throw arg41;
    }
  }

  public String getWebContextState(ServletContext webCtx) {
    try {
      License lic = (License) webCtx.getAttribute(License.WEB_KEY);
      if (lic == null) {
        log.error("=======没有有效的证书文件");
        log.error("=======" + lic.toString());
        return "limit";
      }
      if (!lic.getProduct().equals(KEY.PRODUCT)) {
        log.error("=======证书产品信息错误");
        log.error("=======" + lic.toString());
        return "limit";
      }
      if (!lic.getCompany().equals(KEY.COMPANY)) {
        log.error("=======证书公司信息错误");
        log.error("=======" + lic.toString());
        return "limit";
      }
      if (!lic.getVersion().equals(KEY.VERSION)) {
        log.error("=======证书版本错误");
        log.error("=======" + lic.toString());
        return "limit";
      }

      String originalAuth = lic.getOriginalAuth();
      String decryptOriginalAuth = DESedeUtil.decrypt(lic.getSignature(), KEY.DESEDE_PUB_KEY);
      if (!originalAuth.equals(decryptOriginalAuth)) {
        log.error("=======证书签名信息错误");
        log.error("=======" + lic.toString());
        return "limit";
      }

      long days = lic.getExpireDays();
      if (days < 0L) {
        log.error("您的服务期限至:" + lic.getExpiresDate() + ",已超出服务期!");
        return "limit";
      }
      log.info("您的服务期限剩余天数为: " + days + " 天");
      return "run";
    } catch (Exception arg1) {
      return "limit";
    }
  }

  public License fromXML(String filename) {
    log.debug("加载License.dat:" + filename);
    License lic = null;
    try {
      XMLDocument arg4 = new XMLDocument();
      arg4.loadFromFile(filename);
      XMLNode root = arg4.getRoot();
      lic = new License();
      String signature = root.getChildNode("signature").getNodeValue();
      String[] licenseMsg = DESedeUtil.decrypt(signature, KEY.DESEDE_PUB_KEY).split(";");

      lic.setVersion(licenseMsg[1]);
      lic.setExpiresDate(licenseMsg[2]);
      lic.setProduct(licenseMsg[3]);
      lic.setCompany(licenseMsg[0]);
      lic.setSignature(signature);
    } catch (Exception arg41) {
      log.error("加载License.dat失败" + arg41.getMessage());
      return null;
    }
    log.debug("加载License.dat\r\n" + lic.toString());
    return lic;
  }

  public void refreshLicense(License lic, ServletContext webCtx) {
    webCtx.setAttribute(License.WEB_KEY, lic);
  }
}
