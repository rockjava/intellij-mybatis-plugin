package com.seventh7.mybatis.util;

import java.util.Collection;

/**
 * @author yanglin
 */
public final class CollectionUtils {

  private CollectionUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isEmpty(Collection collection) {
    return null == collection || collection.size() == 0;
  }

  public static boolean isNotEmpty(Collection collection) {
    return !isEmpty(collection);
  }

}
