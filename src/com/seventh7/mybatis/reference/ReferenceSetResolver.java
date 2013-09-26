package com.seventh7.mybatis.reference;

import com.google.common.base.Optional;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author yanglin
 */
public interface ReferenceSetResolver {

  @NotNull
  public Optional<? extends PsiElement> resolve(TextRange range, int index);

  @NotNull
  public Collection<String> getCompletions();

}
