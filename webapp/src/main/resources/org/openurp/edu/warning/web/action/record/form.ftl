[#ftl]
[@b.head/]
<style>
	form.listform label.title {
		width: 120px
	}
</style>
[@b.toolbar title="编辑帮扶记录"]bar.addBack();[/@]
[@b.tabs]
    [#assign sa][#if record.persisted]!update?id=${record.id!}[#else]!save?gradeWarning.id=${gradeWarning.id}[/#if][/#assign]
    [@b.form action=sa theme="list"]
        [@edu_base.semester name="record.semester.id" label="学年学期" value=currentSemester /]
				[@b.textfield name="record.name" label="记录名称" value="${record.name!}" required="true"/]
        [@b.textfield name="record.std.user.code" label="学号" value="${(gradeWarning.std.user.code)!}" disabled = "disabled"/]
        [@b.textfield name="record.std.user.name" label="姓名" value="${(gradeWarning.std.user.name)!}" disabled = "disabled"/]
        [@b.textarea name="record.description" label="记录" value="${record.description!}" required="true" rows="20" cols="100"/]
        [@b.formfoot]
            [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
        [/@]
    [/@]
[/@]
[@b.foot/]
