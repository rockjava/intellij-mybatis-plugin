package com.seventh7.mybatis.generate;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.xml.XmlTag;
import com.seventh7.mybatis.dom.model.GroupTwo;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.service.EditorService;
import com.seventh7.mybatis.setting.MybatisSetting;
import com.seventh7.mybatis.ui.ListSelectionListener;
import com.seventh7.mybatis.ui.UiComponentFacade;
import com.seventh7.mybatis.util.CollectionUtils;
import com.seventh7.mybatis.util.JavaUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * @author yanglin
 */
public abstract class StatementGenerator {

  public static final StatementGenerator UPDATE_GENERATOR = new UpdateGenerator("update", "modify", "set");

  public static final StatementGenerator SELECT_GENERATOR = new SelectGenerator("select", "get", "look", "find", "list", "search", "count", "query");

  public static final StatementGenerator DELETE_GENERATOR = new DeleteGenerator("del", "cancel");

  public static final StatementGenerator INSERT_GENERATOR = new InsertGenerator("insert", "add", "new");

  public static final StatementGenerator[] ALL = {UPDATE_GENERATOR, SELECT_GENERATOR, DELETE_GENERATOR, INSERT_GENERATOR};

  public static Optional<PsiClass> getSelectResultType(@Nullable PsiMethod method) {
    if (null == method) {
      return Optional.absent();
    }
    PsiType returnType = method.getReturnType();
    if (returnType instanceof PsiPrimitiveType && returnType != PsiType.VOID) {
      return JavaUtils.findClzz(method.getProject(), ((PsiPrimitiveType) returnType).getBoxedTypeName());
    } else if (returnType instanceof PsiClassReferenceType) {
      PsiClassReferenceType type = (PsiClassReferenceType)returnType;
      if (type.hasParameters()) {
        PsiType[] parameters = type.getParameters();
        if (parameters.length == 1) {
          type = (PsiClassReferenceType)parameters[0];
        }
      }
      return Optional.fromNullable(type.resolve());
    }
    return Optional.absent();
  }

  public static void applyGenerate(@Nullable final PsiMethod method) {
    if (null == method) return;
    final StatementGenerator[] generators = getGenerators(method);
    if (1 == generators.length) {
      generators[0].execute(method);
    } else {
      UiComponentFacade.getInstance(method.getProject()).showListPopup("[ Select target statement ]", new ListSelectionListener() {
        @Override
        public void selected(int index) {
          generators[index].execute(method);
        }

        @Override
        public boolean isWriteAction() {
          return true;
        }

      }, generators);
    }
  }

  @NotNull
  public static StatementGenerator[] getGenerators(@NotNull PsiMethod method) {
    GenerateModel model = MybatisSetting.getInstance().getStatementGenerateModel();
    String target = method.getName();
    List<StatementGenerator> result = Lists.newArrayList();
    for (StatementGenerator generator : ALL) {
      if (model.matchsAny(generator.getPatterns(), target)) {
        result.add(generator);
      }
    }
    return CollectionUtils.isNotEmpty(result) ? result.toArray(new StatementGenerator[result.size()]) : ALL;
  }

  private Set<String> patterns;

  public StatementGenerator(@NotNull String... patterns) {
    this.patterns = Sets.newHashSet(patterns);
  }

  public void execute(@NotNull PsiMethod method) {
    Optional<Mapper> mapper = MapperUtils.findFirstMapper(method.getProject(), method.getContainingClass());
    if (mapper.isPresent()) {
      setupTag(method, mapper);
    }
  }

  private void setupTag(PsiMethod method, Optional<Mapper> mapper) {
    GroupTwo target = getTarget(mapper.get(), method);
    target.getId().setStringValue(method.getName());
    target.setValue(" ");
    XmlTag tag = target.getXmlTag();
    int offset = tag.getTextOffset() + tag.getTextLength() - tag.getName().length() + 1;
    EditorService editorService = EditorService.getInstance(method.getProject());
    editorService.format(tag.getContainingFile(), tag);
    editorService.scrollTo(tag, offset);
  }

  @Override
  public String toString() {
    return this.getDisplayText();
  }

  @NotNull
  protected abstract GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method);

  @NotNull
  public abstract String getId();

  @NotNull
  public abstract String getDisplayText();

  public Set<String> getPatterns() {
    return patterns;
  }

  public void setPatterns(Set<String> patterns) {
    this.patterns = patterns;
  }

}
