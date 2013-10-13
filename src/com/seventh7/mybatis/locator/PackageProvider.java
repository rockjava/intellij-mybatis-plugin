package com.seventh7.mybatis.locator;

import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author yanglin
 */
public abstract class PackageProvider {

  @NotNull
  public abstract Set<String> getPakcages(@NotNull Project project);

}