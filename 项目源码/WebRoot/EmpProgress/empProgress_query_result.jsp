<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/empProgress.css" /> 

<div id="empProgress_manage"></div>
<div id="empProgress_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="empProgress_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="empProgress_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="empProgress_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="empProgress_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="empProgress_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="empProgressQueryForm" method="post">
			所在作业：<input class="textbox" type="text" id="taskObj_taskId_query" name="taskObj.taskId" style="width: auto"/>
			上传的学生：<input class="textbox" type="text" id="studentObj_studentNo_query" name="studentObj.studentNo" style="width: auto"/>
			上传时间：<input type="text" class="textbox" id="addTime" name="addTime" style="width:110px" />
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="empProgress_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="empProgressEditDiv">
	<form id="empProgressEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">进度id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="empProgress_progressId_edit" name="empProgress.progressId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">所在作业:</span>
			<span class="inputControl">
				<input class="textbox"  id="empProgress_taskObj_taskId_edit" name="empProgress.taskObj.taskId" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">上传进度文件:</span>
			<span class="inputControl">
				<a id="empProgress_progressFileA"/><br/>
    			<input type="hidden" id="empProgress_progressFile" name="empProgress.progressFile"/>
				<input id="progressFileFile" name="progressFileFile" type="file" size="50" />
			</span>
		</div>
		<div>
			<span class="label">上传的学生:</span>
			<span class="inputControl">
				<input class="textbox"  id="empProgress_studentObj_studentNo_edit" name="empProgress.studentObj.studentNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">上传时间:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="empProgress_addTime_edit" name="empProgress.addTime" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">评价:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="empProgress_evaluate_edit" name="empProgress.evaluate" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">评语:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="empProgress_evalateContent_edit" name="empProgress.evalateContent" style="width:200px" />

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="EmpProgress/js/empProgress_manage.js"></script> 
