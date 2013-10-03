package com.seventh7.mybatis.alias;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

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

  @Nullable
  protected AliasDesc addAliasDesc(@NotNull Set<AliasDesc> descs, @Nullable PsiClass clzz, @Nullable String alias) {
    if (null == clzz || null == alias) {
      return null;
    }
    AliasDesc desc = new AliasDesc();
    descs.add(desc);
    desc.setClzz(clzz);
    desc.setAlias(alias);
    return desc;
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
