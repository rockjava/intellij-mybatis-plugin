package com.seventh7.mybatis.locator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author yanglin
 */
public abstract class PackageProvider {

  @NotNull
  public abstract Set<PsiPackage> getPakcages(@NotNull Project project);

}