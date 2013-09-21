
//@require @packageOverrides
Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
	appFolder: '/FlexibleOrders/resources/app',
    models: [
        'BestellungData',
        'BestellpositionData',
        'ShippingItemData',
//        'InvoiceItemData',
//        'ArchiveItemData',
        'ArtikelData',
        'KundeData'
    ],
    stores: [
        'BestellungDataStore',
        'BestellpositionDataStore',
        'ShippingItemDataStore',
//        'InvoiceItemDataStore',
//        'ArchiveItemDataStore',
        'ArtikelDataStore',
        'KundeDataStore',
        'OrderNumberDataStore'
    ],
    views: [
        'MainPanel',
        'BpForm',
        'ErstelleBestellungWindow',
        'BestellungWindow',
		'ConfirmWindow',
        'CompleteWindow',
        'DeliverWindow',
        'TransitionWindow',
        'PositionGridPanel',
        'OrderItemGridPanel',
        'ShippingItemGridPanel',
//        'InvoiceItemGridPanel',
//        'ArchiveItemGridPanel',
        'BestellungGridPanel',
        'CustomerComboBox',
        'OrderNumberComboBox'
    ],
    autoCreateViewport: false,
    controllers: [
        'MyController'
    ],
    name: 'MyApp',
    //autoCreateViewport:true,
    launch: function() {
        Ext.create('MyApp.view.ConfirmPanel', {
            layout: 'fit',
            renderTo: Ext.get('extjs_confirm')
        });
    }
});

