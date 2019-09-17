[#ftl]
[@b.head/]

[@b.form name="gradeWarningListForm" method="post" action=""]
    [@b.grid items=gradeWarnings var="gradeWarning"]
        [@b.gridbar]
					bar.addItem("自动统计", "autoStat()");
					bar.addItem("查看不及格课程信息", "courseGradeInfo()");
					bar.addItem("查看帮扶记录", "record()");
					bar.addItem("${b.text('action.export')}", "exportData()");
					function exportData(){
					var form = document.courseEvaluateStatSearchForm;
					bg.form.addInput(form, "keys", "semester.code,std.user.code,std.user.name,std.state.squad.name,std.state.grade,std.state.department.name,std.state.major.name,std.state.direction.name,warningType.name,detail");
					bg.form.addInput(form, "titles", "学年学期,学号,姓名,班级,年级,院系,专业,方向,预警类型,情况说明");
					bg.form.addInput(form, "fileName", "学业预警详细名单");
					bg.form.submit(form, "${b.url('!export')}","_self");
					}
        [/@]
        [@b.row]
            [@b.boxcol /]
            [@b.col width="10%" property="std.user.code" title="学号"/]
            [@b.col width="8%" property="std.user.name" title="姓名"/]
            [@b.col width="15%" property="std.state.squad.name" title="班级"/]
            [@b.col width="7%" property="std.state.grade" title="年级"/]
            [@b.col width="10%" property="std.state.department.name" title="院系"/]
            [@b.col width="10%" property="warningType.name" title="预警类别"/]
            [@b.col width="35%" property="detail" title="情况说明" style="white-space:pre"/]
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
