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
    /*uses: [
        'MyApp.model.Kunde'
    ],
    hasOne: {
        model: 'MyApp.model.Kunde'
    }*/
    fields: [
        {
            name: 'id'
        },
        {
            name: 'productNumber',
            type: 'int'
        },
        {
            name: 'orderNumber',
            type: 'int'
        },
        {
            name: 'invoiceNumber'
        },
        {
            name: 'orderConfirmationNumber'
        },
        {
            name: 'quantity',
            type: 'int'
        },
        {
            name: 'priceNet'
        },
        {
            name: 'status'
        },
        {
            name: 'expectedDelivery'
        }
    ]
});