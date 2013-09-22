package com.seventh7.mybatis.generate;

import com.intellij.psi.PsiMethod;
import com.seventh7.mybatis.dom.model.GroupTwo;
import com.seventh7.mybatis.dom.model.Mapper;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class InsertGenerator extends StatementGenerator{

  public InsertGenerator(String pattern, String... patterns) {
    super(pattern, patterns);
  }

  @NotNull @Override
  protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
    return mapper.addInsert();
  }

  @NotNull @Override
  public String getId() {
    return "InsertGenerator";
  }

  @NotNull @Override
  public String getDisplayText() {
    return "Insert Statement";
  }
}
