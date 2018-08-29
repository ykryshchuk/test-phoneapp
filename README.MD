
# Phone App

## Assumptions

The application is designed with support of pricing changes. When the order is placed, the real prices from phones catalog are used to create the order entries.
That makes possible to still change the live prices over a time, but the prices on order entries will remain as originally shown to the customer during the order placement.

Both services have their own access to the database. Just for testing purposes I have created a single database and single user is granted access to everything. 
In theory the phones catalog could be persisted in NoSQL and the orders (with entries) in relational database.

For simplicity of the pricing model I use the long values to represent the prices. These values correspond to the cents (secondary currency unit), so that for instance price 15990 means 159 Euro and 90 cents. It would be a responsibility of presentation layer to format the price according to its needs.

The phone name is considered as unique business key of the Phone entities.

There are some javadocs and inline comments on key classes with more explanation.

## Build

In order to build the docker images issue the following

```mvn clean package```

This will create images for the system:

* Database (MySQL with predefined database, user, tables and some records on tables for test data)
* Phones Catalog (the services to get phone list or single phone, expects the database as env vars - for instance, docker link)
* Orders Book (the services to get or place an order, expects the database as env vars - for instance, docker link. and expects the phones catalog same via the env vars)


## Test

You may benefit from the maven here too.

```mvn clean verify```

This will compile applications. Run some unit tests for both microservices. 
Then it will create a test database container with some records. And it will create the two containers for both microservices. 
References to database are done using the docker links. Same applies to reference from orders book to phones catalog.
Once containers created and started, maven will run the integration tests. Finally shut down the containers.

Have fun.

Oh, you need a running docker service locally and your user should have access to it.


## Improvements

It seems that the system is missing the Customers Registry. A dedicated CRM Service can be added to register customers and then just refer those in orders.

For more flexible handling of the order placement I would add a ShoppingCart entity, which could be modified by user as adding or removing entries (as individual calls to API). Then the order placement would be just a reference to a cart. The customer assignment to the cart could be also done either on cart creation or order placement (that depends how the UI is designed, so the customer can be already logged in, that way we know who is it and can immediately assign to the cart).

The requirements for phones catalog is very weak, the service to list all phones should support pagination (implemented the concept).

