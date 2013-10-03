package com.seventh7.mybatis.alias;

import com.google.common.base.Optional;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * @author yanglin
 */
public abstract class AliasResolver {

  protected Project project;

  public AliasResolver(Project project) {
    this.project = project;
  }

  @NotNull
  protected Optional<AliasDesc> addAliasDesc(@NotNull Set<AliasDesc> descs, @Nullable PsiClass clzz, @Nullable String alias) {
    if (null == alias || !JavaUtils.isModelClzz(clzz)) {
      return Optional.absent();
    }
    AliasDesc desc = new AliasDesc();
    descs.add(desc);
    desc.setClzz(clzz);
    desc.setAlias(alias);
    return Optional.of(desc);
  }

  @NotNull
  public abstract Set<AliasDesc> getClssAliasDescriptions();

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
