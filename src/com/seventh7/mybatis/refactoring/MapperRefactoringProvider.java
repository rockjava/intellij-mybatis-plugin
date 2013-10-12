package com.seventh7.mybatis.refactoring;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;

/**
 * @author yanglin
 */
public class MapperRefactoringProvider implements RefactoringElementListenerProvider {

  @Nullable @Override
  public RefactoringElementListener getListener(final PsiElement element) {
    if (!(element instanceof PsiClass)) return null;
    return new RefactoringElementListener() {
      @Override
      public void elementMoved(@NotNull PsiElement newElement) {
      }

      @Override
      public void elementRenamed(@NotNull final PsiElement newElement) {
        if (newElement instanceof PsiClass) {
          ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override public void run() {
              renameMapperXml((PsiClass) element, (PsiClass) newElement);
            }
          });
        }
      }
    };
  }

  private void renameMapperXml(@NotNull final PsiClass oldClzz, @NotNull final PsiClass newClzz) {
    Collection<Mapper> mappers = MapperUtils.findMappers(oldClzz.getProject(), oldClzz);
    for (Mapper mapper : mappers) {
      try {
        VirtualFile vf = mapper.getXmlTag().getOriginalElement().getContainingFile().getVirtualFile();
        if (null != vf) {
          vf.rename(MapperRefactoringProvider.this, newClzz.getName() + "." + vf.getExtension());
        }
      } catch (IOException e) {
      }
    }
  }

}
