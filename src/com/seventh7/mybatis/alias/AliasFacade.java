package com.seventh7.mybatis.alias;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author yanglin
 */
public class AliasFacade {

  private Project project;

  public static final AliasFacade getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, AliasFacade.class);
  }

  public AliasFacade(Project project) {
    this.project = project;
  }

  @NotNull
  public Optional<PsiClass> findPsiClass(@NotNull String shortName) {
    for (AliasResolver resolver : createAliasResolvers()) {
      Set<AliasDesc> clssDescs = resolver.getClssDescs();
      for (AliasDesc desc : clssDescs) {
        if (desc.getAlias().equals(shortName)) {
          return Optional.of(desc.getClzz());
        }
      }
    }
    return Optional.absent();
  }

  @NotNull
  public Collection<PsiClass> getAliasSupporttedClasses() {
    ArrayList<PsiClass> result = Lists.newArrayList();
    for (AliasResolver resolver : createAliasResolvers()) {
      Set<AliasDesc> clssDescs = resolver.getClssDescs();
      for (AliasDesc desc : clssDescs) {
        result.add(desc.getClzz());
      }
    }
    return result;
  }

  private AliasResolver[] createAliasResolvers() {
    return new AliasResolver[] {
        AliasResolverFactory.createAnnotationResolver(project),
        AliasResolverFactory.createSingleAliasResolver(project),
        AliasResolverFactory.createConfigPackageResolver(project),
        AliasResolverFactory.createBeanResolver(project)
    };
  }

}