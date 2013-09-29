package com.seventh7.mybatis.util;

import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DomUtils {

  private DomUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isMybatisFile(@Nullable PsiFile file) {
    if (!isXmlFile(file)) {
      return false;
    }
    XmlTag rootTag = ((XmlFile) file).getRootTag();
    return null != rootTag && rootTag.getName().equals("mapper");
  }

  public static boolean isMybatisConfigurationFile(@NotNull PsiFile file) {
    if (!isXmlFile(file)) {
      return false;
    }
    XmlTag rootTag = ((XmlFile) file).getRootTag();
    return null != rootTag && rootTag.getName().equals("configuration");
  }

  public static boolean isBeansFile(@NotNull PsiFile file) {
    if (!isXmlFile(file)) {
      return false;
    }
    XmlTag rootTag = ((XmlFile) file).getRootTag();
    return null != rootTag && rootTag.getName().equals("beans");
  }

  static boolean isXmlFile(@NotNull PsiFile file) {
    return file instanceof XmlFile;
  }

}