package com.seventh7.mybatis.generate;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import com.seventh7.mybatis.dom.model.GroupTwo;
import com.seventh7.mybatis.dom.model.Mapper;
import com.seventh7.mybatis.service.EditorService;
import com.seventh7.mybatis.setting.MybatisSetting;
import com.seventh7.mybatis.util.CollectionUtils;
import com.seventh7.mybatis.util.MapperUtils;

import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * @author yanglin
 */
public abstract class StatementGenerator {

  public static final StatementGenerator UPDATE_GENERATOR = new UpdateGenerator("update", "modify", "set");

  public static final StatementGenerator SELECT_GENERATOR = new SelectGenerator("select", "get", "look", "find", "list", "search", "count");

  public static final StatementGenerator DELETE_GENERATOR = new DeleteGenerator("delete", "del", "cancel");

  public static final StatementGenerator INSERT_GENERATOR = new InsertGenerator("insert", "add", "new");

  public static final StatementGenerator[] ALL = {UPDATE_GENERATOR, SELECT_GENERATOR, DELETE_GENERATOR, INSERT_GENERATOR};

  private static final GenerateModel MODEL = MybatisSetting.getInstance().getStatementGenerateModel();

  @NotNull
  public static final StatementGenerator[] getGenerators(@NotNull PsiMethod method) {
    String target = method.getName();
    List<StatementGenerator> result = Lists.newArrayList();
    for (StatementGenerator generator : ALL) {
      if (MODEL.matchAny(generator.getPatterns(), target)) {
        result.add(generator);
      }
    }
    return CollectionUtils.isNotEmpty(result) ? result.toArray(new StatementGenerator[result.size()]) : ALL;
  }

  private Set<String> patterns;

  public StatementGenerator(String pattern, String... patterns) {
    this.patterns = Sets.newHashSet(pattern);
    if (!ArrayUtils.isEmpty(patterns)) {
      this.patterns.addAll(Lists.newArrayList(patterns));
    }
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
    int offset = (tag.getTextOffset() + tag.getTextLength()) - tag.getName().length() + 1;
    EditorService editorService = EditorService.getInstance(method.getProject());
    editorService.format(tag);
    editorService.scrollTo(tag, offset);
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
