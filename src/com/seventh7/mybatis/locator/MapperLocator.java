package com.seventh7.mybatis.locator;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class MapperLocator {

  public static LocateStrategy dfltLocateStrategy = new PackageLocateStrategy();

  public static MapperLocator getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, MapperLocator.class);
  }

  public boolean process(@Nullable PsiMethod method) {
    return null != method && process(method.getContainingClass());
  }

  public boolean process(@Nullable PsiClass clzz) {
    return null != clzz && JavaUtils.isElementWithinInterface(clzz) && dfltLocateStrategy.apply(clzz);
  }

}
