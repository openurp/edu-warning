[#ftl]
[@b.head/]
[@b.toolbar title="电子档案"/]
<div class="search-container">
  <div class="search-panel">
    [@b.form name="fileSearchForm" action="!search" target="filelist" title="ui.searchForm" theme="search"]
      [@b.textfield label="姓名" name="electronicFile.std.user.name" value="" /]
      [@b.textfield label="学号" name="electronicFile.std.user.code" value="" /]
      [@b.select name="electronicFile.std.state.department.id" label="院系" items=departments empty="..."/]
      [@b.textfield label="班级" name="electronicFile.std.state.squad.name" value="" /]
      <input type="hidden" name="orderBy" value="std.user.code"/>
    [/@]
  </div>
  <div class="search-list">[@b.div id="filelist" href="!search?orderBy=std.user.code"/]
  </div>
</div>
[@b.foot/]
