[#ftl]
[@b.head/]
[@b.form name="indexForm" action="" /]
[@b.toolbar title='课程预警情况'/]
<div class="search-container">
	<div class="search-panel">
      [@b.form action="!search" name="courseWarningSearchForm" title="ui.searchForm" target="contentDiv" theme="search"]
          [@urp_base.semester name="courseWarning.semester.id" label="学年学期" value=currentSemester  /]
          [@b.textfield name="courseWarning.course.code" label="课程代码" maxlength="32"/]
          [@b.textfield name="courseWarning.course.name" label="课程名称" maxlength="32"/]
          [@b.select name="courseWarning.course.department.id" label="院系" items=departments empty="..."/]
				<input type="hidden" name="orderBy" value="courseWarning.count desc"/>
				<input type="hidden" name="courseWarning.semester.id" value="${currentSemester.id}"/>
      [/@]
	</div>
	<div class="search-list">
      [@b.div id="contentDiv" href="!search?orderBy=courseWarning.count desc & courseWarning.semester.id="+currentSemester.id/]
	</div>
</div>
[@b.foot/]