package com.seventh7.mybatis.alias;

import com.google.common.collect.Lists;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.seventh7.mybatis.dom.model.Configuration;
import com.seventh7.mybatis.dom.model.TypeAliases;
import com.seventh7.mybatis.util.DomUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author yanglin
 */
public class ConfigPackageAliasResolver extends PackageAliasResolver{

  public ConfigPackageAliasResolver(Project project) {
    super(project);
  }

  @NotNull @Override
  public Collection<String> getPackages(@Nullable PsiElement element) {
    ArrayList<String> result = Lists.newArrayList();
    for (Configuration conf : DomUtils.findDomElements(project, Configuration.class)) {
      for (TypeAliases tas : conf.getTypeAliases()) {
        for (com.seventh7.mybatis.dom.model.Package pkg : tas.getPackages()) {
          String stringValue = pkg.getName().getStringValue();
          if (null != stringValue) {
            result.add(stringValue);
          }
        }
      }
    }
    return result;
  }

}
