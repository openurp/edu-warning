[#ftl]
[@b.head/]
[@b.form name="FileListForm" method="post" action=""]
    [@b.grid items=files var="file"]
        [@b.gridbar]
					bar.addItem("查看帮扶记录", "record()");
					bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
        [/@]
        [@b.row]
            [@b.boxcol /]
            [@b.col width="20%" property="std.user.code" title="学号"/]
            [@b.col width="20%" property="std.user.name" title="姓名"/]
            [@b.col width="20%" property="std.state.squad.name" title="班级"/]
            [@b.col width="15%" property="std.state.grade" title="年级"/]
            [@b.col width="20%" property="std.state.department.name" title="院系"/]
        [/@]
    [/@]
[/@]
<script>

	function record() {
		var ids = bg.input.getCheckBoxValues("file.id");
		if (ids == null || ids == "") {
			alert("请选择记录进行操作!");
			return;
		}
		if (ids.indexOf(",") != -1) {
			alert("请选择一条记录进行操作");
			return;
		}
		bg.form.addInput(document.FileListForm, "fileIds", ids);
		bg.form.submit(document.FileListForm, "${b.url('record!search')}", "_blank");
	}
</script>
[@b.foot/]
