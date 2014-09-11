Ext.define('MyApp.view.AgreementItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.AgreementItemGrid',
			title : "Auftragsbest&auml;tigungen",
			id : 'AgreementItemGrid',
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('AgreementItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('AgreementController').onAgree(
						"ok", record);
			}
		});