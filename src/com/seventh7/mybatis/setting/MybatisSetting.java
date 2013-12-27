package com.seventh7.mybatis.setting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.seventh7.mybatis.generate.GenerateModel;
import com.seventh7.mybatis.generate.StatementGenerator;

import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

import static com.seventh7.mybatis.generate.StatementGenerator.DELETE_GENERATOR;
import static com.seventh7.mybatis.generate.StatementGenerator.INSERT_GENERATOR;
import static com.seventh7.mybatis.generate.StatementGenerator.SELECT_GENERATOR;
import static com.seventh7.mybatis.generate.StatementGenerator.UPDATE_GENERATOR;

/**
 * @author yanglin
 */
@State(
    name = "MybatisSettings",
    storages = @Storage(id = "other", file = "$APP_CONFIG$/mybatis.xml"))
public class MybatisSetting implements PersistentStateComponent<Element> {

  private static final String STATEMENT_GENERATE_MODEL_SETTING_ID = "StatementGenerateModel";
  private static final String DEFAULT_SOURCE_SETTING_ID = "DefaultDataSourceId";
  private static final Type SETTING_TYPE_TOKEN = new TypeToken<Set<String>>() {}.getType();

  private GenerateModel statementGenerateModel;
  private String defaultDataSourceId = "";
  private Gson gson = new Gson();

  public MybatisSetting() {
    statementGenerateModel = GenerateModel.START_WITH_MODEL;
  }

  public static MybatisSetting getInstance() {
    return ServiceManager.getService(MybatisSetting.class);
  }

  @Nullable @Override
  public Element getState() {
    Element element = new Element("MybatisSettings");
    element.setAttribute(INSERT_GENERATOR.getId(), gson.toJson(INSERT_GENERATOR.getPatterns()));
    element.setAttribute(DELETE_GENERATOR.getId(), gson.toJson(DELETE_GENERATOR.getPatterns()));
    element.setAttribute(UPDATE_GENERATOR.getId(), gson.toJson(UPDATE_GENERATOR.getPatterns()));
    element.setAttribute(SELECT_GENERATOR.getId(), gson.toJson(SELECT_GENERATOR.getPatterns()));
    element.setAttribute(STATEMENT_GENERATE_MODEL_SETTING_ID, String.valueOf(statementGenerateModel.getIdentifier()));
    setDefaultDataSourceIdIfNull();
    element.setAttribute(DEFAULT_SOURCE_SETTING_ID, defaultDataSourceId);
    return element;
  }

  private void setDefaultDataSourceIdIfNull() {
    if (this.defaultDataSourceId == null) {
      this.defaultDataSourceId = "";
    }
  }

  @Override
  public void loadState(Element state) {
    loadState(state, INSERT_GENERATOR);
    loadState(state, DELETE_GENERATOR);
    loadState(state, UPDATE_GENERATOR);
    loadState(state, SELECT_GENERATOR);
    final String model = state.getAttributeValue(STATEMENT_GENERATE_MODEL_SETTING_ID);
    statementGenerateModel = GenerateModel.getInstance(model);
    this.defaultDataSourceId = state.getAttributeValue(DEFAULT_SOURCE_SETTING_ID);
    setDefaultDataSourceIdIfNull();
  }

  public String getDefaultDataSourceId() {
    return defaultDataSourceId;
  }

  public void setDefaultDataSourceId(String defaultDataSourceId) {
    this.defaultDataSourceId = defaultDataSourceId;
  }

  private void loadState(Element state, StatementGenerator generator) {
    String attribute = state.getAttributeValue(generator.getId());
    if (null != attribute) {
      generator.setPatterns((Set<String>) gson.fromJson(attribute, SETTING_TYPE_TOKEN));
    }
  }

  public GenerateModel getStatementGenerateModel() {
    return statementGenerateModel;
  }

  public void setStatementGenerateModel(GenerateModel statementGenerateModel) {
    this.statementGenerateModel = statementGenerateModel;
  }
}
