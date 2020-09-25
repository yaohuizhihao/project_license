package com.suzhuichang.project.license;

import com.suzhuichang.project.license.utils.DESedeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CreateLicense {
  public static void main(String[] args) {
    createFile(
        "C:\\Users\\GLJ\\Desktop", "suzuichang", "中科智云软件技术有限公司", "2020-09-24 13:51:00", "1.0");
  }

  private static void createFile(
      String path, String product, String company, String expireDate, String version) {
    License lic = new License();
    try {
      lic.setCompany(company);
      lic.setExpiresDate(expireDate);
      lic.setProduct(product);
      lic.setVersion(version);
      String sourseStr = lic.getOriginalAuth();
      String encryptStr = DESedeUtil.encrypt(sourseStr, KEY.DESEDE_PUB_KEY);
      lic.setSignature(encryptStr);
      LicFactory e = LicFactoryImpl.getInstance();
      String filename = path + "/license" + CalendarUtil.getNowLong() + ".dat";
      try {
        e.saveToFile(lic, filename);
        //        byte[] e1 = FileUtils.fileToBytes(filename);
        //        FileUtils.deleteFile(filename);
        //        return e1;
      } catch (Exception arg10) {
        arg10.printStackTrace();
      }
    } catch (Exception arg11) {
      arg11.printStackTrace();
    }
    //            return null;
  }

  public static String removeSepcialChar(String str) {
    byte[] bytes = str.getBytes();
    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
    byte[] arg5 = bytes;
    int arg4 = bytes.length;
    for (int e = 0; e < arg4; e++) {
      byte bts = arg5[e];
      if (bts != 10 && bts != 13 && bts != 9 && bts != 32) bytestream.write(bts);
    }
    byte[] arg8 = bytestream.toByteArray();
    try {
      bytestream.close();
    } catch (IOException arg7) {
      arg7.printStackTrace();
    }
    try {
      return new String(arg8, "UTF8");
    } catch (UnsupportedEncodingException arg6) {
      arg6.printStackTrace();
      return str;
    }
  }
}
