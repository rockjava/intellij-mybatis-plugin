package com.seventh7.mybatis.locator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author yanglin
 */
public class PackageLocateStrategy extends LocateStrategy{

  private PackageProvider provider = new MapperXmlPakcageProvider();

  @Override
  public boolean apply(@NotNull PsiClass clzz) {
    Set<String> pakcages = provider.getPakcages(clzz.getProject());
    String pkg = ((PsiJavaFile) clzz.getContainingFile()).getPackageName();
    for (String tmp : pakcages) {
      if (tmp.equals(pkg)) {
        return true;
      }
    }
    return false;
  }

}
