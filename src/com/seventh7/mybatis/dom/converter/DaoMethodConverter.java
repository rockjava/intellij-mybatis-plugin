package com.seventh7.mybatis.dom.converter;

import com.intellij.psi.PsiMethod;
import com.intellij.util.xml.ConvertContext;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class DaoMethodConverter extends ConverterAdaptor<PsiMethod> {

  @Nullable @Override
  public PsiMethod fromString(@Nullable @NonNls String id, ConvertContext context) {
    Mapper mapper = MapperUtils.getMapper(context.getInvocationElement());
    return JavaUtils.findMethod(context.getProject(), MapperUtils.getNamespace(mapper), id).orNull();
  }

}