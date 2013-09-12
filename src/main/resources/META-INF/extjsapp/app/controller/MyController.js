/*
 * File: app/controller/MyController.js
 * 
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 * 
 * This file requires use of the Ext JS 4.2.x library, under independent
 * license. License of Sencha Architect does not include license for Ext JS
 * 4.2.x. For more details see http://www.sencha.com/license or contact
 * license@sencha.com.
 * 
 * This file will be auto-generated each and everytime you save your project.
 * 
 * Do NOT hand edit this file.
 */

Ext.override(Ext.data.JsonWriter, {
			encode : false,
			writeAllFields : true,
			listful : true,
			constructor : function(config) {
				this.callParent(this, config);
				return this;
			},
			render : function(params, baseParams, data) {
				params.jsonData = data;
			}
		});

Ext.define('MyApp.controller.MyController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'MyController',
	models : ['BestellungData', 'BestellpositionData', 'KundeData',
			'ArchiveItemData', 'InvoiceItemData', 'ShippingItemData'],
	stores : ['BestellungDataStore', 'BestellpositionDataStore',
			'KundeDataStore', 'InvoiceItemDataStore', 'ShippingItemDataStore',
			'ArchiveItemDataStore','OrderNumberDataStore'],
	views : ['MainPanel', 'BpForm', 'BestellungWindow',
			'ErstelleBestellungWindow', 'BpWindow', 'BestellpositionGridPanel',
			'ConfirmWindow', 'CompleteWindow', 'DeliverWindow',
			'TransitionWindow', 'OrderNumberComboBox'],
	// TODO: Registrieren und Initialisiseren von Views an einer Stelle
	// implementieren
	// Die view muss eine ID haben

	activeBestellnr : 0,
	activeBestellpositionId : 0,
	bestellungDataStore : null,

	activeCustomer : 0,

	init : function(application) {
		this.control({
					'#mainCustomerComboBox' : {
						change : this.onCustomerChange
					},
					'#BestellungGridPanel' : {
						selectionchange : this.onSelectionchange,
						itemdblclick : this.showTransition
					},
					'#ErstelleBpButton' : {
						click : this.loadBpForm
					},
					'#ErstelleBestellungButton' : {
						click : this.loadErstelleBestellungWindow
					},
					'#DeleteBpButton' : {
						click : this.deleteBpDialog
					},
					'#DeleteBestellungButton' : {
						click : this.deleteBestellungDialog
					},
					'#SubmitBpButton' : {
						click : this.addBp
					},
					'#AbBestellungButton' : {
						click : this.bestaetigeAuftragDialog
					},
					'#RechnungBestellungButton' : {
						click : this.liefereDialog
					},
					'#BestellungPdf' : {
						click : this.showBestellundPdf
					},
					'#AbPdf' : {
						click : this.showAbPdf
					},
					'#RechnungPdf' : {
						click : this.showRechnungPdf
					}
				});
		this.getBpFormView().create();
		this.getBpWindowView().create();
		this.getErstelleBestellungWindowView().create();
		this.getStore('BestellpositionDataStore').filter('status', 'ordered');
		this.getStore('ShippingItemDataStore').filter('status', 'confirmed');
		this.getStore('InvoiceItemDataStore').filter('status', 'shipped');
		this.getStore('ArchiveItemDataStore').filter('status', 'completed');
	},

	onSelectionchange : function(view, selections, options) {
		if (view.getStore().storeId != "BestellungDataStore")
			return;
		if (selections.length == 0)
			return;
		console.log('changeBestellungSelection');
		// Variablen laden
		/*
		 * var bestellung = selections[0]; this.activeBestellnr =
		 * bestellung.getData().orderNumber; bpform =
		 * Ext.getCmp('ErstelleBpForm').getForm();
		 * 
		 * //BpGrid und Controller aktualisieren
		 * bpform.findField('bestellung').setValue(this.activeBestellnr);
		 * this.getBpFormView.bestellnr = this.activeBestellnr;
		 * Ext.StoreMgr.get('BestellpositionDataStore').reload(this.activeBestellnr); //
		 * Buttons konfigurieren ORDERED, CONFIRMED, SHIPPED, COMPLETED; buttons =
		 * this.getButtons(); switch (bestellung.getData().status){ case
		 * "ORDERED": buttons.abBestellungButton.setDisabled(false);
		 * buttons.bezahltBestellungButton.setDisabled(true);
		 * buttons.deleteBestellung.setDisabled(false);
		 * buttons.deleteBestellungButton.setDisabled(false);
		 * buttons.rechnungBestellungButton.setDisabled(true);
		 * buttons.stornoBestellungButton.setDisabled(false);
		 * buttons.bestellungPdfButton.setDisabled(false);
		 * buttons.abPdfButton.setDisabled(true);
		 * buttons.rechnungPdfButton.setDisabled(true); break; case "CONFIRMED":
		 * buttons.abBestellungButton.setDisabled(true);
		 * buttons.bezahltBestellungButton.setDisabled(true);
		 * buttons.deleteBestellung.setDisabled(true);
		 * buttons.deleteBestellungButton.setDisabled(true);
		 * buttons.rechnungBestellungButton.setDisabled(false);
		 * buttons.stornoBestellungButton.setDisabled(false);
		 * buttons.bestellungPdfButton.setDisabled(false);
		 * buttons.abPdfButton.setDisabled(false);
		 * buttons.rechnungPdfButton.setDisabled(true); break; case "SHIPPED":
		 * buttons.abBestellungButton.setDisabled(true);
		 * buttons.bezahltBestellungButton.setDisabled(false);
		 * buttons.deleteBestellung.setDisabled(true);
		 * buttons.deleteBestellungButton.setDisabled(true);
		 * buttons.rechnungBestellungButton.setDisabled(true);
		 * buttons.stornoBestellungButton.setDisabled(false);
		 * buttons.bestellungPdfButton.setDisabled(false);
		 * buttons.abPdfButton.setDisabled(false);
		 * buttons.rechnungPdfButton.setDisabled(false); break; case
		 * "COMPLETED": buttons.abBestellungButton.setDisabled(true);
		 * buttons.bezahltBestellungButton.setDisabled(true);
		 * buttons.deleteBestellung.setDisabled(true);
		 * buttons.deleteBestellungButton.setDisabled(true);
		 * buttons.rechnungBestellungButton.setDisabled(true);
		 * buttons.stornoBestellungButton.setDisabled(false);
		 * buttons.bestellungPdfButton.setDisabled(false);
		 * buttons.abPdfButton.setDisabled(false);
		 * buttons.rechnungPdfButton.setDisabled(false); break; }
		 */
	},
	getButtons : function() {
		var buttons = {
			deleteBp : Ext.getCmp('DeleteBpButton'),
			erstelleBpButton : Ext.getCmp('ErstelleBpButton'),
			deleteBestellung : Ext.getCmp('DeleteBestellungButton'),
			erstelleBestellungButton : Ext.getCmp('ErstelleBestellungButton'),
			abBestellungButton : Ext.getCmp('AbBestellungButton'),
			rechnungBestellungButton : Ext.getCmp('RechnungBestellungButton'),
			bezahltBestellungButton : Ext.getCmp('BezahltBestellungButton'),
			stornoBestellungButton : Ext.getCmp('StornoBestellungButton'),
			deleteBestellungButton : Ext.getCmp('DeleteBestellungButton'),
			bestellungPdfButton : Ext.getCmp('BestellungPdf'),
			abPdfButton : Ext.getCmp('AbPdf'),
			rechnungPdfButton : Ext.getCmp('RechnungPdf'),
			offeneBpPdfButton : Ext.getCmp('OffeneBpPdf')
		};
		return buttons;
	},

	deleteBpDialog : function(button, event, options) {
		if (this.debug)
			console.log('deleteBpDialog');
		var bestellung = this.getBpSelection();

		if (bestellung.getData().status == 'ORDERED')
			Ext.MessageBox.confirm('Best&aumltigen',
					'Bestellpostion sicher l&ouml;schen?', this.deleteBp);
		else
			Ext.MessageBox
					.alert('Hinweis',
							'Bestellposition schon best&auml;tigt. Nur noch Storno ist m&ouml;glich.');
	},

	deleteBestellungDialog : function(button, event, options) {
		if (this.debug)
			console.log('deleteBpDialog');
		var bestellung = this.getBestellungSelection();

		if (bestellung.getData().status == 'ORDERED')
			Ext.MessageBox.confirm('Best&aumltigen',
					'Bestellung sicher l&ouml;schen?', this.deleteBestellung);
		else
			Ext.MessageBox
					.alert('Hinweis',
							'Bestellung schon best&auml;tigt. Nur noch Storno ist m&ouml;glich.');
	},

	getBpSelection : function() {
		var bpGridPanel = Ext.getCmp('BestellpositionGridPanel');
		var selectionModel = bpGridPanel.getSelectionModel();
		var bp = selectionModel.getSelection()[0];
		return bp;
	},

	/*
	 * deleteBp: function(btn) { var bpGridPanel =
	 * Ext.getCmp('BestellpositionGridPanel'); var selectionModel =
	 * bpGridPanel.getSelectionModel(); var bp = selectionModel.getSelection();
	 * 
	 * bpStore = Ext.data.StoreManager.lookup('BestellpositionDataStore');
	 * bpStore.remove(bp); bpStore.sync();
	 * 
	 * if (this.debug) console.log('Bp geloescht!'); },
	 * 
	 * deleteBestellung: function(btn) { var bestellungGridPanel =
	 * Ext.getCmp('BestellungGridPanel'); var selectionModel =
	 * bestellungGridPanel.getSelectionModel(); var bestellung =
	 * selectionModel.getSelection();
	 * 
	 * bestellungStore = Ext.data.StoreManager.lookup('BestellungDataStore');
	 * bestellungStore.remove(bestellung); bestellungStore.sync();
	 * 
	 * if (this.debug) console.log('Bestellung geloescht!'); },
	 */

	liefereDialog : function(btn, e, eOpts) {
		Ext.MessageBox.confirm('Best&auml;tigen', 'Auftrag sicher liefern?',
				Ext.Function
						.bind(this.liefere, null, [this.activeBestellnr], 1));
	},

	liefere : function(success, activeBestellnr) {
		console
				.log('Parameter1: ' + success + " Parameter2:"
						+ activeBestellnr);
		var request = Ext.Ajax.request({
					url : '/leanorders/auftragsbestaetigungs/liefere/'
							+ activeBestellnr,
					success : function(response) {
						var text = response.responseText;
					},
					failure : function(response) {
						// TODO: Fehlerhandling vereinheitlichen
						Ext.Msg
								.alert('Fehler', response.status
												+ response.text);
					}
				});
		if (this.debug)
			console.log('liefere');

		// Reload GridPanel
		MyApp.getApplication().getController('MyController').sleep(500);
		// Ext.data.StoreManager.lookup('BestellungDataStore').reload();
		grid = Ext.getCmp('BestellungGrid');
		grid.refresh();
		Ext.getCmp('RechnungBestellungButton').setDisabled(true);
		Ext.getCmp('BezahltBestellungButton').setDisabled(false);
		Ext.getCmp('RechnungPdf').setDisabled(false);
		// Falsch, da Status des Grid-Records immer noch auf Auftragbestaetigt
		// ist
		/*
		 * grid.fireEvent('selectionchange', grid.getSelectionModel(),
		 * grid.getSelectionModel().getSelection() );
		 */
	},

	showTransition : function(grid, record) {
		Ext.ComponentQuery.query('panel[extend=MyApp.view.TransitionWindow]');
		switch (record.data.status) {
			case "ORDERED" :
				this.getConfirmWindowView().create();
				Ext.ComponentQuery.query('panel[itemid=ConfirmWindow]')[0]
						.loadAndShow(record);
				break;
			case "CONFIRMED" :
				this.getDeliverWindowView().create();
				Ext.ComponentQuery.query('panel[itemid=DeliverWindow]')[0]
						.loadAndShow(record);
				break;
			case "SHIPPED" :
				this.getCompleteWindowView().create();
				Ext.ComponentQuery.query('panel[itemid=CompleteWindow]')[0]
						.loadAndShow(record);
				break;
			case "COMPLETED" :
				this.getTransitionWindowView().create();
				Ext.ComponentQuery.query('panel[itemid=TransitionWindow]')[0]
						.loadAndShow(record);
				break;
			case "CANCELED" :
				this.getTransitionWindowView().create();
				Ext.ComponentQuery.query('panel[itemid=TransitionWindow]')[0]
						.loadAndShow(record);
				break;

		}

	},

	/*
	 * loadBpForm: function(btn, e, eOpts) { var bestellung =
	 * this.getBestellungSelection();
	 * 
	 * var form = Ext.getCmp('ErstelleBpForm').getForm(); try {
	 * form.findField('bestellung').setValue(bestellung.getId()); var BpForm =
	 * Ext.getCmp('BpForm'); BpForm.show(); } catch (e){
	 * Ext.Msg.alert('Hinweis', 'Bevor eine eine Bestellposition erstellt werden
	 * kann, muss eine Bestellung ausgew&auml;hlt sein </br> Fehlerdetails:'+
	 * e.toString()); } },
	 */

	loadErstelleBestellungWindow : function(btn, e, eOpts) {
		console.log('function createBestellung');
		var bestellungWindow = Ext.getCmp('ErstelleBestellungWindow');
		var customerid = Ext.getCmp('mainCustomerComboBox').getValue();
		if (customerid == null)
			Ext.MessageBox.show({
						title : 'Keinen Kunden ausgew&auml;hlt',
						msg : "bitte Kunden ausw&auml;hlen",
						icon : Ext.MessageBox.INFO,
						buttons : Ext.Msg.OK
					});
		else {
			Ext.ComponentQuery.query('combobox[xtype=ordernumbercombobox]')[0].setValue("");
			store = Ext.data.StoreMgr.lookup('BestellpositionDataStore');
			bestellungWindow.show();

		}
	},

	/*
	 * getBestellungSelection: function(){ var bestellungGridPanel =
	 * Ext.getCmp('BestellungGridPanel'); var selectionModel =
	 * bestellungGridPanel.getSelectionModel(); var bestellung =
	 * selectionModel.getSelection()[0]; return bestellung; },
	 */

	syncBpGrid : function(view, owner, options) {
		if (this.debug)
			console.log('syncBpGrid');
		var bpDataStore = Ext.data.StoreManager
				.lookup('BestellpositionDataStore');
	},

	showBestellundPdf : function(button, event, options) {
		var win = window.open('/leanorders/orders/' + this.activeBestellnr
						+ '.pdf', '_blank');
		win.focus();
	},
	showAbPdf : function(button, event, options) {
		var win = window.open('/leanorders/orderConfirmations/'
						+ this.activeBestellnr + '.pdf', '_blank');
		win.focus();
	},
	showRechnungPdf : function(button, event, options) {
		var win = window.open('/leanorders/invoices/' + this.activeBestellnr
						+ '.pdf', '_blank');
		win.focus();
	},
	sleep : function(milliseconds) {
		var start = new Date().getTime();
		for (var i = 0; i < 1e7; i++) {
			if ((new Date().getTime() - start) > milliseconds) {
				break;
			}
		}
	},
	confirm : function(event, ocnr, record) {
		if (event == "ok") {
			console.log(record.data.product + " " + record.data.quantity + " "
					+ record.data.orderNumber);

			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/confirm/json',
						params : {
							productNumber : record.data.product,
							quantity : record.data.quantity,
							customer : record.data.customer,
							orderConfirmationNumber : ocnr
						},
						success : function(response) {
							var text = response.responseText;
						},
						failure : function(response) {
							// TODO: Fehlerhandling vereinheitlichen
							Ext.Msg.alert('Fehler', response.status
											+ response.text);
						}
					});
			if (this.debug)
				console.log('confirm order');

			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},
	
	deconfirm: function(event, ocnr, record) {
		if (event == "ok") {
			console.log(record.data.product + " " + record.data.quantity + " "
					+ record.data.orderNumber);

			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/deconfirm/json',
						params : {
							productNumber : record.data.product,
							quantity : record.data.quantity,
							customer : record.data.customer,
							orderConfirmationNumber : ocnr
						},
						success : function(response) {
							var text = response.responseText;
						},
						failure : function(response) {
							// TODO: Fehlerhandling vereinheitlichen
							Ext.Msg.alert('Fehler', response.status
											+ response.text);
						}
					});
			if (this.debug)
				console.log('deconfirm order');

			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},
	
	deliver:function(event, inr, record) {
		if (event == "ok") {
			console.log(record.data.product + " " + record.data.quantity + " "
					+ record.data.orderConfirmationNumber);

			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/deliver/json',
						params : {
							productNumber : record.data.product,
							quantity : record.data.quantity,
							customer : record.data.customer,
							invoiceNumber : inr
						},
						success : function(response) {
							var text = response.responseText;
						},
						failure : function(response) {
							// TODO: Fehlerhandling vereinheitlichen
							Ext.Msg.alert('Fehler', response.status
											+ response.text);
						}
					});
			if (this.debug)
				console.log('deliver order confirmation');

			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},
	
	complete:function(event, anr, record) {
		if (event == "ok") {
			console.log(record.data.product + " " + record.data.quantity + " "
					+ record.data.accountNumber);

			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/complete/json',
						params : {
							productNumber : record.data.product,
							quantity : record.data.quantity,
							customer : record.data.customer,
							accountNumber : anr
						},
						success : function(response) {
							var text = response.responseText;
						},
						failure : function(response) {
							// TODO: Fehlerhandling vereinheitlichen
							Ext.Msg.alert('Fehler', response.status
											+ response.text);
						}
					});
			if (this.debug)
				console.log('complete invoice');

			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},

	/*
	 * bestaetigeAuftragDialog : function(btn, e, eOpts) {
	 * console.log('bestaetigeAuftragDialog');
	 * Ext.MessageBox.prompt('Versandkosten', 'Bitte Versandkosten netto
	 * eingeben:', Ext.Function .bind(this.bestaetigeAuftrag, null,
	 * [this.activeBestellnr], 2)); },
	 * 
	 * bestaetigeAuftrag : function(status, versandkosten, activeBestellnr) {
	 * console.log('/leanorders/bestellungs/bestaetigeAuftrag/' +
	 * activeBestellnr + " Parameter2:" + versandkosten + " Parameter3:" +
	 * activeBestellnr); var request = Ext.Ajax.request({ url :
	 * '/leanorders/bestellungs/bestaetigeAuftrag/' + activeBestellnr, params : {
	 * versandNetto : versandkosten }, success : function(response) { var text =
	 * response.responseText; }, failure : function(response) { // TODO:
	 * Fehlerhandling vereinheitlichen Ext.Msg .alert('Fehler', response.status +
	 * response.text); } }); if (this.debug) console.log('bestaetigeAuftrag');
	 *  // Reload GridPanel
	 * MyApp.getApplication().getController('MyController').sleep(500);
	 * Ext.data.StoreManager.lookup('BestellungDataStore').reload(); grid =
	 * Ext.getCmp('BestellungGrid'); grid.refresh();
	 *  },
	 */

	onCustomerChange : function(field, newValue, oldValue, eOpts) {
		console.log('onCustomerChange');
		var stores = new Array();
		stores[0] = Ext.data.StoreManager.lookup('BestellpositionDataStore');
		stores[1] = Ext.data.StoreManager.lookup('ShippingItemDataStore');
		stores[2] = Ext.data.StoreManager.lookup('InvoiceItemDataStore');
		stores[3] = Ext.data.StoreManager.lookup('ArchiveItemDataStore');

		stores.forEach(function(store) {
					found = false;
					store.filters.items.forEach(function(filter) {
								if (filter.property == 'customer') {
									filter.value = newValue;
									found = true;
									store.load();
								}
							});
					if (!found) {
						store.filter("customer", newValue);
						// store.load();
					}
				});

	}

});
