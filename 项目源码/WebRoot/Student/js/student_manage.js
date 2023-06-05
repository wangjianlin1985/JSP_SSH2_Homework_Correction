var student_manage_tool = null; 
$(function () { 
	initStudentManageTool(); //建立Student管理对象
	student_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#student_manage").datagrid({
		url : 'Student/Student_ajaxQueryStudent.action',
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "studentNo",
		sortOrder : "desc",
		toolbar : "#student_manage_tool",
		columns : [[
			{
				field : "studentNo",
				title : "学生编号",
				width : 140,
			},
			{
				field : "departmentObj",
				title : "所在班级",
				width : 140,
			},
			{
				field : "name",
				title : "姓名",
				width : 140,
			},
			{
				field : "sex",
				title : "性别",
				width : 140,
			},
			{
				field : "birthday",
				title : "出生日期",
				width : 140,
			},
			{
				field : "telephone",
				title : "联系电话",
				width : 140,
			},
			{
				field : "studentPhoto",
				title : "学生照片",
				width : "70px",
				height: "65px",
				formatter: function(val,row) {
					return "<img src='" + val + "' width='65px' height='55px' />";
				}
 			},
		]],
	});

	$("#studentEditDiv").dialog({
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
				if ($("#studentEditForm").form("validate")) {
					//验证表单 
					if(!$("#studentEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#studentEditForm").form({
						    url:"Student/Student_ajaxModifyStudent.action",
						    onSubmit: function(){
								if($("#studentEditForm").form("validate"))  {
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
			                        $("#studentEditDiv").dialog("close");
			                        student_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#studentEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#studentEditDiv").dialog("close");
				$("#studentEditForm").form("reset"); 
			},
		}],
	});
});

function initStudentManageTool() {
	student_manage_tool = {
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
			$("#student_manage").datagrid("reload");
		},
		redo : function () {
			$("#student_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#student_manage").datagrid("options").queryParams;
			queryParams["studentNo"] = $("#studentNo").val();
			queryParams["departmentObj.departmentNo"] = $("#departmentObj_departmentNo_query").combobox("getValue");
			queryParams["name"] = $("#name").val();
			queryParams["birthday"] = $("#birthday").datebox("getValue"); 
			queryParams["telephone"] = $("#telephone").val();
			$("#student_manage").datagrid("options").queryParams=queryParams; 
			$("#student_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#studentQueryForm").form({
			    url:"Student/Student_queryStudentOutputToExcel.action",
			});
			//提交表单
			$("#studentQueryForm").submit();
		},
		remove : function () {
			var rows = $("#student_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var studentNos = [];
						for (var i = 0; i < rows.length; i ++) {
							studentNos.push(rows[i].studentNo);
						}
						$.ajax({
							type : "POST",
							url : "Student/Student_ajaxDeleteStudent.action",
							data : {
								studentNos : studentNos.join(","),
							},
							beforeSend : function () {
								$("#student_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#student_manage").datagrid("loaded");
									$("#student_manage").datagrid("load");
									$("#student_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#student_manage").datagrid("loaded");
									$("#student_manage").datagrid("load");
									$("#student_manage").datagrid("unselectAll");
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
			var rows = $("#student_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Student/Student_ajaxModifyStudentQuery.action",
					type : "post",
					data : {
						studentNo : rows[0].studentNo,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (student, response, status) {
						$.messager.progress("close");
						if (student) { 
							$("#studentEditDiv").dialog("open");
							$("#student_studentNo_edit").val(student.studentNo);
							$("#student_studentNo_edit").validatebox({
								required : true,
								missingMessage : "请输入学生编号",
								editable: false
							});
							$("#student_password_edit").val(student.password);
							$("#student_password_edit").validatebox({
								required : true,
								missingMessage : "请输入登录密码",
							});
							$("#student_departmentObj_departmentNo_edit").combobox({
								url:"Department/Department_ajaxQueryAllDepartment.action",
							    valueField:"departmentNo",
							    textField:"departmentName",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#student_departmentObj_departmentNo_edit").combobox("select", student.departmentObjPri);
									//var data = $("#student_departmentObj_departmentNo_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#student_departmentObj_departmentNo_edit").combobox("select", data[0].departmentNo);
						            //}
								}
							});
							$("#student_name_edit").val(student.name);
							$("#student_name_edit").validatebox({
								required : true,
								missingMessage : "请输入姓名",
							});
							$("#student_sex_edit").val(student.sex);
							$("#student_sex_edit").validatebox({
								required : true,
								missingMessage : "请输入性别",
							});
							$("#student_birthday_edit").datebox({
								value: student.birthday,
							    required: true,
							    showSeconds: true,
							});
							$("#student_telephone_edit").val(student.telephone);
							$("#student_telephone_edit").validatebox({
								required : true,
								missingMessage : "请输入联系电话",
							});
							$("#student_studentPhoto").val(student.studentPhoto);
							$("#student_studentPhotoImg").attr("src", student.studentPhoto);
							$("#student_address_edit").val(student.address);
							$("#student_address_edit").validatebox({
								required : true,
								missingMessage : "请输入家庭地址",
							});
							$("#student_memo_edit").val(student.memo);
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
