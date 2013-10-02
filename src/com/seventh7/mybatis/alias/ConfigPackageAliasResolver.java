package com.seventh7.mybatis.alias;

import com.google.common.collect.Lists;

import com.intellij.openapi.project.Project;
import com.seventh7.mybatis.dom.model.*;
import com.seventh7.mybatis.util.DomUtils;

import org.jetbrains.annotations.NotNull;

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
  public Collection<String> getPackages() {
    ArrayList<String> result = Lists.newArrayList();
    for (Configuration conf : DomUtils.findDomElements(project, Configuration.class)) {
      for (TypeAliases tas : conf.getTypeAliases()) {
        for (com.seventh7.mybatis.dom.model.Package pkg : tas.getPackages()) {
          result.add(pkg.getName().getStringValue());
        }
      }
    }
    return result;
  }

}
