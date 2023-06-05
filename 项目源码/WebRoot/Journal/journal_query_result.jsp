<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/journal.css" /> 

<div id="journal_manage"></div>
<div id="journal_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="journal_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="journal_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="journal_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="journal_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="journal_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="journalQueryForm" method="post">
			日志标题：<input type="text" class="textbox" id="title" name="title" style="width:110px" />
			日志日期：<input type="text" id="journalDate" name="journalDate" class="easyui-datebox" editable="false" style="width:100px">
			学生：<input class="textbox" type="text" id="studentObj_studentNo_query" name="studentObj.studentNo" style="width: auto"/>
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="journal_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="journalEditDiv">
	<form id="journalEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">日志id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="journal_journalId_edit" name="journal.journalId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">日志标题:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="journal_title_edit" name="journal.title" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">日志内容:</span>
			<span class="inputControl">
				<textarea id="journal_content_edit" name="journal.content" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div>
			<span class="label">日志日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="journal_journalDate_edit" name="journal.journalDate" />

			</span>

		</div>
		<div>
			<span class="label">学生:</span>
			<span class="inputControl">
				<input class="textbox"  id="journal_studentObj_studentNo_edit" name="journal.studentObj.studentNo" style="width: auto"/>
			</span>
		</div>
	</form>
</div>
<script type="text/javascript" src="Journal/js/journal_manage.js"></script> 
