package com.seventh7.mybatis.alias;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author yanglin
 */
public abstract class AliasResolver {

  protected Project project;

  public AliasResolver(Project project) {
    this.project = project;
  }

  protected AliasDesc addAliasDesc(@NotNull Set<AliasDesc> descs, @NotNull PsiClass clzz, @NotNull String alias) {
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
