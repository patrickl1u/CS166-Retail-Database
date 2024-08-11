CREATE INDEX store_product_list
ON Product
USING btree (storeID);

CREATE INDEX user_orders
ON Orders
USING btree (customerID);

CREATE INDEX store_orders
ON Orders
USING btree (storeID);

CREATE INDEX save_entry 
ON ProductUpdates
USING btree (storeID);