var departTask_manage_tool = null; 
$(function () { 
	initDepartTaskManageTool(); //建立DepartTask管理对象
	departTask_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#departTask_manage").datagrid({
		url : 'DepartTask/DepartTask_ajaxQueryDepartTask.action',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "taskId",
		sortOrder : "desc",
		toolbar : "#departTask_manage_tool",
		columns : [[
			{
				field : "taskId",
				title : "作业id",
				width : 70,
			},
			{
				field : "departmentObj",
				title : "作业班级",
				width : 140,
			},
			{
				field : "title",
				title : "作业标题",
				width : 140,
			},
			{
				field : "publishDate",
				title : "发布日期",
				width : 140,
			},
			{
				field : "executeState",
				title : "执行状态",
				width : 140,
			},
			{
				field : "evaluate",
				title : "进度评价",
				width : 140,
			},
		]],
	});

	$("#departTaskEditDiv").dialog({
		title : "修改管理",
		top: "50px",
		width : 700,
		height : 515,
		modal : true,
		closed : true,
		iconCls : "icon-edit-new",
		buttons : [{
			text : "提交",
			iconCls : "icon-edit-new",
			handler : function () {
				if ($("#departTaskEditForm").form("validate")) {
					//验证表单 
					if(!$("#departTaskEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#departTaskEditForm").form({
						    url:"DepartTask/DepartTask_ajaxModifyDepartTask.action",
						    onSubmit: function(){
								if($("#departTaskEditForm").form("validate"))  {
				                	$.messager.progress({
										text : "正在提交数据中...",
									});
				                	return true;
				                } else { 
				                    return false; 
				                }
						    },
						    success:function(data){
						    	$.messager.progress("close");
						    	console.log(data);
			                	var obj = jQuery.parseJSON(data);
			                    if(obj.success){
			                        $.messager.alert("消息","信息修改成功！");
			                        $("#departTaskEditDiv").dialog("close");
			                        departTask_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#departTaskEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#departTaskEditDiv").dialog("close");
				$("#departTaskEditForm").form("reset"); 
			},
		}],
	});
});

function initDepartTaskManageTool() {
	departTask_manage_tool = {
		init: function() {
			$.ajax({
				url : "Department/Department_ajaxQueryAllDepartment.action",
				type : "post",
				success : function (data, response, status) {
					$("#departmentObj_departmentNo_query").combobox({ 
					    valueField:"departmentNo",
					    textField:"departmentName",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{departmentNo:"",departmentName:"不限制"});
					$("#departmentObj_departmentNo_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#departTask_manage").datagrid("reload");
		},
		redo : function () {
			$("#departTask_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#departTask_manage").datagrid("options").queryParams;
			queryParams["departmentObj.departmentNo"] = $("#departmentObj_departmentNo_query").combobox("getValue");
			queryParams["title"] = $("#title").val();
			queryParams["publishDate"] = $("#publishDate").datebox("getValue"); 
			queryParams["executeState"] = $("#executeState").val();
			$("#departTask_manage").datagrid("options").queryParams=queryParams; 
			$("#departTask_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#departTaskQueryForm").form({
			    url:"DepartTask/DepartTask_queryDepartTaskOutputToExcel.action",
			});
			//提交表单
			$("#departTaskQueryForm").submit();
		},
		remove : function () {
			var rows = $("#departTask_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var taskIds = [];
						for (var i = 0; i < rows.length; i ++) {
							taskIds.push(rows[i].taskId);
						}
						$.ajax({
							type : "POST",
							url : "DepartTask/DepartTask_ajaxDeleteDepartTask.action",
							data : {
								taskIds : taskIds.join(","),
							},
							beforeSend : function () {
								$("#departTask_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#departTask_manage").datagrid("loaded");
									$("#departTask_manage").datagrid("load");
									$("#departTask_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#departTask_manage").datagrid("loaded");
									$("#departTask_manage").datagrid("load");
									$("#departTask_manage").datagrid("unselectAll");
									$.messager.alert("消息",data.message);
								}
							},
						});
					}
				});
			} else {
				$.messager.alert("提示", "请选择要删除的记录！", "info");
			}
		},
		edit : function () {
			var rows = $("#departTask_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "DepartTask/DepartTask_ajaxModifyDepartTaskQuery.action",
					type : "post",
					data : {
						taskId : rows[0].taskId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (departTask, response, status) {
						$.messager.progress("close");
						if (departTask) { 
							$("#departTaskEditDiv").dialog("open");
							$("#departTask_taskId_edit").val(departTask.taskId);
							$("#departTask_taskId_edit").validatebox({
								required : true,
								missingMessage : "请输入作业id",
								editable: false
							});
							$("#departTask_departmentObj_departmentNo_edit").combobox({
								url:"Department/Department_ajaxQueryAllDepartment.action",
							    valueField:"departmentNo",
							    textField:"departmentName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#departTask_departmentObj_departmentNo_edit").combobox("select", departTask.departmentObjPri);
									//var data = $("#departTask_departmentObj_departmentNo_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#departTask_departmentObj_departmentNo_edit").combobox("select", data[0].departmentNo);
						            //}
								}
							});
							$("#departTask_title_edit").val(departTask.title);
							$("#departTask_title_edit").validatebox({
								required : true,
								missingMessage : "请输入作业标题",
							});
							$("#departTask_content_edit").val(departTask.content);
							$("#departTask_content_edit").validatebox({
								required : true,
								missingMessage : "请输入作业内容",
							});
							$("#departTask_publishDate_edit").datebox({
								value: departTask.publishDate,
							    required: true,
							    showSeconds: true,
							});
							$("#departTask_executeState_edit").val(departTask.executeState);
							$("#departTask_executeState_edit").validatebox({
								required : true,
								missingMessage : "请输入执行状态",
							});
							$("#departTask_evaluate_edit").val(departTask.evaluate);
							$("#departTask_evaluate_edit").validatebox({
								required : true,
								missingMessage : "请输入进度评价",
							});
						} else {
							$.messager.alert("获取失败！", "未知错误导致失败，请重试！", "warning");
						}
					}
				});
			} else if (rows.length == 0) {
				$.messager.alert("警告操作！", "编辑记录至少选定一条数据！", "warning");
			}
		},
	};
}
