package com.seventh7.mybatis.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.seventh7.mybatis.generate.MapperXmlGenerator;
import com.seventh7.mybatis.locator.MapperLocator;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class MapperInXmlInspection extends MapperInspection{

  @Nullable
  @Override
  public ProblemDescriptor[] checkClass(@NotNull PsiClass clazz, @NotNull InspectionManager manager, boolean isOnTheFly) {
    if (MapperUtils.isTargetPresentInMapperXml(clazz) ||
        clazz.getNameIdentifier() == null ||
        !MapperLocator.getInstance(clazz.getProject()).process(clazz)) {
      return EMPTY_ARRAY;
    }
    ProblemDescriptor pd = manager.createProblemDescriptor(clazz.getNameIdentifier(),
                                                            "Mapper xml does not exist",
                                                            new GenerateMapperXmlQuickFix(
                                                                clazz),
                                                            ProblemHighlightType.GENERIC_ERROR,
                                                            isOnTheFly);
    return new ProblemDescriptor[]{pd};
  }

  private static class GenerateMapperXmlQuickFix extends GenericQuickFix{

    private final PsiClass clazz;

    public GenerateMapperXmlQuickFix(@NotNull PsiClass clazz) {
      this.clazz = clazz;
    }

    @NotNull
    @Override
    public String getName() {
      return "Generate mapper xml";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      MapperXmlGenerator.getInstance().generate(clazz);
    }

  }

}
