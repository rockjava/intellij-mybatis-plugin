package com.seventh7.mybatis.util;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public final class LongStoryUtils {

  private LongStoryUtils() {
    throw new UnsupportedOperationException();
  }

  public static String clearDummyIdentifier(@NotNull String target) {
    return target.replace(MybatisConstants.DUMMY_IDENTIFIER, "");
  }

}
