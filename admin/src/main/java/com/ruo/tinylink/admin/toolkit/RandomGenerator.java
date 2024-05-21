package com.ruo.tinylink.admin.toolkit;

import java.security.SecureRandom;

public final class RandomGenerator {

  private static final String CHARACTERS =
      "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  private static final SecureRandom RANDOM = new SecureRandom();

  /**
   * generate random group ID
   *
   * @return group ID
   */
  public static String generateRandom() {
    return generateRandom(6);
  }

  /**
   * generate random group ID
   *
   * @param length
   * @return group ID
   */
  public static String generateRandom(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      int randomIndex = RANDOM.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(randomIndex));
    }
    return sb.toString();
  }
}
