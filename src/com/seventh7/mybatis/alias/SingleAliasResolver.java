package com.seventh7.mybatis.alias;

import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.seventh7.mybatis.dom.model.Configuration;
import com.seventh7.mybatis.dom.model.TypeAlias;
import com.seventh7.mybatis.dom.model.TypeAliases;
import com.seventh7.mybatis.util.DomUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yanglin
 */
public class SingleAliasResolver extends AliasResolver{

  public SingleAliasResolver(Project project) {
    super(project);
  }

  @NotNull @Override
  public Set<AliasDesc> getClssAliasDescriptions(@Nullable PsiElement element) {
    HashSet<AliasDesc> result = Sets.newHashSet();
    for (Configuration conf : DomUtils.findDomElements(project, Configuration.class)) {
      for (TypeAliases tas : conf.getTypeAliases()) {
        for (TypeAlias ta : tas.getTypeAlias()) {
          String stringValue = ta.getAlias().getStringValue();
          if (null != stringValue) {
            addAliasDesc(result, ta.getType().getValue(), stringValue);
          }
        }
      }
    }
    return result;
  }

}
