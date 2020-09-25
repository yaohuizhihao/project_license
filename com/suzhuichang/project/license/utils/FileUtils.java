package com.suzhuichang.project.license.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件处理工具类
 *
 * @author ruoyi
 */
public class FileUtils {
  public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

  /**
   * 输出指定文件的byte数组
   *
   * @param filePath 文件路径
   * @param os 输出流
   * @return
   */
  public static void writeBytes(String filePath, OutputStream os) throws IOException {
    FileInputStream fis = null;
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        throw new FileNotFoundException(filePath);
      }
      fis = new FileInputStream(file);
      byte[] b = new byte[1024];
      int length;
      while ((length = fis.read(b)) > 0) {
        os.write(b, 0, length);
      }
    } catch (IOException e) {
      throw e;
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
  }

  /**
   * 删除文件
   *
   * @param filePath 文件
   * @return
   */
  public static boolean deleteFile(String filePath) {
    boolean flag = false;
    File file = new File(filePath);
    // 路径为文件且不为空则进行删除
    if (file.isFile() && file.exists()) {
      file.delete();
      flag = true;
    }
    return flag;
  }

  /**
   * 文件名称验证
   *
   * @param filename 文件名称
   * @return true 正常 false 非法
   */
  public static boolean isValidFilename(String filename) {
    return filename.matches(FILENAME_PATTERN);
  }

  /**
   * 下载文件名重新编码
   *
   * @param request 请求对象
   * @param fileName 文件名
   * @return 编码后的文件名
   */
  public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
      throws UnsupportedEncodingException {
    final String agent = request.getHeader("USER-AGENT");
    String filename = fileName;
    if (agent.contains("MSIE")) {
      // IE浏览器
      filename = URLEncoder.encode(filename, "utf-8");
      filename = filename.replace("+", " ");
    } else if (agent.contains("Firefox")) {
      // 火狐浏览器
      filename = new String(fileName.getBytes(), "ISO8859-1");
    } else if (agent.contains("Chrome")) {
      // google浏览器
      filename = URLEncoder.encode(filename, "utf-8");
    } else {
      // 其它浏览器
      filename = URLEncoder.encode(filename, "utf-8");
    }
    return filename;
  }

  /** 复制文件 */
  public static void infile(String oldPath, String newPath) {
    try {
      int bytesum = 0;
      int byteread = 0;
      File oldfile = new File(oldPath);
      if (oldfile.exists()) {
        // 文件存在时
        InputStream inStream = new FileInputStream(oldPath);
        // 读入原文件
        File dirTempFile = new File(newPath.substring(0, newPath.lastIndexOf("/")));
        if (!dirTempFile.exists()) {
          dirTempFile.mkdirs();
        }
        FileOutputStream fs = new FileOutputStream(newPath);
        byte[] buffer = new byte[1444];
        while ((byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread;
          // 字节数 文件大小
          fs.write(buffer, 0, byteread);
        }
        inStream.close();
        fs.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static byte[] fileToBytes(String filename) {
    File file = new File(filename);
    InputStream in = null;
    try {
      in = new FileInputStream(file);
      byte[] bytes = inputStreamToByte(in);
      in.close();
      return bytes;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static byte[] inputStreamToByte(InputStream in) throws IOException {
    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
    int ch;
    while ((ch = in.read()) != -1) {
      bytestream.write(ch);
    }
    byte[] bytes = bytestream.toByteArray();
    bytestream.close();
    return bytes;
  }
}
