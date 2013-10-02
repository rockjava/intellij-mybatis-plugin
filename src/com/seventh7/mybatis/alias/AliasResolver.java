package com.seventh7.mybatis.alias;

import com.intellij.openapi.project.Project;

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

  @NotNull
  public abstract Set<AliasDesc> getClssDescs();

}
