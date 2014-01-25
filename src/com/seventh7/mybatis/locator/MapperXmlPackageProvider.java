package com.seventh7.mybatis.locator;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackage;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yanglin
 */
public class MapperXmlPackageProvider extends PackageProvider{

  @NotNull @Override
  public Set<PsiPackage> getPackages(@NotNull Project project) {
    HashSet<PsiPackage> res = Sets.newHashSet();
    Collection<Mapper> mappers = MapperUtils.findMappers(project);
    JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
    for (Mapper mapper : mappers) {
      final Optional<PsiClass> clazz = JavaUtils.findClazz(project, MapperUtils.getNamespace(mapper));
      if (clazz.isPresent()) {
        PsiFile file = clazz.get().getContainingFile();
        if (file instanceof PsiJavaFile) {
          PsiPackage pkg = javaPsiFacade.findPackage(((PsiJavaFile) file).getPackageName());
          if (null != pkg) {
            res.add(pkg);
          }
        }
      }
    }
    return res;
  }

}
