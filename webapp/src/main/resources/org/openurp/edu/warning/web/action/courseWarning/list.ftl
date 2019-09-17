[#ftl]
[@b.head/]

[@b.form name="courseWarningListForm" method="post" action=""]
    [@b.grid items=courseWarnings var="courseWarning"]
        [@b.gridbar]
					bar.addItem("统计", "stat()");
					bar.addItem("${b.text('action.export')}", "exportData()");
					function exportData(){
					var form = document.courseWarningSearchForm;
					bg.form.addInput(form, "keys", "semester.code,course.code,course.name,course.courseType.name,course.department.name,count");
					bg.form.addInput(form, "titles", "学年学期,课程代码,课程名称,课程类别,所属院系,不及格人数");
					bg.form.addInput(form, "fileName", "课程预警情况");
					bg.form.submit(form, "${b.url('!export')}","_self");
					}
        [/@]
        [@b.row]
            [@b.boxcol /]
            [@b.col width="20%" property="course.code" title="课程代码" /]
            [@b.col width="20%" property="course.name" title="课程名称"/]
            [@b.col width="20%" property="course.courseType.name" title="课程类别"/]
            [@b.col width="20%" property="course.department.name" title="所属院系"/]
            [@b.col width="15%" property="count" title="不及格人数" /]
        [/@]
    [/@]
[/@]

<script>
	function stat() {
		var count = prompt("请填写统计课程不及格人数下限", "");
		if (count) {
			bg.form.submit(document.courseWarningSearchForm, "${b.url('!stat')}" + "?count=" + count + "&semester.id=" + ${semester.id});
		}
	}
</script>
[@b.foot/]
