package com.halalhomemade.backend.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.halalhomemade.backend.exceptions.InternalServiceException;

@Component
@Slf4j
public class EncDecSymmetric {

  @Value("${mail.encode.key}")
  private String encodedKey;

  // Symmetric encryption algorithms supported - AES, RC4, DES
  protected static final String DEFAULT_ENCRYPTION_ALGORITHM = "AES";
  protected SecretKey mSecretKey;

  @PostConstruct
  void initialize() {
    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    mSecretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, DEFAULT_ENCRYPTION_ALGORITHM);
  }

  public static String keyToString(byte[] byteArray) {
    return Base64.getEncoder().encodeToString(byteArray);
  }

  public static byte[] stringToKey(String key) {
    return Base64.getDecoder().decode(key);
  }

  public byte[] encryptText(String textToEncrypt) {
    byte[] byteCipherText = null;
    try {
      Cipher encCipher = Cipher.getInstance(DEFAULT_ENCRYPTION_ALGORITHM);
      encCipher.init(Cipher.ENCRYPT_MODE, mSecretKey);
      byteCipherText = encCipher.doFinal(textToEncrypt.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new InternalServiceException("Error producing encrypted text", e);
    }
    return byteCipherText;
  }

  public String decryptText(byte[] encryptedText) {
    String decryptedPlainText = null;
    try {
      Cipher aesCipher2 = Cipher.getInstance(DEFAULT_ENCRYPTION_ALGORITHM);
      aesCipher2.init(Cipher.DECRYPT_MODE, mSecretKey);
      byte[] bytePlainText = aesCipher2.doFinal(encryptedText);
      decryptedPlainText = new String(bytePlainText, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new InternalServiceException("Error decrypting text", e);
    }
    return decryptedPlainText;
  }
}
