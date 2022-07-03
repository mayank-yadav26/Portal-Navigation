Ext.onReady(function() {
	grid();
});

Ext.define('requestTypeStore', {
	extend : "Ext.data.Store",
	fields : ['requestType'],
	data : [{
		"requestType" : "GET"
	},{
		"requestType" : "POST"
	}]
});

Ext.define('Grid', {
	extend : 'Ext.data.Model',
	fields: [
		{name: 'navigationName', type:'string'},
		{name: 'baseUrl', type:'string'},
		{name: 'requestType', type:'string'},
		{name: 'parameters', type:'string'},
		{name: 'requestHeaders', type:'string'}
		]
});
Ext.define('GridStore', {
	extend : "Ext.data.Store",
	storeId : "gridStoreId",
	autoLoad : true,
	model : "Grid",
	//pageSize : 10,
	proxy : {
		type : 'ajax',
		url : '/portal_navigation/GetNavigationDetails.action',
		reader : {
			type : 'json',
			rootProperty : 'data',
			totalProperty : 'total',
			//enablePaging : 'true'
		}
	}
});

var requestTypeStore = Ext.create('requestTypeStore');
var activityStore = Ext.create('GridStore');
//for pagination
/*activityStore.load({
	params : {
		start : 0,
		limit : 10,
		query : "select"
	}
});*/

function resetFields(form) {
	form.getForm().reset();
}

//for grid panel
function grid() {
	console.log('Loading Grid UI ...');
	dataGrid = Ext.create('Ext.grid.Panel',{
		renderTo : Ext.getBody(),
		id : 'grid',
		width : '100%',
		height : 720,
		store : activityStore,
		title : 'Navigation Grid',
		columns : [ {
			text : 'Navigation Id',
			flex : 1,
			width : '100%',
			sortable : true,
			hidden : true,
			dataIndex : 'navigationId'
		},{
			text : 'Navigation Name',
			flex : 0.5,
			width : '100%',
			sortable : false,
			hidden : false,
			dataIndex : 'navigationName'
		},{
			text : 'Base Url',
			flex : 0.5,
			width : '100%',
			sortable : false,
			hidden : false,
			dataIndex : 'baseUrl'
		}, {
			text : 'Request Type',
			flex : 0.5,
			width : '100%',
			sortable : false,
			hideable : false,
			dataIndex : 'requestType'
		}, {
			text : 'Parameters',
			flex : 1,
			width : '100%',
			sortable : false,
			hideable : false,
			dataIndex : 'parameters',
		}, {
			text : 'Request Headers',
			flex : 1,
			width : '100%',
			sortable : false,
			hideable : false,
			dataIndex : 'requestHeaders',
		}],

		selModel : {
			selType : 'checkboxmodel',
			border : false,
			listeners : {
				select : function(grid, record) {
					record = Ext.getCmp('grid').getSelectionModel().getSelection();
					if (record.length == 1) {
						Ext.getCmp("editbtn").enable();
					} else {
						Ext.getCmp("editbtn").disable();
					}
					if (record.length > 0) {
						Ext.getCmp("deletebtn").enable();
						Ext.getCmp("runbtn").enable();
						Ext.getCmp("navigationFile").enable();
					} else {
						Ext.getCmp("deletebtn").disable();
						Ext.getCmp("runbtn").disable();
						Ext.getCmp("navigationFile").disable();
					}
					data = record;
				},
				deselect : function(grid, record) {
					record = Ext.getCmp('grid').getSelectionModel().getSelection();
					if (record.length == 1) {
						Ext.getCmp("editbtn").enable();
					} else {
						Ext.getCmp("editbtn").disable();
					}
					if (record.length > 0) {
						Ext.getCmp("deletebtn").enable();
						Ext.getCmp("runbtn").enable();
						Ext.getCmp("navigationFile").enable();
					} else {
						Ext.getCmp("deletebtn").disable();
						Ext.getCmp("runbtn").disable();
						Ext.getCmp("navigationFile").disable();
					}
					data = record;
				}
			}
		},
		tbar : new Ext.PagingToolbar(
				{
					//inputItemWidth : 50,
					//pageSize : 10,
					store : activityStore,
					displayInfo : true,
					displayMsg : 'Total items : {2} ',
					emptyMsg : "No topics to display",
					items : [
						'-',
						{
							iconCls : 'fa fa-plus-circle',
							id : 'addbtn',
							text : 'Add',
							handler : function() {
								addData(dataGrid);
							}
						},
						'-',
						{
							iconCls : 'fa fa-pencil',
							text : 'Edit',
							id : 'editbtn',
							disabled : true,
							handler : function() {
								editData(data);
							}
						},
						'-',
						{
							iconCls : 'fa fa-trash',
							text : 'Delete',
							id : 'deletebtn',
							disabled : true,
							handler : function() {
								Ext.Msg
								.show({
									title : 'Are you sure?',
									message : 'Are you sure that you want to delete these records?',
									buttons : Ext.Msg.YESNOCANCEL,
									icon : Ext.Msg.QUESTION,
									fn : function(btn) {
										if (btn === 'yes') {
											deleteData(data);
										}
									}
								});
							}
						},
						'-',
						{
							iconCls : 'fa fa-play',
							text : 'RUN',
							id : 'runbtn',
							disabled : true,
							handler : function() {
								Ext.Msg
								.show({
									title : 'Are you sure?',
									message : 'Are you sure that you want to run these records?',
									buttons : Ext.Msg.YESNOCANCEL,
									icon : Ext.Msg.QUESTION,
									fn : function(btn) {
										if (btn === 'yes') {
											runData(data);
										}
									}
								});
							}
						},
						'-',
						{
							iconCls : 'fa fa-file',
							text : 'Generate Nav File',
							id : 'navigationFile',
							disabled : true,
							handler : function() {
								Ext.Msg
								.show({
									title : 'Are you sure?',
									message : 'Do you want to generate navigation file with these records?',
									buttons : Ext.Msg.YESNOCANCEL,
									icon : Ext.Msg.QUESTION,
									fn : function(btn) {
										if (btn === 'yes') {
											createNavigationFile(data);
										}
									}
								});
							}
						}]
				})

	});
	console.log('Successfully Rendered Grid UI.');
}
function addData(grid) {
	var form = new Ext.form.Panel(
			{
				url : '/portal_navigation/AddNavigationDetails.action',
				width : 500,
				title : 'Add Film',
				floating : true,
				closable : true,
				frame : true,
				bodyPadding : 10,
				defaultType : 'textfield',
				items : [{
					allowBlank : false,
					fieldLabel : 'Navigation Name',
					name : 'navigationName',
					msgTarget : 'under',
					width : '100%',
					allowBlank : false,
					id : 'navigationName',
				}, {
					allowBlank : false,
					fieldLabel : 'Base Url',
					name : 'baseUrl',
					msgTarget : 'under',
					width : '100%',
					allowBlank : false,
					id : 'baseUrl',
				}, {
					xtype : 'combobox',
					id : 'requestType',
					allowBlank : false,
					width : '100%',
					fieldLabel : 'Request Type',
					store : requestTypeStore,
					valueField : 'requestType',
					displayField : 'requestType',
					name : 'requestType',
					typeAhead : true,
					queryMode : 'local',
				}, {
					allowBlank : true,
					fieldLabel : 'Parameters',
					name : 'parameters',
					xtype : 'textareafield',
					maxRows : 4,
					width : '100%',
					id : 'parameters',
				} , {
					allowBlank : true,
					fieldLabel : 'Request Headers',
					name : 'requestHeaders',
					xtype : 'textareafield',
					maxRows : 4,
					width : '100%',
					id : 'requestHeaders',
				} ],
				buttonAlign : 'center',
				buttons : [
					{
						text : 'Save',
						disabled : true,
						formBind : true,
						handler : function(button,e) {
							var formData = this.up('form').getForm();
							form.getForm().submit(
									{
										method : 'POST',
										waitTitle : 'Connecting',
										waitMsg : 'Adding Data...',
										success : function(form,action) {
											console.log("Success");
											//Ext.Msg.alert('Success',action.result.successresponse.message);
											form.reset();
											Ext.getCmp('grid').getStore().reload();
											button.up('form').close();
										},
										failure : function(form,action) {
											console.log("Failed");
											form.reset();
											//Ext.Msg.alert('Failure',action.result.successresponse.message);
										}
									});
						}
					}, {
						text : 'Cancel',
						handler : function() {
							resetFields(form);
							form.close();
						}
					}

					]
			}).show();
}

function deleteData(record) {
	var navigationIds = [];
	record.forEach(function(item, index) {
		navigationIds.push(item.data.navigationId);
	});
	navigationIds = navigationIds.join();
	Ext.Ajax.request({
		url : '/portal_navigation/DeleteNavigationDetails.action',
		method : 'POST',
		params : {
			"navigationIds" : navigationIds
		},
		success : function(response) {
			console.log(response);
			var obj = JSON.parse(response.responseText);
			if (obj.success) {
				//Ext.Msg.alert('Success', obj.successresponse.message);
				Ext.getCmp('grid').getStore().reload();
				Ext.getCmp("deletebtn").disable();
				Ext.getCmp("editbtn").disable();
			} else {
				console.log("Failed in delete");
				//Ext.Msg.alert('Failure', obj.successresponse.message);
			}
		},
		failure : function(response) {
			console.log(response);
			var obj = JSON.parse(response.responseText);
			if (obj.success) {
				//Ext.Msg.alert('Success', obj.successresponse.message);
				Ext.getCmp('grid').getStore().reload();
				Ext.getCmp("deletebtn").disable();
				Ext.getCmp("editbtn").disable();
			} else {
				console.log("Failed in delete");
				//Ext.Msg.alert('Failure', obj.successresponse.message);
			}
		}
	});
}

function editData(record) {
	var form = new Ext.form.Panel(
			{
				url : '/portal_navigation/EditNavigationDetails.action',
				width : 500,
				title : 'Edit Navigation',
				floating : true,
				closable : true,
				frame : true,
				bodyPadding : 10,
				defaultType : 'textfield',
				items : [ {
					allowBlank : false,
					fieldLabel : 'Navigation Id',
					name : 'navigationId',
					id : 'navigationId',
					width : '100%',
					value : record[0].data.navigationId,
					hidden : true
				}, {
					allowBlank : false,
					fieldLabel : 'Navigaton Name',
					name : 'navigationName',
					msgTarget : 'under',
					width : '100%',
					allowBlank : false,
					id : 'navigationName',
					value : record[0].data.navigationName
				},{
					allowBlank : false,
					fieldLabel : 'Base Url',
					name : 'baseUrl',
					msgTarget : 'under',
					width : '100%',
					allowBlank : false,
					id : 'baseUrl',
					value : record[0].data.baseUrl
				}, {
					xtype : 'combobox',
					id : 'requestType',
					allowBlank : false,
					width : '100%',
					fieldLabel : 'Request Type',
					store : requestTypeStore,
					valueField : 'requestType',
					displayField : 'requestType',
					name : 'requestType',
					typeAhead : true,
					queryMode : 'local',
					value : record[0].data.requestType
				}, {
					allowBlank : true,
					fieldLabel : 'Parameters',
					name : 'parameters',
					xtype : 'textareafield',
					maxRows : 4,
					width : '100%',
					id : 'parameters',
					value : record[0].data.parameters
				} , {
					allowBlank : true,
					fieldLabel : 'Request Headers',
					name : 'requestHeaders',
					xtype : 'textareafield',
					maxRows : 4,
					width : '100%',
					id : 'requestHeaders',
					value : record[0].data.requestHeaders
				}],
				buttonAlign : 'center',
				buttons : [
					{
						text : 'Save',
						disabled : true,
						formBind : true,
						handler : function(button,e) {
							form.getForm().submit(
									{
										method : 'POST',
										waitTitle : 'Connecting',
										waitMsg : 'Updating Data...',
										success : function(form,action) {
											//Ext.Msg.alert('Success',action.result.successresponse.message);
											Ext.getCmp('grid').getStore().reload();
											Ext.getCmp("editbtn").disable();
											Ext.getCmp("deletebtn").disable();
											button.up('form').close();
										},
										failure : function(form,action) {
											console.log("Updating of data failed");
											//Ext.Msg.alert('Failure',action.result.successresponse.message);
										}
									});
						}
					}, {
						text : 'Cancel',
						handler : function() {
							resetFields(form);
							form.close();
						}
					}

					]
			}).show();
}

function runData(record) {
	var navigationIds = [];
	record.forEach(function(item, index) {
		navigationIds.push(item.data.navigationId);
	});
	navigationIds = navigationIds.join();
	Ext.Ajax.request({
		url : '/portal_navigation/RunNavigationDetails.action',
		method : 'POST',
		params : {
			"navigationIds" : navigationIds
		},
		success : function(response) {
			var obj = JSON.parse(response.responseText);
			if (obj.success) {
				//Ext.Msg.alert('Success', obj.successresponse.message);
				Ext.getCmp('grid').getStore().reload();
				Ext.getCmp("runbtn").disable();
				Ext.getCmp("deletebtn").disable();
				Ext.getCmp("editbtn").disable();
			} else {
				//Ext.Msg.alert('Failure', obj.successresponse.message);
			}
			console.log("obj is : "+obj);
			console.log("response : "+response);
			//location.href="http://www.google.com";
			//for opening link in new tab;
			window.open("http://127.0.0.1:8887/"+obj.docLink);
			//location.assign("http://www.google.com");
		},
		failure : function(response) {
			var obj = JSON.parse(response.responseText);
			if (obj.success) {
				//Ext.Msg.alert('Success', obj.successresponse.message);
				Ext.getCmp('grid').getStore().reload();
				Ext.getCmp("runbtn").disable();
				Ext.getCmp("deletebtn").disable();
				Ext.getCmp("editbtn").disable();
			} else {
				//Ext.Msg.alert('Failure', obj.successresponse.message);
			}
		}
	});
}

function createNavigationFile(record) {
	var navigationIds = [];
	record.forEach(function(item, index) {
		navigationIds.push(item.data.navigationId);
	});
	navigationIds = navigationIds.join();
	Ext.Ajax.request({
		url : '/portal_navigation/CreateNavigationFile.action',
		method : 'POST',
		params : {
			"navigationIds" : navigationIds
		},
		success : function(response) {
			var obj = JSON.parse(response.responseText);
			if (obj.success) {
				//Ext.Msg.alert('Success', obj.successresponse.message);
				Ext.getCmp('grid').getStore().reload();
				Ext.getCmp("createNavigationBtn").disable();
				Ext.getCmp("runbtn").disable();
				Ext.getCmp("deletebtn").disable();
				Ext.getCmp("editbtn").disable();
			} else {
				//Ext.Msg.alert('Failure', obj.successresponse.message);
			}
			console.log("obj is : "+obj);
			console.log("response : "+response);
			//location.href="http://www.google.com";
			//for opening link in new tab;
			window.open("http://127.0.0.1:8887/"+obj.docLink);
			//location.assign("http://www.google.com");
		},
		failure : function(response) {
			var obj = JSON.parse(response.responseText);
			if (obj.success) {
				//Ext.Msg.alert('Success', obj.successresponse.message);
				Ext.getCmp('grid').getStore().reload();
				Ext.getCmp("createNavigationBtn").disable();
				Ext.getCmp("runbtn").disable();
				Ext.getCmp("deletebtn").disable();
				Ext.getCmp("editbtn").disable();
			} else {
				//Ext.Msg.alert('Failure', obj.successresponse.message);
			}
		}
	});
}


