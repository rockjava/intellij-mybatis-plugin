package com.seventh7.mybatis.reference;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.ReferenceSetBase;
import com.seventh7.mybatis.util.LongStoryUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author yanglin
 */
public abstract class ContextReferenceSetResolver<F extends PsiElement, K extends PsiElement> implements ReferenceSetResolver{

  private Splitter splitter = Splitter.on(ReferenceSetBase.DOT_SEPARATOR);

  protected Project project;

  protected F element;

  protected ContextReferenceSetResolver(@NotNull F element) {
    this.element = element;
    this.project = element.getProject();
  }

  @NotNull @Override
  public Optional<? extends PsiElement> resolve(TextRange range, int index) {
    List<String> texts = Lists.newArrayList(splitter.split(LongStoryUtils.clearDummyIdentifier(getText())));
    Optional<K>  startElement = getStartElement(Iterables.getFirst(texts, null));
    if (!startElement.isPresent()) {
      return Optional.absent();
    }
    return texts.size() > 1 ? parseNext(startElement, texts, index) : startElement;
  }

  private Optional<K> parseNext(Optional<K> current, List<String> texts, int index) {
    int ind = 1;
    while (current.isPresent() && ind <= index) {
      String text = texts.get(ind);
      if (text.contains(" ")) {
        return Optional.absent();
      }
      current = resolve(current.get(), text);
      ind++;
    }
    return current;
  }

  @NotNull @Override
  public List<String> getCompletions() {
    return Collections.emptyList();
  }

  @NotNull
  public abstract Optional<K> getStartElement(@Nullable String firstText);

  @NotNull
  public abstract String getText();

  @NotNull
  public abstract Optional<K> resolve(@NotNull K current, @NotNull String text);

  public F getElement() {
    return element;
  }

  public void setElement(F element) {
    this.element = element;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
