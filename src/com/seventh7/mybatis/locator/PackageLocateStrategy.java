package com.seventh7.mybatis.locator;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author yanglin
 */
public class PackageLocateStrategy extends LocateStrategy{

  private PackageProvider provider = new MapperXmlPakcageProvider();

  @Override
  public boolean apply(@NotNull PsiClass clzz) {
    Set<PsiPackage> pakcages = provider.getPackages(clzz.getProject());
    String packageName = ((PsiJavaFile) clzz.getContainingFile()).getPackageName();
    PsiPackage pkg = JavaPsiFacade.getInstance(clzz.getProject()).findPackage(packageName);
    for (PsiPackage tmp : pakcages) {
      if (tmp.equals(pkg)) {
        return true;
      }
    }
    return false;
  }

}
