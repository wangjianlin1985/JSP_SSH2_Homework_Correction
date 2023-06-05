var empProgress_manage_tool = null; 
$(function () { 
	initEmpProgressManageTool(); //建立EmpProgress管理对象
	empProgress_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#empProgress_manage").datagrid({
		url : 'EmpProgress/EmpProgress_ajaxQueryEmpProgress.action',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "progressId",
		sortOrder : "desc",
		toolbar : "#empProgress_manage_tool",
		columns : [[
			{
				field : "progressId",
				title : "进度id",
				width : 70,
			},
			{
				field : "taskObj",
				title : "所在作业",
				width : 140,
			},
			{
				field : "progressFile",
				title : "上传进度文件",
				width : "400px",
				height: "100px",
				formatter: function(val,row) {
					return "<a href='" + val + "' target='_blank'><font color=red>" + val +　"</font></a>";
				}
 			},
			{
				field : "studentObj",
				title : "上传的学生",
				width : 140,
			},
			{
				field : "addTime",
				title : "上传时间",
				width : 140,
			},
			{
				field : "evaluate",
				title : "评价",
				width : 140,
			},
		]],
	});

	$("#empProgressEditDiv").dialog({
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
				if ($("#empProgressEditForm").form("validate")) {
					//验证表单 
					if(!$("#empProgressEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#empProgressEditForm").form({
						    url:"EmpProgress/EmpProgress_ajaxModifyEmpProgress.action",
						    onSubmit: function(){
								if($("#empProgressEditForm").form("validate"))  {
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
			                        $("#empProgressEditDiv").dialog("close");
			                        empProgress_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#empProgressEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#empProgressEditDiv").dialog("close");
				$("#empProgressEditForm").form("reset"); 
			},
		}],
	});
});

function initEmpProgressManageTool() {
	empProgress_manage_tool = {
		init: function() {
			$.ajax({
				url : "StudentTask/StudentTask_ajaxQueryAllStudentTask.action",
				type : "post",
				success : function (data, response, status) {
					$("#taskObj_taskId_query").combobox({ 
					    valueField:"taskId",
					    textField:"title",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{taskId:0,title:"不限制"});
					$("#taskObj_taskId_query").combobox("loadData",data); 
				}
			});
			$.ajax({
				url : "Student/Student_ajaxQueryAllStudent.action",
				type : "post",
				success : function (data, response, status) {
					$("#studentObj_studentNo_query").combobox({ 
					    valueField:"studentNo",
					    textField:"name",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{studentNo:"",name:"不限制"});
					$("#studentObj_studentNo_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#empProgress_manage").datagrid("reload");
		},
		redo : function () {
			$("#empProgress_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#empProgress_manage").datagrid("options").queryParams;
			queryParams["taskObj.taskId"] = $("#taskObj_taskId_query").combobox("getValue");
			queryParams["studentObj.studentNo"] = $("#studentObj_studentNo_query").combobox("getValue");
			queryParams["addTime"] = $("#addTime").val();
			$("#empProgress_manage").datagrid("options").queryParams=queryParams; 
			$("#empProgress_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#empProgressQueryForm").form({
			    url:"EmpProgress/EmpProgress_queryEmpProgressOutputToExcel.action",
			});
			//提交表单
			$("#empProgressQueryForm").submit();
		},
		remove : function () {
			var rows = $("#empProgress_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var progressIds = [];
						for (var i = 0; i < rows.length; i ++) {
							progressIds.push(rows[i].progressId);
						}
						$.ajax({
							type : "POST",
							url : "EmpProgress/EmpProgress_ajaxDeleteEmpProgress.action",
							data : {
								progressIds : progressIds.join(","),
							},
							beforeSend : function () {
								$("#empProgress_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#empProgress_manage").datagrid("loaded");
									$("#empProgress_manage").datagrid("load");
									$("#empProgress_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#empProgress_manage").datagrid("loaded");
									$("#empProgress_manage").datagrid("load");
									$("#empProgress_manage").datagrid("unselectAll");
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
			var rows = $("#empProgress_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "EmpProgress/EmpProgress_ajaxModifyEmpProgressQuery.action",
					type : "post",
					data : {
						progressId : rows[0].progressId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (empProgress, response, status) {
						$.messager.progress("close");
						if (empProgress) { 
							$("#empProgressEditDiv").dialog("open");
							$("#empProgress_progressId_edit").val(empProgress.progressId);
							$("#empProgress_progressId_edit").validatebox({
								required : true,
								missingMessage : "请输入进度id",
								editable: false
							});
							$("#empProgress_taskObj_taskId_edit").combobox({
								url:"StudentTask/StudentTask_ajaxQueryAllStudentTask.action",
							    valueField:"taskId",
							    textField:"title",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#empProgress_taskObj_taskId_edit").combobox("select", empProgress.taskObjPri);
									//var data = $("#empProgress_taskObj_taskId_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#empProgress_taskObj_taskId_edit").combobox("select", data[0].taskId);
						            //}
								}
							});
							$("#empProgress_progressFile").val(empProgress.progressFile);
							$("#empProgress_progressFileA").attr("href", empProgress.progressFile);
							$("#empProgress_progressFileA").attr("target", "_blank");
							$("#empProgress_progressFileA").html("<font color=red>" + empProgress.progressFile + "</font>");
							$("#empProgress_studentObj_studentNo_edit").combobox({
								url:"Student/Student_ajaxQueryAllStudent.action",
							    valueField:"studentNo",
							    textField:"name",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#empProgress_studentObj_studentNo_edit").combobox("select", empProgress.studentObjPri);
									//var data = $("#empProgress_studentObj_studentNo_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#empProgress_studentObj_studentNo_edit").combobox("select", data[0].studentNo);
						            //}
								}
							});
							$("#empProgress_addTime_edit").val(empProgress.addTime);
							$("#empProgress_evaluate_edit").val(empProgress.evaluate);
							$("#empProgress_evalateContent_edit").val(empProgress.evalateContent);
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
