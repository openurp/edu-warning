[#ftl]
[@b.head/]
[@b.toolbar title="电子档案"/]
<table class="indexpanel">
  <tr>
    <td class="index_view" >
    [@b.form name="fileSearchForm" action="!search" target="filelist" title="ui.searchForm" theme="search"]
      [@b.textfield label="姓名" name="file.std.user.name" value="" /]
      [@b.textfield label="学号" name="file.std.user.code" value="" /]
      [@b.select name="file.std.state.department.id" label="院系" items=departments empty="..."/]
      [@b.textfield label="班级" name="file.std.state.squad.name" value="" /]
      <input type="hidden" name="orderBy" value="std.user.code"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="filelist" href="!search?orderBy=std.user.code"/]
    </td>
  </tr>
</table>
[@b.foot/]
