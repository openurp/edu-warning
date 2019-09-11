[#ftl]
[@b.head/]
[@b.toolbar title="帮扶记录信息"]
	bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
	<tr>
		<td class="title" width="20%">学年学期</td>
		<td class="content">${record.semester.schoolYear}年${record.semester.name}学期</td>
	</tr>
	<tr>
		<td class="title" width="20%">学号</td>
		<td class="content">${record.file.std.user.code}</td>
	</tr>
	<tr>
		<td class="title" width="20%">姓名</td>
		<td class="content">${record.file.std.user.name}</td>
	</tr>
	<tr>
		<td class="title" width="20%">记录</td>
		<td class="content">${record.description}</td>
	</tr>
</table>

[@b.foot/]
