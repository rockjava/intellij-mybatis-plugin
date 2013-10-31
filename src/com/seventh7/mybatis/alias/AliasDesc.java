package com.seventh7.mybatis.alias;

import com.intellij.psi.PsiClass;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class AliasDesc {

  private PsiClass clzz;

  private String alias;

  public AliasDesc() {
  }

  public static AliasDesc create(@NotNull PsiClass psiClass, @NotNull String alias) {
    return new AliasDesc(psiClass, alias);
  }

  public AliasDesc(PsiClass clzz, String alias) {
    this.clzz = clzz;
    this.alias = alias;
  }

  public PsiClass getClzz() {
    return clzz;
  }

  public void setClzz(PsiClass clzz) {
    this.clzz = clzz;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AliasDesc aliasDesc = (AliasDesc) o;

    if (alias != null ? !alias.equals(aliasDesc.alias) : aliasDesc.alias != null) {
      return false;
    }
    if (clzz != null ? !clzz.equals(aliasDesc.clzz) : aliasDesc.clzz != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = clzz != null ? clzz.hashCode() : 0;
    result = 31 * result + (alias != null ? alias.hashCode() : 0);
    return result;
  }
}
