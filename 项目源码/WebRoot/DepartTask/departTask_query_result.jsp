<%@ page language="java"  contentType="text/html;charset=UTF-8"%>
<jsp:include page="../check_logstate.jsp"/> 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/departTask.css" /> 

<div id="departTask_manage"></div>
<div id="departTask_manage_tool" style="padding:5px;">
	<div style="margin-bottom:5px;">
		<a href="#" class="easyui-linkbutton" iconCls="icon-edit-new" plain="true" onclick="departTask_manage_tool.edit();">修改</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-delete-new" plain="true" onclick="departTask_manage_tool.remove();">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-reload" plain="true"  onclick="departTask_manage_tool.reload();">刷新</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="departTask_manage_tool.redo();">取消选择</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-export" plain="true" onclick="departTask_manage_tool.exportExcel();">导出到excel</a>
	</div>
	<div style="padding:0 0 0 7px;color:#333;">
		<form id="departTaskQueryForm" method="post">
			作业班级：<input class="textbox" type="text" id="departmentObj_departmentNo_query" name="departmentObj.departmentNo" style="width: auto"/>
			作业标题：<input type="text" class="textbox" id="title" name="title" style="width:110px" />
			发布日期：<input type="text" id="publishDate" name="publishDate" class="easyui-datebox" editable="false" style="width:100px">
			执行状态：<input type="text" class="textbox" id="executeState" name="executeState" style="width:110px" />
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onclick="departTask_manage_tool.search();">查询</a>
		</form>	
	</div>
</div>

<div id="departTaskEditDiv">
	<form id="departTaskEditForm" enctype="multipart/form-data"  method="post">
		<div>
			<span class="label">作业id:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="departTask_taskId_edit" name="departTask.taskId" style="width:200px" />
			</span>
		</div>
		<div>
			<span class="label">作业班级:</span>
			<span class="inputControl">
				<input class="textbox"  id="departTask_departmentObj_departmentNo_edit" name="departTask.departmentObj.departmentNo" style="width: auto"/>
			</span>
		</div>
		<div>
			<span class="label">作业标题:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="departTask_title_edit" name="departTask.title" style="width:200px" />

			</span>

		</div>
		<div>
			<span class="label">作业内容:</span>
			<span class="inputControl">
				<textarea id="departTask_content_edit" name="departTask.content" rows="8" cols="60"></textarea>

			</span>

		</div>
		<div>
			<span class="label">发布日期:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="departTask_publishDate_edit" name="departTask.publishDate" />

			</span>

		</div>
		<div>
			<span class="label">执行状态:</span>
			<span class="inputControl">
				<select  id="departTask_executeState_edit" name="departTask.executeState">
					<option value="执行中">执行中</option>
					<option value="执行完毕">执行完毕</option>
				</select>
			</span>

		</div>
		<div>
			<span class="label">进度评价:</span>
			<span class="inputControl">
				<input class="textbox" type="text" id="departTask_evaluate_edit" name="departTask.evaluate" style="width:200px" />

			</span>

		</div>
	</form>
</div>
<script type="text/javascript" src="DepartTask/js/departTask_manage.js"></script> 
