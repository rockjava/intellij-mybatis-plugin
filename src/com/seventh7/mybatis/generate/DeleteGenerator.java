package com.seventh7.mybatis.generate;

import com.intellij.psi.PsiMethod;
import com.seventh7.mybatis.dom.model.GroupTwo;
import com.seventh7.mybatis.dom.model.Mapper;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class DeleteGenerator extends StatementGenerator{

  public DeleteGenerator(String pattern, String... patterns) {
    super(pattern, patterns);
  }

  @NotNull @Override
  protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
    return mapper.addDelete();
  }

  @NotNull @Override
  public String getId() {
    return "DeleteGenerator";
  }

  @NotNull @Override
  public String getDisplayText() {
    return "Delete Statement";
  }

}
