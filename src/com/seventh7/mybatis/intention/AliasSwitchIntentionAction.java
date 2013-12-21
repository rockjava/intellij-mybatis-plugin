package com.seventh7.mybatis.intention;

import com.google.common.base.Optional;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.IncorrectOperationException;
import com.seventh7.mybatis.alias.AliasClassReference;
import com.seventh7.mybatis.alias.AliasDesc;
import com.seventh7.mybatis.alias.AliasFacade;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class AliasSwitchIntentionAction extends GenericIntention {

  public AliasSwitchIntentionAction() {
    super(AliasSwitchChooser.INSTANCE);
  }

  @NotNull @Override
  public String getText() {
    return "[Mybatis] Switch between java type and alias";
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file)
      throws IncorrectOperationException {

    int offset = editor.getCaretModel().getOffset();
    PsiElement element = file.findElementAt(offset);
    XmlAttribute attribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class);
    if (attribute == null) { return; }

    PsiReference reference = file.findReferenceAt(offset);

    if (reference instanceof JavaClassReference) {
      boolean success = setupAlias(project, attribute, (JavaClassReference) reference);
      if (!success) {
        HintManager.getInstance().showErrorHint(editor, "No alias found");
      }
    } else if (reference instanceof AliasClassReference) {
      PsiClass psiClass = ((AliasClassReference) reference).resolve();
      if (psiClass != null) {
        attribute.setValue(psiClass.getQualifiedName());
      } else {
        HintManager.getInstance().showErrorHint(editor, "No alias found");
      }
    }
  }

  private boolean setupAlias(Project project, XmlAttribute attribute, JavaClassReference reference) {
    JavaClassReferenceSet set = reference.getJavaClassReferenceSet();
    if (set == null) { return false; }
    PsiReference[] references = set.getReferences();
    if (references == null) { return false; }

    PsiElement ele = references[references.length - 1].resolve();
    if (ele instanceof PsiClass) {
      Optional<AliasDesc> aliasDesc = AliasFacade.getInstance(project).findAliasDesc((PsiClass) ele);
      if (aliasDesc.isPresent()) {
        attribute.setValue(aliasDesc.get().getAlias());
        return true;
      }
    }
    return false;
  }

}
