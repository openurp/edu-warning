[#ftl]
[@b.head/]
[@b.form name="indexForm" action="" /]
[@b.toolbar title='学生预警情况']
	bar.addItem("汇总", "summary()");

	function summary() {
	bg.form.submit(document.indexForm,  "${b.url("depart-summary")}", "_blank");
	}
[/@]
<div class="search-container">
	<div class="search-panel">
        [@b.form action="!search" name="gradeWarningSearchForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@urp_base.semester name="gradeWarning.semester.id" label="学年学期" value=currentSemester /]
            [@b.textfield label="学号" name="gradeWarning.std.user.code" value="" /]
            [@b.textfield label="姓名" name="gradeWarning.std.user.name" value="" /]
            [@b.textfield label="年级" name="gradeWarning.std.state.grade" value="" /]
            [@b.select name="gradeWarning.std.state.department.id" label="院系" items=departments empty="..."/]
            [@b.textfield label="班级" name="gradeWarning.std.state.squad.name" value="" /]
            [@b.select name="gradeWarning.warningType.id" label="预警类型" items=warningTypes empty="..."/]
            [@b.select name="isGreen" items={} label="是否预警"]
							<option value="">...</option>
							<option value="0" selected="selected">有预警</option>
							<option value="1">未预警</option>
            [/@]
					<input type="hidden" name="orderBy" value="gradeWarning.std.user.code"/>
        [/@]
	</div>
	<div class="search-list">
        [@b.div id="contentDiv" href="!search?orderBy=gradeWarning.std.user.code & gradeWarning.semester.id="+currentSemester.id + "&isGreen=0" /]
	</div>
</div>
[@b.foot/]