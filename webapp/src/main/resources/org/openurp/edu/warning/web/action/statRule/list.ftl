[#ftl]
[@b.head/]
[@b.grid items=statRules var="statRule"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="25%" property="name" title="名称"/]
    [@b.col width="20%" property="method" title="统计办法"]${(statRule.method.name)!}[/@]
    [@b.col width="15%" property="minValue" title="不及格学分下限"/]
    [@b.col width="15%" property="maxValue" title="不及格学分上限"/]
    [@b.col width="20%" property="warningType" title="预警类别"]${statRule.warningType.name}[/@]
  [/@]
[/@]
[@b.foot/]
