package com.seventh7.mybatis.alias;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.seventh7.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.seventh7.mybatis.annotation.Annotation.ALIAS;

/**
 * @author yanglin
 */
public class AnnotationAliasResolver extends AliasResolver implements ProjectComponent {

  private Project project;

  private Set<PsiClass> clzzCache;

  private Function<PsiClass, AliasDesc> function = new Function<PsiClass, AliasDesc>() {
    @Override
    public AliasDesc apply(PsiClass input) {
      AliasDesc ad = new AliasDesc();
      ad.setClzz(input);
      ad.setAlias(JavaUtils.getAnnotationValueText(input, ALIAS).get());
      return ad;
    }
  };

  public static final AnnotationAliasResolver getInstance(@NotNull Project project) {
    return project.getComponent(AnnotationAliasResolver.class);
  }

  public AnnotationAliasResolver(Project project) {
    super(project);
    this.project = project;
    this.clzzCache = Sets.newHashSet();
  }

  @NotNull @Override
  public Set<AliasDesc> getClssAliasDescriptions() {
    return Sets.newHashSet(Collections2.transform(clzzCache, function));
  }

  public void initComponent() {
  }

  private void initCache() {
    PsiShortNamesCache shortNamesCache = PsiShortNamesCache.getInstance(project);
    for (String name : shortNamesCache.getAllClassNames()) {
      PsiClass[] psiClasses = shortNamesCache.getClassesByName(name, GlobalSearchScope.allScope(project));
      for (PsiClass clzz : psiClasses) {
        cacheClzzIfNeeded(clzz);
      }
    }
  }

  private void handlePsiClassChange(@NotNull PsiClass[] clzzes) {
    for (PsiClass clzz : clzzes) {
      if (!clzz.isInterface() && !clzz.isAnnotationType() && !clzz.isEnum() && clzz.isValid()) {
        handlePsiClassChange(clzz);
      }
    }
  }

  private void handlePsiClassChange(@NotNull PsiClass clzz) {
    if (JavaUtils.isAnnotationPresent(clzz, ALIAS)) {
      cacheClzzIfNeeded(clzz);
    } else {
      removeCachedClzzIfNeeded(clzz);
    }
  }

  public void cacheClzzIfNeeded(@NotNull PsiClass clzz) {
    if (JavaUtils.getAnnotationValueText(clzz, ALIAS).isPresent()) {
      clzzCache.add(clzz);
    }
  }

  public void removeCachedClzzIfNeeded(@NotNull PsiClass clzz) {
    if (!JavaUtils.getAnnotationValueText(clzz, ALIAS).isPresent()) {
      clzzCache.remove(clzz);
    }
  }

  public void disposeComponent() {
  }

  @NotNull
  public String getComponentName() {
    return "AnnotationAliasResolver";
  }

  public void projectOpened() {
    DumbService.getInstance(project).smartInvokeLater(new Runnable() {
      @Override
      public void run() {
        setupListener();
        initCache();
      }
    });
  }

  private void setupListener() {
    PsiManager.getInstance(project).addPsiTreeChangeListener(new PsiTreeChangeAdapter() {
      @Override
      public void childrenChanged(@NotNull PsiTreeChangeEvent event) {
        PsiElement element = event.getParent();
        if (element instanceof PsiJavaFile) {
          PsiClass[] classes = ((PsiJavaFile) element).getClasses();
          handlePsiClassChange(classes);
        }
      }
    });
  }

  public void projectClosed() {
    clzzCache = null;
  }
}
