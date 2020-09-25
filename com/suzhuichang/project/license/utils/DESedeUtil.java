package com.suzhuichang.project.license.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESedeUtil {

  /** 密钥算法 */
  private static final String KEY_ALGORITHM = "DESede";

  /** 加密/解密算法 / 工作模式 / 填充方式 Java 6支持PKCS5Padding填充方式 Bouncy Castle支持PKCS7Padding填充方式 */
  private static final String CIPHER_ALGORITHM = "DESede/ECB/PKCS5Padding";

  /**
   * 生成密钥
   *
   * @return 密钥
   * @throws Exception
   */
  public static String generateKey() throws Exception {
    // 实例化密钥生成器
    KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
    // DESede 要求密钥长度为 112位或168位
    kg.init(168);
    // 生成密钥
    SecretKey secretKey = kg.generateKey();
    // 获得密钥的字符串形式
    return Base64.encodeBase64String(secretKey.getEncoded());
  }

  /**
   * des加密
   *
   * @param source 源字符串
   * @param key 密钥
   * @return 加密后的密文
   * @throws Exception
   */
  public static String encrypt(String source, String key) throws Exception {
    byte[] sourceBytes = source.getBytes("UTF-8");
    byte[] keyBytes = Base64.decodeBase64(key);
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM));
    byte[] decrypted = cipher.doFinal(sourceBytes);
    return Base64.encodeBase64String(decrypted);
  }

  /**
   * des解密
   *
   * @param encryptStr 密文
   * @param key 密钥
   * @return 源字符串
   * @throws Exception
   */
  public static String decrypt(String encryptStr, String key) throws Exception {
    byte[] sourceBytes = Base64.decodeBase64(encryptStr);
    byte[] keyBytes = Base64.decodeBase64(key);
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM));
    byte[] decoded = cipher.doFinal(sourceBytes);
    return new String(decoded, "UTF-8");
  }
}
