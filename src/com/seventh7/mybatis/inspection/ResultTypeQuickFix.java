package com.seventh7.mybatis.inspection;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.seventh7.mybatis.dom.model.Select;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class ResultTypeQuickFix implements LocalQuickFix {

  private Select select;
  private PsiClass target;

  public ResultTypeQuickFix(Select select, PsiClass target) {
    this.select = select;
    this.target = target;
  }

  @NotNull @Override
  public String getName() {
    return "Corrrect resultType";
  }

  @NotNull @Override
  public String getFamilyName() {
    return getName();
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
  }

  public PsiClass getTarget() {
    return target;
  }

  public void setTarget(PsiClass target) {
    this.target = target;
  }

  public Select getSelect() {
    return select;
  }

  public void setSelect(Select select) {
    this.select = select;
  }
}
