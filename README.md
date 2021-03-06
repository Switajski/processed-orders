FlexibleOrders <a href="https://travis-ci.org/Switajski/FlexibleOrders">![Build Status](https://travis-ci.org/Switajski/FlexibleOrders.svg)</a> 
==============

FlexibleOrders is a java web application for creating and tracking orders, invoices, shipping info, etc.  
The intention of this application is to ease the work of start-up companies by a simple user interface and easy integration with e-commerce systems.  
Used technologies are Spring, Hibernate and ExtJs in GUI. The domain model is depicted with POJOs.

Intention
---------
FlexibleOrders does not have the claim to manage customers relationships or product information. No product catalog and no customer database is used. It's intention is get customers and products from already existing systems and fit into a distribution / e-commerce system landscape like a microservice. It's defined "bounded context" is strictly defined to the order process and document creation with a clear API.

The usage of "Event Sourcing" as a core idea makes the order process tracebale, reflects B2B processes as well as B2C without complex logic and makes them interchangeble in time of processing.

Getting started
---------------
FlexibleOrders is developed with Eclipse. In order to get to the code stuff run from command line: 

`git clone git@github.com:Switajski/FlexibleOrders.git`

`mvn eclipse:eclipse`

and import the just created git repository as "existing project" to eclipse. The database can be created by modifying persistence.xml and changing the line:
`<property name="hibernate.hbm2ddl.auto" value="update"/>`
to 
`<property name="hibernate.hbm2ddl.auto" value="create"/>`
and start the application server with a running DB. PostgreSQL is preconfigured.

Run application
---------------
After having created a postgres database with valid connection settings in database.properties, just run:

`mvn tomcat7:run`

Contribute
----------
Just send me an email: marek@switajski.de

License
-------
This application is using ExtJs. The licensing model of this application is bound to ExtJs open source license (GPL license v3 - http://www.sencha.com/products/extjs/license/)

    Copyright 2012 Markus Switajski
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
