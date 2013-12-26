package com.seventh7.mybatis.alias;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yanglin
 */
public class AliasFacade {

  private Project project;

  private List<AliasResolver> resolvers;

  public static final AliasFacade getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, AliasFacade.class);
  }

  public AliasFacade(Project project) {
    this.project = project;
    this.resolvers = Lists.newArrayList();
    initResolvers();
  }

  private void initResolvers() {
    try {
      Class.forName("com.intellij.spring.model.utils.SpringModelUtils");
      this.registerResolver(AliasResolverFactory.createBeanResolver(project));
    } catch (ClassNotFoundException e) {
    }
    this.registerResolver(AliasResolverFactory.createSingleAliasResolver(project));
    this.registerResolver(AliasResolverFactory.createConfigPackageResolver(project));
    this.registerResolver(AliasResolverFactory.createAnnotationResolver(project));
    this.registerResolver(AliasResolverFactory.createInnerAliasResolver(project));
  }

  @NotNull
  public Optional<PsiClass> findPsiClass(@Nullable PsiElement element, @NotNull String clazzName) {
    final Optional<PsiClass> clazz = JavaUtils.findClazz(project, clazzName);
    if (clazz.isPresent()) {
      return clazz;
    }
    for (AliasResolver resolver : resolvers) {
      for (AliasDesc desc : resolver.getClassAliasDescriptions(element)) {
        if (desc.getAlias().equals(clazzName)) {
          return Optional.of(desc.getClazz());
        }
      }
    }
    return Optional.absent();
  }

  @NotNull
  public Collection<AliasDesc> getAliasDescs(@Nullable PsiElement element) {
    ArrayList<AliasDesc> result = Lists.newArrayList();
    for (AliasResolver resolver : resolvers) {
      result.addAll(resolver.getClassAliasDescriptions(element));
    }
    return result;
  }

  public Optional<AliasDesc> findAliasDesc(@Nullable PsiClass clazz) {
    if (null == clazz) {
      return Optional.absent();
    }
    for (AliasResolver resolver : resolvers) {
      for (AliasDesc desc : resolver.getClassAliasDescriptions(clazz)) {
        if (desc.getClazz().equals(clazz)) {
          return Optional.of(desc);
        }
      }
    }
    return Optional.absent();
  }

  public void registerResolver(@NotNull AliasResolver resolver) {
    this.resolvers.add(resolver);
  }

}
