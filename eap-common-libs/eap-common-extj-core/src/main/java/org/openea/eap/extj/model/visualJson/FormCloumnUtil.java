package org.openea.eap.extj.model.visualJson;

import org.openea.eap.extj.model.visual.ExtnKeyConsts;
import org.openea.eap.extj.model.visualJson.analysis.*;
import org.openea.eap.extj.model.visualJson.config.ConfigModel;
import org.openea.eap.extj.model.visualJson.props.PropsBeanModel;
import org.openea.eap.extj.model.visualJson.props.PropsModel;
import org.openea.eap.extj.util.JsonUtil;
import org.openea.eap.extj.util.RandomUtil;
import org.openea.eap.extj.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 在线工作流开发
 *
 *
 */
public class FormCloumnUtil {

    /**
     * 引擎递归
     **/
    public static void recursionForm(RecursionForm recursionForm, List<FormAllModel> formAllModel) {
        List<TableModel> tableModelList = recursionForm.getTableModelList();
        List<FieLdsModel> list = recursionForm.getList();
        if(list==null) list = new ArrayList<>(0);
        for (FieLdsModel fieLdsModel : list) {
            FormAllModel start = new FormAllModel();
            FormAllModel end = new FormAllModel();
            ConfigModel config = fieLdsModel.getConfig();
            String visibility = config.getVisibility();
            multipleChoices(config);
            String ExtnKey = config.getExtnKey();
            List<FieLdsModel> childrenList = config.getChildren();
            boolean isEndExtnKey;
            if (StringUtil.isEmpty(ExtnKey)){
                isEndExtnKey = true;
            }else {
                isEndExtnKey = FormEnum.collapseItem.getMessage().equals(ExtnKey) || FormEnum.tabItem.getMessage().equals(ExtnKey);
            }
            boolean isName = StringUtil.isNotEmpty(fieLdsModel.getName());
            if (FormEnum.row.getMessage().equals(ExtnKey) || FormEnum.card.getMessage().equals(ExtnKey)
                    || FormEnum.tab.getMessage().equals(ExtnKey) || FormEnum.collapse.getMessage().equals(ExtnKey)
                    || isEndExtnKey) {
                String key = isEndExtnKey ? isName ? FormEnum.collapse.getMessage() : FormEnum.tab.getMessage() : ExtnKey;
                //布局属性
                FormModel formModel = JsonUtil.getJsonToBean(fieLdsModel, FormModel.class);
                formModel.setSpan(config.getSpan());
                formModel.setActive(config.getActive());
                formModel.setChildNum(config.getChildNum());
                formModel.setModel(config.getModel());
                formModel.setVisibility(config.getVisibility());
                String outermost = !isEndExtnKey ? "0" : "1";
                if (FormEnum.tab.getMessage().equals(key) || FormEnum.collapse.getMessage().equals(key)) {
                    if (!isEndExtnKey) {
                        String chidModel = "active" + RandomUtil.enUuid();
                        formModel.setModel(chidModel);
                        for (int i = 0; i < childrenList.size(); i++) {
                            FieLdsModel childModel = childrenList.get(i);
                            ConfigModel childConfig = childModel.getConfig();
                            childConfig.setVisibility(visibility);
                            childConfig.setModel(chidModel);
                            childConfig.setChildNum(i);
                            multipleChoices(childConfig);
                            childModel.setConfig(childConfig);
                        }
                        formModel.setChildren(childrenList);
                    }
                    formModel.setOutermost(outermost);
                }
                start.setExtnKey(key);
                start.setFormModel(formModel);
                formAllModel.add(start);
                RecursionForm recursion = new RecursionForm(childrenList, tableModelList);
                recursionForm(recursion, formAllModel);
                end.setIsEnd("1");
                end.setExtnKey(key);
                //折叠、标签的判断里层还是外层
                FormModel endFormModel = new FormModel();
                endFormModel.setOutermost(outermost);
                endFormModel.setConfig(config);
                end.setFormModel(endFormModel);
                formAllModel.add(end);
            } else if (FormEnum.table.getMessage().equals(ExtnKey)) {
                tableModel(fieLdsModel, formAllModel);
            } else if (FormEnum.isModel(ExtnKey)) {
                FormModel formModel = JsonUtil.getJsonToBean(fieLdsModel, FormModel.class);
                formModel.setVisibility(fieLdsModel.getConfig().getVisibility());
                start.setExtnKey(ExtnKey);
                start.setFormModel(formModel);
                formAllModel.add(start);
            } else {
                model(fieLdsModel, formAllModel, tableModelList);
            }
        }
        for (FormAllModel formModel : formAllModel) {
            if (FormEnum.mast.getMessage().equals(formModel.getExtnKey())) {
                setRelationFieldAttr(formAllModel, formModel.getFormColumnModel().getFieLdsModel());
            }else if(FormEnum.mastTable.getMessage().equals(formModel.getExtnKey())){
                setRelationFieldAttr( formAllModel,  formModel.getFormMastTableModel().getMastTable().getFieLdsModel());
            }

        }
    }

    /**
     * 多端选择
     * @param configModel
     * @return
     */
    private static ConfigModel multipleChoices(ConfigModel configModel){
        String visibility = configModel.getVisibility();
        if(Objects.nonNull(visibility)){
            configModel.setApp(visibility.contains("app"));
            configModel.setPc(visibility.contains("pc"));
        }
        return configModel;
    }

    /**
     * 主表属性添加
     **/
    private static void model(FieLdsModel fieLdsModel, List<FormAllModel> formAllModel, List<TableModel> tableModelList) {
        FormColumnModel mastModel = formModel(fieLdsModel);
        FormAllModel formModel = new FormAllModel();
        formModel.setExtnKey(FormEnum.mast.getMessage());
        formModel.setFormColumnModel(mastModel);
        if (tableModelList.size() > 0) {
            TableModel tableModel = tableModelList.stream().filter(t -> t.getTable().equals(fieLdsModel.getConfig().getTableName())).findFirst().orElse(null);
            if (tableModel == null) {
                Optional<TableModel> first = tableModelList.stream().filter(t -> "1".equals(t.getTypeId())).findFirst();
                if (first.isPresent()) {
                    tableModel = first.get();
                }
            }
            String type = tableModel.getTypeId();
            if ("1".equals(type)) {
                mastModel.getFieLdsModel().getConfig().setTableName(tableModel.getTable());
                formModel.setFormColumnModel(mastModel);
                formAllModel.add(formModel);
            } else {
                mastTable(tableModel, fieLdsModel, formAllModel);
            }
        } else {
            formAllModel.add(formModel);
        }
    }

    /**
     * 主表的属性是子表字段
     */
    private static void mastTable(TableModel tableModel, FieLdsModel fieLdsModel, List<FormAllModel> formAllModel) {
        FormMastTableModel childModel = new FormMastTableModel();
        String vModel = fieLdsModel.getVModel();
        List<TableFields> tableFieldsList = tableModel.getFields();
        String mastKey = "extn_" + tableModel.getTable() + "_extn_";
        TableFields tableFields = tableFieldsList.stream().filter(t -> StringUtil.isNotEmpty(vModel) && vModel.equals(mastKey + t.getField())).findFirst().orElse(null);
        FormAllModel formModel = new FormAllModel();
        formModel.setExtnKey(FormEnum.mastTable.getMessage());
        if (tableFields != null) {
            childModel.setTable(tableModel.getTable());
            formModel.setFormMastTableModel(childModel);
            childModel.setField(tableFields.getField());
            childModel.setVModel(vModel);
        }
        FormColumnModel mastTable = formModel(fieLdsModel);
        childModel.setMastTable(mastTable);
        formAllModel.add(formModel);
    }

    /**
     * 子表表属性添加
     **/
    private static void tableModel(FieLdsModel model, List<FormAllModel> formAllModel) {
        List<FormColumnModel> childList = new ArrayList<>();
        ConfigModel config = model.getConfig();
        List<FieLdsModel> childModelList = config.getChildren();
        String table = model.getVModel();
        List<String> summaryField = StringUtil.isNotEmpty(model.getSummaryField()) ? JsonUtil.getJsonToList(model.getSummaryField(), String.class) : new ArrayList<>();
        Map<String, String> summaryName = new HashMap<>();
        for (FieLdsModel childmodel : childModelList) {
            if (childmodel.getProps() != null) {
                PropsBeanModel beanModel = JsonUtil.getJsonToBean(childmodel.getProps().getProps(), PropsBeanModel.class);
                PropsModel propsModel = new PropsModel();
                propsModel.setProps(childmodel.getProps().getProps());
                propsModel.setPropsModel(beanModel);
                childmodel.setProps(propsModel);
            }
            FormColumnModel childModel = formModel(childmodel);
            boolean isSummary = summaryField.contains(childmodel.getVModel());
            if (isSummary) {
                summaryName.put(childmodel.getVModel(), childmodel.getConfig().getLabel());
            }
            relationModel(childModelList, childmodel);
            childList.add(childModel);
        }
        multipleChoices(config);
        FormColumnTableModel tableModel = JsonUtil.getJsonToBean(config,FormColumnTableModel.class);
        tableModel.setActionText(StringUtil.isNotEmpty(model.getActionText()) ? model.getActionText() : "新增");
        tableModel.setTableModel(table);
        tableModel.setChildList(childList);
        tableModel.setShowSummary(model.getShowSummary());
        tableModel.setSummaryField(JsonUtil.getObjectToString(summaryField));
        tableModel.setSummaryFieldName(JsonUtil.getObjectToString(summaryName));
        tableModel.setVisibility(config.getVisibility());
        tableModel.setAddType(model.getAddType());
        tableModel.setAddTableConf(model.getAddTableConf());
        FormAllModel formModel = new FormAllModel();
        formModel.setExtnKey(FormEnum.table.getMessage());
        formModel.setChildList(tableModel);
        formAllModel.add(formModel);
    }

    private static void relationModel(List<FieLdsModel> childModelList, FieLdsModel childmodel) {
        String ExtnKey = childmodel.getConfig().getExtnKey();
        String childRelationField = childmodel.getRelationField();
        if (FormEnum.relationFormAttr.getMessage().equals(ExtnKey) || FormEnum.popupAttr.getMessage().equals(ExtnKey)) {
            String relationField = childmodel.getRelationField().split("_extnTable_")[0];
            FieLdsModel child = childModelList.stream().filter(t -> relationField.equals(t.getVModel())).findFirst().orElse(null);
            if (child != null) {
                childmodel.setInterfaceId(child.getInterfaceId());
                childmodel.setModelId(child.getModelId());
                childmodel.setPropsValue(child.getPropsValue());
                childmodel.setRelationField(relationField);
            }
        }
        if(ExtnKeyConsts.USERSELECT.equals(ExtnKey) && StringUtil.isNotEmpty(childRelationField)){
            String[] relationField = childRelationField.split("-");
            if(relationField.length>1){
                childmodel.setRelationField(relationField[1]);
            }
        }
    }

    /**
     * 属性赋值
     **/
    private static FormColumnModel formModel(FieLdsModel model) {
        ConfigModel configModel = model.getConfig();
        multipleChoices(configModel);
        if (configModel.getDefaultValue() instanceof String) {
            configModel.setValueType("String");
        }
        if (configModel.getDefaultValue() == null) {
            configModel.setValueType("undefined");
        }
        FormColumnModel formColumnModel = new FormColumnModel();
        //级联判断多选还是单选
        if (ExtnKeyConsts.CASCADER.equals(configModel.getExtnKey())) {
            PropsBeanModel propsMap = JsonUtil.getJsonToBean(model.getProps().getProps(), PropsBeanModel.class);
            model.setMultiple(propsMap.getMultiple());
        }
        formColumnModel.setFieLdsModel(model);
        return formColumnModel;
    }

    /**
     * 判断重复子表
     *
     * @return
     */
    public static boolean repetition(RecursionForm recursionForm, List<FormAllModel> formAllModel) {
        boolean flag = false;
        List<TableModel> tableModelList = recursionForm.getTableModelList();
        recursionForm(recursionForm, formAllModel);
        if (tableModelList.size() > 0) {
            List<FormAllModel> tables = formAllModel.stream().filter(t -> FormEnum.table.getMessage().equals(t.getExtnKey())).collect(Collectors.toList());
            List<FormAllModel> mastTable = formAllModel.stream().filter(t -> FormEnum.mastTable.getMessage().equals(t.getExtnKey())).collect(Collectors.toList());
            List<String> tableList = tables.stream().map(t -> t.getChildList().getTableName()).collect(Collectors.toList());
            List<String> mastTableList = mastTable.stream().map(t -> t.getFormMastTableModel().getTable()).collect(Collectors.toList());
            flag = tableList.stream().filter(item -> mastTableList.contains(item)).count() > 0;
        }
        return flag;
    }

    /**
     * 获取关联表单字段信息
     * @param
     * @return
     */
    private static void setRelationFieldAttr( List<FormAllModel> formAllModel, FieLdsModel formModel) {
        String ExtnKey = formModel.getConfig().getExtnKey();
        if (FormEnum.relationFormAttr.getMessage().equals(ExtnKey) || FormEnum.popupAttr.getMessage().equals(ExtnKey)) {
            List<FieLdsModel> partenList = new ArrayList<>();
            partenList.addAll(formAllModel.stream().filter(t -> t.getFormColumnModel() != null).map(t -> t.getFormColumnModel().getFieLdsModel()).collect(Collectors.toList()));
            partenList.addAll(formAllModel.stream().filter(t -> t.getFormMastTableModel() != null).map(t -> t.getFormMastTableModel().getMastTable().getFieLdsModel()).collect(Collectors.toList()));
            String relationField = formModel.getRelationField().split("_extnTable_")[0];
            FieLdsModel parten = partenList.stream().filter(t -> relationField.equals(t.getVModel())).findFirst().orElse(null);
            if (parten != null) {
                formModel.setInterfaceId(parten.getInterfaceId());
                formModel.setModelId(parten.getModelId());
                formModel.setPropsValue(parten.getPropsValue());
                formModel.setRelationField(parten.getVModel());
            }
        }
    }
}
