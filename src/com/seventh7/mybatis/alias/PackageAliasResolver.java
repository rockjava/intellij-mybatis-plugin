package com.seventh7.mybatis.alias;

import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public abstract class PackageAliasResolver extends AliasResolver{

  public PackageAliasResolver(Project project) {
    super(project);
  }

  @NotNull @Override
  public Set<AliasDesc> getClssDescs() {
    JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
    Set<AliasDesc> result = Sets.newHashSet();
    AliasDesc desc;
    for (String pkgName : getPackages()) {
      PsiPackage pkg = javaPsiFacade.findPackage(pkgName);
      if (null != pkg) {
        for (PsiClass clzz : pkg.getClasses()) {
          desc = new AliasDesc();
          result.add(desc);
          desc.setClzz(clzz);
          desc.setAlias(clzz.getName());
        }
      }
    }
    return result;
  }

  @NotNull
  public abstract Collection<String> getPackages();
}
