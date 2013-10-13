package com.seventh7.mybatis.inspection;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.dom.model.Select;
import com.seventh7.mybatis.locator.MapperLocator;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @author yanglin
 */
public class MapperMethodInspection extends MapperInspection{

  @Nullable @Override
  public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
    Project project = method.getProject();
    if (!MapperLocator.getInstance(project).process(method) || JavaUtils.isAnyAnnotationPresent(method, Annotation.STATEMENT_SYMMETRIES))
      return EMPTY_ARRAY;
    ArrayList<ProblemDescriptor> res = createProblemDescriptors(method, manager, isOnTheFly);
    return res.toArray(new ProblemDescriptor[res.size()]);
  }

  private ArrayList<ProblemDescriptor> createProblemDescriptors(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
    ArrayList<ProblemDescriptor> res = Lists.newArrayList();
    Optional<ProblemDescriptor> p1 = checkStatementExists(method, manager, isOnTheFly);
    if (p1.isPresent()) {
      res.add(p1.get());
    }
    Optional<ProblemDescriptor> p2 = checkResultType(method, manager, isOnTheFly);
    if (p2.isPresent()) {
      res.add(p2.get());
    }
    return res;
  }

  private Optional<ProblemDescriptor> checkResultType(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
    Optional<DomElement> ele = JavaService.getInstance(method.getProject()).findStatement(method);
    if (ele.isPresent()) {
      DomElement domElement = ele.get();
      if (domElement instanceof Select) {
        Select select = (Select)domElement;
        GenericAttributeValue<PsiClass> resultType = select.getResultType();
        PsiType returnType = method.getReturnType();
        PsiIdentifier ide = method.getNameIdentifier();
      }
    }
    return Optional.absent();
  }

  private Optional<ProblemDescriptor> checkStatementExists(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
    PsiIdentifier ide = method.getNameIdentifier();
    if (!JavaService.getInstance(method.getProject()).findStatement(method).isPresent() && null != ide) {
      return  Optional.of(manager.createProblemDescriptor(ide, "Statement with id=\"#ref\" not defined in mapper xml",
                                                          new StatementNotExistsQuickFix(method), ProblemHighlightType.GENERIC_ERROR, isOnTheFly));
    }
    return Optional.absent();
  }

}
