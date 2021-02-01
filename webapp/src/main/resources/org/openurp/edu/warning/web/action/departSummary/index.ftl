[#ftl]
<style>
	.summaryTable {
		border: 1px solid #006CB2;
		border-collapse: collapse;
		font-size: 10pt;
		font-style: normal;
		vertical-align: middle;
		table-layout: fixed;
	}

	.summaryTable td {
		border: 1px solid #006CB2;
		border-collapse: collapse;
		overflow: hidden;
		word-wrap: break-word;
		padding: 2px 0px;
	}

	.summaryTable thead tr {
		background-color: #C7DBFF;
		color: #000000;
		letter-spacing: 0;
		text-decoration: none;
	}

</style>
[@b.head/]
[@b.toolbar title="预警情况汇总"/]
[@urp_base.semester_bar name="gradeWarning.semester.id" value=currentSemester /]

<table border="0" width="100%" align="center">
	<tr>
		<td align="center">
			<table name="departSummary" class="summaryTable" style="font-size:12px;font-family:宋体;vnd.ms-excel.numberformat:@"
						 width="95%">
				<thead>
				<tr align="center">
					<td rowspan="2" width="25%">学院</td>
					<td rowspan="2" width="10%">总人数</td>
					<td colspan="2" width="20%" style="color: red">红色预警</td>
					<td colspan="2" width="20%" style="color: yellow">黄色预警</td>
					<td colspan="2" width="20%">未预警</td>
				</tr>
				<tr align="center">
					<td width="10%">人数</td>
					<td width="10%">比例</td>
					<td width="10%">人数</td>
					<td width="10%">比例</td>
					<td width="10%">人数</td>
					<td width="10%">比例</td>
				</tr>
				</thead>
				<tbody>
        [#assign totalRed = 0/]
        [#assign totalYELLOW = 0/]
        [#assign totalGREEN = 0/]
        [#assign total = 0/]
        [#list departs?sort_by("code") as depart]
            [#assign departTotal= (datas.get(depart.id).get(REDID))?default(0) + (datas.get(depart.id).get(YELLOWID))?default(0) + (datas.get(depart.id).get(GREENID))?default(0) /]
            [#assign totalRed = totalRed + (datas.get(depart.id).get(REDID))?default(0)]
            [#assign totalYELLOW = totalYELLOW + (datas.get(depart.id).get(YELLOWID))?default(0)]
            [#assign totalGREEN = totalGREEN + (datas.get(depart.id).get(GREENID))?default(0)]
					<tr align="center">
						<td>
                [@b.a href="squad-summary?departId=" + depart.id + "&gradeWarning.semester.id=" + currentSemester.id title="查看班级预警情况" target="_blank"]
                    ${depart.name}
                [/@]
						</td>
						<td>${departTotal!}</td>
						<td>
                [@b.a href="depart-summary!warningList?department.id=" + depart.id + "&semester.id=" + currentSemester.id + "&warningType.id=" + REDID target="_blank"]
                    ${(datas.get(depart.id).get(REDID))!}
                [/@]
						</td>
						<td>
                [#if (datas.get(depart.id).get(REDID))?default(0) != 0]
                    ${(datas.get(depart.id).get(REDID))?default(0)/departTotal*100}%
                [/#if]
						</td>
						<td>
                [@b.a href="depart-summary!warningList?department.id=" + depart.id + "&semester.id=" + currentSemester.id + "&warningType.id=" + YELLOWID target="_blank"]
                    ${(datas.get(depart.id).get(YELLOWID))!}
                [/@]
						</td>
						<td>
                [#if (datas.get(depart.id).get(YELLOWID))?default(0) != 0]
                    ${(datas.get(depart.id).get(YELLOWID))?default(0)/departTotal*100}%
                [/#if]
						</td>
						<td>
                [@b.a href="depart-summary!warningList?department.id=" + depart.id + "&semester.id=" + currentSemester.id + "&warningType.id=" + GREENID target="_blank"]
                    ${(datas.get(depart.id).get(GREENID))!}
                [/@]
						</td>
						<td>
                [#if (datas.get(depart.id).get(GREENID))?default(0) != 0]
                    ${(datas.get(depart.id).get(GREENID))?default(0)/departTotal*100}%
                [/#if]
						</td>
					</tr>
        [/#list]
        [#assign total = totalRed + totalYELLOW + totalGREEN/]
				<tr align="center">
					<td>合计</td>
					<td>${total!}</td>
					<td>${totalRed}</td>
					<td>[#if total!=0]${totalRed/total*100}%[/#if]</td>
					<td>${totalYELLOW}</td>
					<td>[#if total!=0]${totalYELLOW/total*100}%[/#if]</td>
					<td>${totalGREEN}</td>
					<td>[#if total!=0]${totalGREEN/total*100}%[/#if]</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>
[@b.foot/]
