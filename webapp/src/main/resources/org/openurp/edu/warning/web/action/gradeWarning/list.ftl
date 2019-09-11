[#ftl]
[@b.head/]

[@b.form name="gradeWarningListForm" method="post" action=""]
    [@b.grid items=gradeWarnings var="gradeWarning"]
        [@b.gridbar]
					bar.addItem("自动统计", "autoStat()");
					bar.addItem("查看不及格课程信息", "courseGradeInfo()");
					bar.addItem("查看帮扶记录", "record()");
        [/@]
        [@b.row]
            [@b.boxcol /]
            [@b.col width="10%" property="std.user.code" title="学号"/]
            [@b.col width="8%" property="std.user.name" title="姓名"/]
            [@b.col width="15%" property="std.state.squad.name" title="班级"/]
            [@b.col width="7%" property="std.state.grade" title="年级"/]
            [@b.col width="10%" property="std.state.department.name" title="院系"/]
            [@b.col width="10%" property="warningType" title="预警类别"]${gradeWarning.warningType.name}[/@]
            [@b.col width="35%" property="detail" title="预警情况说明" style="white-space:pre"/]
        [/@]
    [/@]
[/@]
<script>
	function autoStat() {
		var ids = bg.input.getCheckBoxValues("gradeWarning.id");
		if (ids == null || ids == "") {
			alert("请选择记录进行操作!");
			return;
		}
		bg.form.addInput(document.gradeWarningListForm, "gradeWarningIds", ids);
		bg.form.submit(document.gradeWarningListForm, "${b.url('!autoStat')}");
	}

	function courseGradeInfo() {
		var ids = bg.input.getCheckBoxValues("gradeWarning.id");
		if (ids == null || ids == "") {
			alert("请选择记录进行操作!");
			return;
		}
		if (ids.indexOf(",") != -1) {
			alert("请选择一条记录进行操作");
			return;
		}
		bg.form.addInput(document.gradeWarningListForm, "gradeWarningIds", ids);
		bg.form.submit(document.gradeWarningListForm, "${b.url('!courseGradeInfo')}");
	}


	function record() {
		var ids = bg.input.getCheckBoxValues("gradeWarning.id");
		if (ids == null || ids == "") {
			alert("请选择记录进行操作!");
			return;
		}
		if (ids.indexOf(",") != -1) {
			alert("请选择一条记录进行操作");
			return;
		}
		bg.form.addInput(document.gradeWarningListForm, "gradeWarningIds", ids);
		bg.form.submit(document.gradeWarningListForm, "${b.url('record!search')}", "_blank");
	}
</script>
[@b.foot/]
