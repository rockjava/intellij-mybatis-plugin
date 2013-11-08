package com.seventh7.mybatis.alias;

import com.google.common.collect.Sets;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;
import com.seventh7.mybatis.dom.model.TypeAlias;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    final Set<AliasDesc> result = Sets.newHashSet();
    MapperUtils.processConfiguredTypeAliases(project, new Processor<TypeAlias>() {
      @Override
      public boolean process(TypeAlias typeAlias) {
        addAliasDesc(result, typeAlias.getType().getValue(), typeAlias.getAlias().getStringValue());
        return true;
      }
    });
    return result;
  }

}
