<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/studentTask.css" /> 

<div id="studentTask_manage"></div>
<div id="studentTask_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="studentTask_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="studentTask_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="studentTask_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="studentTask_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="studentTask_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="studentTaskQueryForm" method="post">
			作业标题：<input type="text" class="textbox" id="title" name="title" style="width:110px" />
			作业学生：<input class="textbox" type="text" id="studentObj_studentNo_query" name="studentObj.studentNo" style="width: auto"/>
			作业发布日期：<input type="text" id="taskDate" name="taskDate" class="easyui-datebox" editable="false" style="width:100px">
			负责的老师：<input class="textbox" type="text" id="chargeObj_chargeUserName_query" name="chargeObj.chargeUserName" style="width: auto"/>
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="studentTask_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="studentTaskEditDiv">
	<form id="studentTaskEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">作业id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="studentTask_taskId_edit" name="studentTask.taskId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">作业标题:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="studentTask_title_edit" name="studentTask.title" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">作业内容:</span>
			<span class="inputControl">
				<textarea id="studentTask_content_edit" name="studentTask.content" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div>
			<span class="label">作业学生:</span>
			<span class="inputControl">
				<input class="textbox"  id="studentTask_studentObj_studentNo_edit" name="studentTask.studentObj.studentNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">作业文件:</span>
			<span class="inputControl">
				<a id="studentTask_taskFileA"/><br/>
    			<input type="hidden" id="studentTask_taskFile" name="studentTask.taskFile"/>
				<input id="taskFileFile" name="taskFileFile" type="file" size="50" />
			</span>
		</div>
		<div>
			<span class="label">作业发布日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="studentTask_taskDate_edit" name="studentTask.taskDate" />

			</span>

		</div>
		<div>
			<span class="label">负责的老师:</span>
			<span class="inputControl">
				<input class="textbox"  id="studentTask_chargeObj_chargeUserName_edit" name="studentTask.chargeObj.chargeUserName" style="width: auto"/>
			</span>
		</div>
	</form>
</div>
<script type="text/javascript" src="StudentTask/js/studentTask_manage.js"></script> 
