Ext.define('MyApp.view.InvoiceItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.InvoiceItemGrid',
	title : "Rechnungen",
	id : 'InvoiceItemGrid',
	customicon : '/FlexibleOrders/images/new_ab.png',
	onActionClick : function(view, a, b, column, event, record, f) {
				console.log('invoiceItemGrid - customtransitionfunction');
				var anr = record.data.invoiceNumber;
				
				MyApp.getApplication().getController('MyController').complete(
						"ok", anr, record);

			},
	onRemoveClick: function(view, a, b, column, event, record, f) {
				console.log('invoiceItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').deleteReport(
						record.data.invoiceNumber);

			}


});