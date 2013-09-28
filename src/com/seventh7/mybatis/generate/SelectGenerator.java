package com.seventh7.mybatis.generate;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.dom.model.GroupTwo;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.dom.model.Select;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class SelectGenerator extends StatementGenerator{

  public SelectGenerator(@NotNull String... patterns) {
    super(patterns);
  }

  @NotNull @Override
  protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
    Select select = mapper.addSelect();
    setupResultType(method, select);
    return select;
  }

  private void setupResultType(PsiMethod method, Select select) {
    PsiType returnType = method.getReturnType();
    GenericAttributeValue<String> resultType = select.getResultType();
    if (returnType instanceof PsiPrimitiveType && returnType != PsiType.VOID) {
      resultType.setValue(((PsiPrimitiveType) returnType).getBoxedTypeName());
    } else if (returnType instanceof PsiClassReferenceType) {
      PsiClassReferenceType type = (PsiClassReferenceType)returnType;
      if (type.hasParameters()) {
        PsiType[] parameters = type.getParameters();
        if (parameters.length == 1) {
          type = (PsiClassReferenceType)parameters[0];
        }
      }
      PsiClass clzz = type.resolve();
      if (null != clzz) {
        resultType.setValue(clzz.getQualifiedName());
      }
    }
  }

  @NotNull @Override
  public String getId() {
    return "SelectGenerator";
  }

  @NotNull @Override
  public String getDisplayText() {
    return "Select Statement";
  }
}
