package com.seventh7.mybatis.intention.chooser;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class GenerateMapperChooser extends JavaFileIntentionChooser {

  public static final JavaFileIntentionChooser INSTANCE = new GenerateMapperChooser();

  @Override
  public boolean isAvailable(@NotNull PsiElement element) {
    if (isPositionOfInterfaceDeclaration(element)) {
      PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
      if (null != clazz) {
        return !isTargetPresentInXml(clazz);
      }
    }
    return false;
  }

}
