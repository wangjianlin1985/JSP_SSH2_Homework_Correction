var journal_manage_tool = null; 
$(function () { 
	initJournalManageTool(); //建立Journal管理对象
	journal_manage_tool.init(); //如果需要通过下拉框查询，首先初始化下拉框的值
	$("#journal_manage").datagrid({
		url : 'Journal/Journal_ajaxQueryJournal.action',
		queryParams: {"studentObj.studentNo":$("#studentObj_studentNo_query").val() },
		fit : true,
		fitColumns : true,
		striped : true,
		rownumbers : true,
		border : false,
		pagination : true,
		pageSize : 5,
		pageList : [5, 10, 15, 20, 25],
		pageNumber : 1,
		sortName : "journalId",
		sortOrder : "desc",
		toolbar : "#journal_manage_tool",
		columns : [[
			{
				field : "journalId",
				title : "日志id",
				width : 70,
			},
			{
				field : "title",
				title : "日志标题",
				width : 140,
			},
			{
				field : "journalDate",
				title : "日志日期",
				width : 140,
			},
			{
				field : "studentObj",
				title : "学生",
				width : 140,
			},
		]],
	});

	$("#journalEditDiv").dialog({
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
				if ($("#journalEditForm").form("validate")) {
					//验证表单 
					if(!$("#journalEditForm").form("validate")) {
						$.messager.alert("错误提示","你输入的信息还有错误！","warning");
					} else {
						$("#journalEditForm").form({
						    url:"Journal/Journal_ajaxModifyJournal.action",
						    onSubmit: function(){
								if($("#journalEditForm").form("validate"))  {
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
			                        $("#journalEditDiv").dialog("close");
			                        journal_manage_tool.reload();
			                    }else{
			                        $.messager.alert("消息",obj.message);
			                    } 
						    }
						});
						//提交表单
						$("#journalEditForm").submit();
					}
				}
			},
		},{
			text : "取消",
			iconCls : "icon-redo",
			handler : function () {
				$("#journalEditDiv").dialog("close");
				$("#journalEditForm").form("reset"); 
			},
		}],
	});
});

function initJournalManageTool() {
	journal_manage_tool = {
		init: function() {
			 
		},
		reload : function () {
			$("#journal_manage").datagrid("reload");
		},
		redo : function () {
			$("#journal_manage").datagrid("unselectAll");
		},
		search: function() {
			var queryParams = $("#journal_manage").datagrid("options").queryParams;
			queryParams["title"] = $("#title").val();
			queryParams["journalDate"] = $("#journalDate").datebox("getValue"); 
			queryParams["studentObj.studentNo"] = $("#studentObj_studentNo_query").val();
			$("#journal_manage").datagrid("options").queryParams=queryParams; 
			$("#journal_manage").datagrid("load");
		},
		exportExcel: function() {
			$("#journalQueryForm").form({
			    url:"Journal/Journal_queryJournalOutputToExcel.action",
			});
			//提交表单
			$("#journalQueryForm").submit();
		},
		remove : function () {
			var rows = $("#journal_manage").datagrid("getSelections");
			if (rows.length > 0) {
				$.messager.confirm("确定操作", "您正在要删除所选的记录吗？", function (flag) {
					if (flag) {
						var journalIds = [];
						for (var i = 0; i < rows.length; i ++) {
							journalIds.push(rows[i].journalId);
						}
						$.ajax({
							type : "POST",
							url : "Journal/Journal_ajaxDeleteJournal.action",
							data : {
								journalIds : journalIds.join(","),
							},
							beforeSend : function () {
								$("#journal_manage").datagrid("loading");
							},
							success : function (data) {
								if (data.success) {
									$("#journal_manage").datagrid("loaded");
									$("#journal_manage").datagrid("load");
									$("#journal_manage").datagrid("unselectAll");
									$.messager.show({
										title : "提示",
										msg : data.message
									});
								} else {
									$("#journal_manage").datagrid("loaded");
									$("#journal_manage").datagrid("load");
									$("#journal_manage").datagrid("unselectAll");
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
			var rows = $("#journal_manage").datagrid("getSelections");
			if (rows.length > 1) {
				$.messager.alert("警告操作！", "编辑记录只能选定一条数据！", "warning");
			} else if (rows.length == 1) {
				$.ajax({
					url : "Journal/Journal_ajaxModifyJournalQuery.action",
					type : "post",
					data : {
						journalId : rows[0].journalId,
					},
					beforeSend : function () {
						$.messager.progress({
							text : "正在获取中...",
						});
					},
					success : function (journal, response, status) {
						$.messager.progress("close");
						if (journal) { 
							$("#journalEditDiv").dialog("open");
							$("#journal_journalId_edit").val(journal.journalId);
							$("#journal_journalId_edit").validatebox({
								required : true,
								missingMessage : "请输入日志id",
								editable: false
							});
							$("#journal_title_edit").val(journal.title);
							$("#journal_title_edit").validatebox({
								required : true,
								missingMessage : "请输入日志标题",
							});
							$("#journal_content_edit").val(journal.content);
							$("#journal_content_edit").validatebox({
								required : true,
								missingMessage : "请输入日志内容",
							});
							$("#journal_journalDate_edit").datebox({
								value: journal.journalDate,
							    required: true,
							    showSeconds: true,
							});
							$("#journal_studentObj_studentNo_edit").combobox({
								url:"Student/Student_ajaxQueryAllStudent.action",
							    valueField:"studentNo",
							    textField:"name",
							    panelHeight: "auto",
						        editable: false, //不允许手动输入 
						        onLoadSuccess: function () { //数据加载完毕事件
									$("#journal_studentObj_studentNo_edit").combobox("select", journal.studentObjPri);
									//var data = $("#journal_studentObj_studentNo_edit").combobox("getData"); 
						            //if (data.length > 0) {
						                //$("#journal_studentObj_studentNo_edit").combobox("select", data[0].studentNo);
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
