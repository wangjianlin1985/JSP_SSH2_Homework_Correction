var studentTask_manage_tool = null; 
$(function () { 
	initStudentTaskManageTool(); //建立StudentTask管理对象
	studentTask_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#studentTask_manage").datagrid({
		url : 'StudentTask/StudentTask_ajaxQueryStudentTask.action',
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
		toolbar : "#studentTask_manage_tool",
		columns : [[
			{
				field : "taskId",
				title : "作业id",
				width : 70,
			},
			{
				field : "title",
				title : "作业标题",
				width : 140,
			},
			{
				field : "studentObj",
				title : "作业学生",
				width : 140,
			},
			{
				field : "taskFile",
				title : "作业文件",
				width : "400px",
				height: "65px",
				formatter: function(val,row) {
					return "<a href='" + val + "' target='_blank'><font color=red>" + val +　"</font></a>";
				}
 			},
			{
				field : "taskDate",
				title : "作业发布日期",
				width : 140,
			},
			{
				field : "chargeObj",
				title : "负责的老师",
				width : 140,
			},
		]],
	});

	$("#studentTaskEditDiv").dialog({
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
				if ($("#studentTaskEditForm").form("validate")) {
					//验证表单 
					if(!$("#studentTaskEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#studentTaskEditForm").form({
						    url:"StudentTask/StudentTask_ajaxModifyStudentTask.action",
						    onSubmit: function(){
								if($("#studentTaskEditForm").form("validate"))  {
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
			                        $("#studentTaskEditDiv").dialog("close");
			                        studentTask_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#studentTaskEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#studentTaskEditDiv").dialog("close");
				$("#studentTaskEditForm").form("reset"); 
			},
		}],
	});
});

function initStudentTaskManageTool() {
	studentTask_manage_tool = {
		init: function() {
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
			$.ajax({
				url : "DepartCharge/DepartCharge_ajaxQueryAllDepartCharge.action",
				type : "post",
				success : function (data, response, status) {
					$("#chargeObj_chargeUserName_query").combobox({ 
					    valueField:"chargeUserName",
					    textField:"name",
					    panelHeight: "200px",
				        editable: false, //不允许手动输入 
					});
					data.splice(0,0,{chargeUserName:"",name:"不限制"});
					$("#chargeObj_chargeUserName_query").combobox("loadData",data); 
				}
			});
		},
		reload : function () {
			$("#studentTask_manage").datagrid("reload");
		},
		redo : function () {
			$("#studentTask_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#studentTask_manage").datagrid("options").queryParams;
			queryParams["title"] = $("#title").val();
			queryParams["studentObj.studentNo"] = $("#studentObj_studentNo_query").combobox("getValue");
			queryParams["taskDate"] = $("#taskDate").datebox("getValue"); 
			queryParams["chargeObj.chargeUserName"] = $("#chargeObj_chargeUserName_query").combobox("getValue");
			$("#studentTask_manage").datagrid("options").queryParams=queryParams; 
			$("#studentTask_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#studentTaskQueryForm").form({
			    url:"StudentTask/StudentTask_queryStudentTaskOutputToExcel.action",
			});
			//提交表单
			$("#studentTaskQueryForm").submit();
		},
		remove : function () {
			var rows = $("#studentTask_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var taskIds = [];
						for (var i = 0; i < rows.length; i ++) {
							taskIds.push(rows[i].taskId);
						}
						$.ajax({
							type : "POST",
							url : "StudentTask/StudentTask_ajaxDeleteStudentTask.action",
							data : {
								taskIds : taskIds.join(","),
							},
							beforeSend : function () {
								$("#studentTask_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#studentTask_manage").datagrid("loaded");
									$("#studentTask_manage").datagrid("load");
									$("#studentTask_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#studentTask_manage").datagrid("loaded");
									$("#studentTask_manage").datagrid("load");
									$("#studentTask_manage").datagrid("unselectAll");
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
			var rows = $("#studentTask_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "StudentTask/StudentTask_ajaxModifyStudentTaskQuery.action",
					type : "post",
					data : {
						taskId : rows[0].taskId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (studentTask, response, status) {
						$.messager.progress("close");
						if (studentTask) { 
							$("#studentTaskEditDiv").dialog("open");
							$("#studentTask_taskId_edit").val(studentTask.taskId);
							$("#studentTask_taskId_edit").validatebox({
								required : true,
								missingMessage : "请输入作业id",
								editable: false
							});
							$("#studentTask_title_edit").val(studentTask.title);
							$("#studentTask_title_edit").validatebox({
								required : true,
								missingMessage : "请输入作业标题",
							});
							$("#studentTask_content_edit").val(studentTask.content);
							$("#studentTask_content_edit").validatebox({
								required : true,
								missingMessage : "请输入作业内容",
							});
							$("#studentTask_studentObj_studentNo_edit").combobox({
								url:"Student/Student_ajaxQueryAllStudent.action",
							    valueField:"studentNo",
							    textField:"name",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#studentTask_studentObj_studentNo_edit").combobox("select", studentTask.studentObjPri);
									//var data = $("#studentTask_studentObj_studentNo_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#studentTask_studentObj_studentNo_edit").combobox("select", data[0].studentNo);
						            //}
								}
							});
							$("#studentTask_taskFile").val(studentTask.taskFile);
							$("#studentTask_taskFileA").attr("href", studentTask.taskFile);
							$("#studentTask_taskFileA").attr("target", "_blank");
							$("#studentTask_taskFileA").html("<font color=red>" + studentTask.taskFile + "</font>");
							$("#studentTask_taskDate_edit").datebox({
								value: studentTask.taskDate,
							    required: true,
							    showSeconds: true,
							});
							$("#studentTask_chargeObj_chargeUserName_edit").combobox({
								url:"DepartCharge/DepartCharge_ajaxQueryAllDepartCharge.action",
							    valueField:"chargeUserName",
							    textField:"name",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#studentTask_chargeObj_chargeUserName_edit").combobox("select", studentTask.chargeObjPri);
									//var data = $("#studentTask_chargeObj_chargeUserName_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#studentTask_chargeObj_chargeUserName_edit").combobox("select", data[0].chargeUserName);
						            //}
								}
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
