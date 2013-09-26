package com.seventh7.mybatis.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;

/**
 * @author yanglin
 */
public class BatisImmediateReference<T extends PsiElement> extends PsiReferenceBase.Immediate<T>{

  public BatisImmediateReference(T element, TextRange range, PsiElement resolveTo) {
    super(element, range, resolveTo);
  }

}