[#ftl]
[@b.head/]
[@b.form name="indexForm" action="" /]
[@b.toolbar title='课程预警情况'/]
<table class="indexpanel">
	<tr>
		<td class="index_view" style="width:180px">
        [@b.form action="!search" name="courseWarningSearchForm" title="ui.searchForm" target="contentDiv" theme="search"]
            [@edu_base.semester name="courseWarning.semester.id" label="学年学期" value=currentSemester /]
            [@b.textfield name="courseWarning.course.code" label="课程代码" maxlength="32"/]
            [@b.textfield name="courseWarning.course.name" label="课程名称" maxlength="32"/]
            [@b.select name="courseWarning.course.department.id" label="院系" items=departments empty="..."/]
        [/@]
		</td>
		<td class="index_content">
        [@b.div id="contentDiv" href="!search?orderBy=courseWarning.count desc & courseWarning.semester.id="+currentSemester.id/]
		</td>
	</tr>
</table>
[@b.foot/]