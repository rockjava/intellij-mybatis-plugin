package com.seventh7.mybatis.generate;

import com.google.common.base.Optional;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
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
    Optional<PsiClass> clzz = StatementGenerator.getSelectResultType(method);
    if (clzz.isPresent()) {
      GenericAttributeValue<PsiClass> resultType = select.getResultType();
      if (null != resultType) {
        resultType.setValue(clzz.get());
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
