package com.seventh7.mybatis.inspection;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.xml.DomElement;
import com.seventh7.mybatis.annotation.Annotation;
import com.seventh7.mybatis.dom.model.Select;
import com.seventh7.mybatis.generate.StatementGenerator;
import com.seventh7.mybatis.locator.MapperLocator;
import com.seventh7.mybatis.service.JavaService;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglin
 */
public class MapperMethodInspection extends MapperInspection{

  private static final String RESULT_HANDLER_CLASS = "org.apache.ibatis.session.ResultHandler";

  @Nullable @Override
  public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
    PsiClass containingClass = method.getContainingClass();
    if (containingClass == null) { return EMPTY_ARRAY; }
    if (!containingClass.isPhysical() || !containingClass.isInterface()) {
      return EMPTY_ARRAY;
    }
    /** Correct when:
     *  1. Method interface are not defined in the package where most other mapper interfaces are defined
     *  2. No id based statement defined with annotation
     *  3. No custom result handler is present
     */
    boolean correctResultTypeCase = MapperLocator.getInstance(method.getProject()).process(method) &&
                                    !JavaUtils.isAnyAnnotationPresent(method, Annotation.STATEMENT_SYMMETRIES) &&
                                    !isResultHandlerPresent(method);
    if (!correctResultTypeCase) {
      return EMPTY_ARRAY;
    }
    List<ProblemDescriptor> res = createProblemDescriptors(method, manager, isOnTheFly);
    return res.toArray(new ProblemDescriptor[res.size()]);
  }

  private boolean isResultHandlerPresent(PsiMethod method) {
    Optional<PsiClass> handlerClazz = JavaUtils.findClazz(method.getProject(), RESULT_HANDLER_CLASS);
    if (!handlerClazz.isPresent()) {
      return false;
    }
    PsiClass handlerPsiClass = handlerClazz.get();
    PsiParameter[] params = method.getParameterList().getParameters();
    for (PsiParameter param : params) {
      PsiType type = param.getType();
      if (type instanceof PsiClassReferenceType) {
        PsiClass psiClass = ((PsiClassReferenceType) type).resolve();
        if (psiClass != null && (psiClass.equals(handlerPsiClass) || psiClass.isInheritor(handlerPsiClass, true))) {
          return true;
        }
      }
    }
    return false;
  }

  private List<ProblemDescriptor> createProblemDescriptors(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
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
        Optional<PsiClass> target = StatementGenerator.getSelectResultType(method);
        PsiClass clazz = select.getResultType().getValue();
        PsiIdentifier ide = method.getNameIdentifier();
        if (null != ide && null == select.getResultMap().getValue()) {
          if (target.isPresent() && (null == clazz || !target.get().equals(clazz))) {
            return  Optional.of(manager.createProblemDescriptor(ide, "Result type not match for select id=\"#ref\"",
                                                                new ResultTypeQuickFix(select, target.get()), ProblemHighlightType.GENERIC_ERROR, isOnTheFly));
          } else  if (!target.isPresent() && null != clazz) {
            return  Optional.of(manager.createProblemDescriptor(ide, "Result type not match for select id=\"#ref\"",
                                                                (LocalQuickFix)null, ProblemHighlightType.GENERIC_ERROR, isOnTheFly));
          }
        }
      }
    }
    return Optional.absent();
  }

  private Optional<ProblemDescriptor> checkStatementExists(PsiMethod method, InspectionManager manager, boolean isOnTheFly) {
    PsiClass clazz = method.getContainingClass();
    if (clazz == null) {
      return Optional.absent();
    }
    if (!MapperUtils.findFirstMapper(clazz.getProject(), clazz).isPresent()) {
      return Optional.absent();
    }
    PsiIdentifier ide = method.getNameIdentifier();
    if (!JavaService.getInstance(method.getProject()).findStatement(method).isPresent() && null != ide) {
      return  Optional.of(manager.createProblemDescriptor(ide, "Statement with id=\"#ref\" not defined in mapper xml",
                                                          new StatementNotExistsQuickFix(method), ProblemHighlightType.GENERIC_ERROR, isOnTheFly));
    }
    return Optional.absent();
  }

}
