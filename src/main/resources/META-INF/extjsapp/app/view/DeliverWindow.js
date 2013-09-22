Ext.define('MyApp.view.DeliverWindow', {
	extend : 'Ext.window.Window',
	title : 'Auftrag liefern',
	itemid : 'DeliverWindow',
	alias : 'widget.DeliverWindow',
	layout : 'fit',
	defaultInvoiceNumber : 0,
	record : null,
	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			layout : 'anchor',
			items : [{
						xtype : 'form',
						id : 'DeliverForm',
						bodyPadding : 10,
						items : [{
									xtype : 'fieldset',
									title : 'Inhalt',
									flex : 1,
									items : [{
												xtype : 'displayfield',
												anchor : '100%',
												name : 'product',
												fieldLabel : 'Artikel'
											}, {
												xtype : 'displayfield',
												anchor : '100%',
												name : 'customer',
												fieldLabel : 'Kunde'
											}, {
												xtype : 'numberfield',
												fieldLabel : 'Menge',
												name : 'quantity',
												allowBlank : false,
												allowDecimals : false,
												allowExponential : false,
												minValue : 1,
												maxValue : this.record.data.quantity
											}]
								}, {
									xtype : 'fieldset',
									title : 'Lieferadresse',
									flex : 1,
									items : [{
												xtype : 'textfield',
												anchor : '100%',
												name : 'shippingName1',
												fieldLabel : 'Name 1',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'shippingName2',
												fieldLabel : 'Name 2',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'shippingStreet',
												fieldLabel : 'Strasse',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'shippingPostalCode',
												fieldLabel : 'PLZ',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'shippingCity',
												fieldLabel : 'Stadt',
												allowBlank : false
											}, {
												xtype : 'textfield',
												anchor : '100%',
												name : 'shippingCountry',
												fieldLabel : 'Land',
												allowBlank : false
											}]
								}, {
									xtype : 'fieldset',
									title : 'zus&auml;tzliche Informationen',
									flex : 1,
									items : [{
												itemid : 'invoiceNumber',
												xtype : 'invoicenumbercombobox',
												name : 'invoiceNumber',
												fieldLabel : 'Rechnungsnr',
												allowBlank : false,
												allowDecimals : false,
												allowExponential : false
											}, {
												xtype : 'numberfield',
												fieldLabel : 'Paketnr',
												name : 'packageNumber',
												allowBlank : true,
												allowDecimals : false,
												allowExponential : false,
												minValue : 1
											}, {
												xtype : 'numberfield',
												fieldLabel : 'Sendungsnr',
												name : 'trackNumber',
												allowBlank : true,
												allowDecimals : false,
												allowExponential : false,
												minValue : 1
											}]
								}

						],

						dockedItems : [{
									xtype : 'toolbar',
									dock : 'bottom',
									ui : 'footer',
									items : ['->', {
												iconCls : 'icon-save',
												itemId : 'save',
												text : 'Speichern',
												disabled : false,
												scope : this,
												handler : this.onSave
											}]
								}]

					}

			]
		});

		me.callParent(arguments);
		if (this.record == null)
			console.error('no record set!');

		var invoiceItem = Ext.create('MyApp.model.InvoiceItemData', {
					product : this.record.data.product,
					customer : this.record.data.customer,
					invoiceNumber : this.record.data.orderConfirmationNumber,
					quantity : this.record.data.quantity,
					shippingCity : this.record.data.shippingCity,
					shippingCountry : this.record.data.shippingCountry,
					shippingName1 : this.record.data.shippingName1,
					shippingName2 : this.record.data.shippingName2,
					shippingPostalCode : this.record.data.shippingPostalCode,
					shippingStreet : this.record.data.shippingStreet
				})

		this.down('form').getForm().loadRecord(invoiceItem);

		// set the listeners to update record onChange
		this.down('form').items.each(function(item) {
			item.items.each(function(ii) {
				console.log(ii);
				ii.on({
					change : function() {
						var window = Ext.ComponentQuery.query('DeliverWindow')[0];
						window.down('form').getForm()
								.updateRecord(window.record);
					}
				});
			});
		});
	},
	/**
	 * this method is to override by the using Component (usually Panel)
	 */
	updateRecord : function() {
		console.log('Override me!');
	},

	//TODO: remove setter and getter
	setInvoiceNumber : function(invoiceNumber) {
		this.down('form').down('invoicenumbercombobox').setValue(invoiceNumber);
		this.record.data.invoiceNumber = invoiceNumber;
	},
	setQuantity : function(quantity) {
		this.down('form').down('numberfield[name=quantity]').setValue(quantity);
	},
	setPackageNumber : function(packageNumber) {
		this.down('form').down('numberfield[name=packageNumber]')
				.setValue(packageNumber);
	},
	setTrackNumber : function(trackNumber) {
		this.down('form').down('numberfield[name=trackNumber}')
				.setValue(trackNumber);
	},
	setProduct : function(product) {
		this.down('form').down('displayfield[name=product]').setValue(product);
	},
	setCustomer : function(customer) {
		this.down('form').down('displayfield[name=customer]')
				.setValue(customer);
		// alternativ:
		// Ext.ComponentQuery.query('DeliverWindow
		// displayfield[name=customer]')[0].setValue(product);
	},
	getInvoiceNumber : function(invoiceNumber) {
		this.down('form').down('invoicenumbercombobox').getValue(invoiceNumber);
	},
	getQuantity : function(quantity) {
		this.down('form').down('numberfield[name=quantity]').getValue(quantity);
	},
	getPackageNumber : function(packageNumber) {
		this.down('form').down('numberfield[name=packageNumber]')
				.getValue(packageNumber);
	},
	getTrackNumber : function(trackNumber) {
		this.down('form').down('numberfield[name=trackNumber}')
				.getValue(trackNumber);
	},
	getProduct : function(product) {
		this.down('form').down('displayfield[name=product]').getValue(product);
	},
	getCustomer : function(customer) {
		this.down('form').down('displayfield[name=customer]')
				.getValue(customer);
	},
	/**
	 * this method listens to the save button and is usually overridden by a panel.
	 * see {@Link MyController.deliver}
	 */
	onSave : function() {
		var active = this.activeRecord, form = this.getForm();

		if (!active) {
			return;
		}
		if (form.isValid()) {
			form.updateRecord(active);
			this.onReset();
		}
	},
	onComboboxChange : function() {
		if (record != null) {
			var combobox = Ext.ComponentQuery.query('invoicenumbercombobox')[0];
			this.record.data.invoiceNumber = combobox.getValue();
			console.log('new value' + combobox.getValue());
		}
	}

});