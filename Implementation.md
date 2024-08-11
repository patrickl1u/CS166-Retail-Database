# CS166 Project

## Requirements

TODO: in general, we still need to do some DB tuning...not clear what kind of tuning at this moment...

TODO: gui
- show plot of all stores
  - click on store to show its products
  - clicking on product opens checkout dialogue
- show recent orders
- modify profile
- should be from customer perspective only

### Users/Customers

Customers can:
- view products in each store
- check price and availability
- place an order
- view order history
- visit profile
- update profile

##### Purchasing product

Only allow if:
- units ordered is available in store
- store within 20 miles distance


## Assumptions

### General
---

#### Users

A User can be both a Customer and a Manager.

##### Customers

Customers have an attribute which holds their credit score. Additionally, customers can update their profile information

TODO: add credit score in as an attribute
TODO: allow customers to update profile info
TODO: show all of a customer's orders (not just 5 most recent)

##### Managers

Managers can update any product at any store. See `updateProduct()` under Functions for more details.

TODO: allow managers to add/delete products

### Functions

#### `viewStores()`

It is assumed that the coordinates correspond to a 100 mi x 100 mi square grid. For our range of values from `0-100`, `1` is defined as one mile. This can be changed by changing `double ONEMILE`. 

#### `updateProduct()`

It is assumed that all managers are trustworthy. Managers are able to manage products at different stores. We assume this because all updates to products are recorded and auditable. With great power comes great responsibility; these managers can be held liable for their actions.

#### `viewPopularProducts()`

It is assumed that a product is "popular" if it has been purchased many times. We do not distinguish between unique individuals as people could be purchasing supplies on a regular basis. 

#### `placeProductSupplyRequests()`

It is assumed that all warehouses will have the supply to fulfill requests.

We do not actually "fulfill" any orders as we do not know which orders have been fulfilled or which ones have not. This would require an additonal attribute to determine if orders have been succesfully delivered to a store or not.

TODO: implement this above feature, function should be something like ingestShipment()