package com.seventh7.mybatis.alias;

import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class AliasResolverFactory {

  public static AliasResolver createAnnotationResolver(@NotNull Project project) {
    return AnnotationAliasResolver.getInstance(project);
  }

  public static AliasResolver createBeanResolver(@NotNull Project project) {
    return new BeanAliasResolver(project);
  }

  public static AliasResolver createConfigPackageResolver(@NotNull Project project) {
    return new ConfigPackageAliasResolver(project);
  }

  public static AliasResolver createSingleAliasResolver(@NotNull Project project) {
    return new SingleAliasResolver(project);
  }
}
