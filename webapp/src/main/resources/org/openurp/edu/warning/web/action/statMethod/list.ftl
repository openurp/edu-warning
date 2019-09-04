[#ftl]
[@b.head/]
[@b.grid items=statMethods var="statMethod"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="25%" property="name" title="名称"/]
    [@b.col width="25%" property="description" title="办法描述"/]
    [@b.col width="25%" property="serviceName" title="办法服务名"/]
    [@b.col title="是否启用" property="enabled" width="20%"]${statMethod.enabled?string("是","否")}[/@]
  [/@]
[/@]
[@b.foot/]
