/*
 * File: app/model/BestellpositionData.js
 *
 * This file was generated by Sencha Architect version 2.2.2.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Ext JS 4.2.x library, under independent license.
 * License of Sencha Architect does not include license for Ext JS 4.2.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.model.BestellpositionData', {
	extend: 'Ext.data.Model',
	fields: 
		[
		 {
			 name: 'id'
		 },
		 {
			 name: 'product',
			 type: 'int'
		 },
 		 {
			 name: 'productName'
		 },
		 {
			 name: 'customer',
			 type: 'int'
		 },
		 {
			 name: 'customerName'
		 },
		 {
			 name: 'orderNumber'
		 }, 
		 {
			 name: 'invoiceNumber'
		 }, 
		 {
			 name: 'orderConfirmationNumber'
		 },
		 {
			 name: 'deliveryNotesNumber'
		 },
		 {
			 name: 'documentNumber'
		 },
		 {
			 name: 'receiptNumber'
		 },
		 {
			 name: 'quantity',
			 type: 'int'
		 },
		 {
			 name: 'quantityLeft',
			 type: 'int'
		 },
		 {
			 name: 'priceNet'
		 },
		 {
			 name: 'status'
		 },
		 {
			 name: 'expectedDelivery',
			 format : 'd/m/Y'
		 },
		 {
			 name: 'created',
			 format : 'd/m/Y'
		 }

		 ]
});