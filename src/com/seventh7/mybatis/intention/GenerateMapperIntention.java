package com.seventh7.mybatis.intention;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.generate.MapperXmlGenerator;
import com.seventh7.mybatis.intention.chooser.GenerateMapperChooser;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateMapperIntention extends GenericIntention {

  public GenerateMapperIntention() {
    super(GenerateMapperChooser.INSTANCE);
  }

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Generate mapper of xml";
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  @Override
  public void invoke(@NotNull final Project project, final Editor editor, PsiFile file) throws IncorrectOperationException {
    PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
    PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
    if (clazz == null) {
      return;
    }
    MapperXmlGenerator.getInstance().generate(clazz);
  }

}