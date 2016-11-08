package com.seventh7.mybatis.dom.converter;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.ConvertContext;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author yanglin
 */
public class CacheRefNamespaceConverter extends ConverterAdaptor<PsiClass> {

  @NotNull @Override
  public Collection<? extends PsiClass> getVariants(ConvertContext context) {
    final Project project = context.getProject();
    final Collection<Mapper> mappers = MapperUtils.findMappers(project);
    return Collections2.transform(mappers, new Function<Mapper, PsiClass>() {
      @Override
      public PsiClass apply(Mapper input) {
        return JavaUtils.findClazz(project, input.getNamespace().getStringValue()).orNull();
      }
    });
  }

  @Nullable @Override
  public String toString(@Nullable PsiClass psiClass, ConvertContext context) {
    return psiClass == null ? null : psiClass.getQualifiedName();
  }

  @Nullable @Override
  public PsiClass fromString(@Nullable @NonNls String s, ConvertContext context) {
    return JavaUtils.findClazz(context.getProject(), s).orNull();
  }

}
