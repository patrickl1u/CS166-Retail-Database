CREATE OR REPLACE LANGUAGE PLPGSQL;

DROP SEQUENCE IF EXISTS newOrderRecordFunction;
CREATE SEQUENCE newOrderRecordFunction;
CREATE OR REPLACE FUNCTION ordernumber_trigger_function()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.orderNumber:=nextval('newOrderRecordFunction');
RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS ordernumber_trigger ON Orders;
CREATE TRIGGER ordernumber_trigger
BEFORE INSERT
ON Orders
FOR EACH ROW
EXECUTE PROCEDURE ordernumber_trigger_function();



DROP SEQUENCE IF EXISTS supplyOrderSequence;
CREATE SEQUENCE supplyOrderSequence;
CREATE OR REPLACE FUNCTION supplyOrderRequestNumber()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.requestNumber:=nextval('supplyOrderSequence');
RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS supplyordernumber_trigger ON Orders;
CREATE TRIGGER supplyordernumber_trigger
BEFORE INSERT
ON ProductSupplyRequests
FOR EACH ROW
EXECUTE PROCEDURE supplyOrderRequestNumber();



DROP SEQUENCE IF EXISTS productUpdateSequence;
CREATE SEQUENCE productUpdateSequence;
CREATE OR REPLACE FUNCTION productUpdateNumber()
RETURNS "trigger" AS
$BODY$
BEGIN
NEW.updateNumber:=nextval('productUpdateSequence');
RETURN NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

DROP TRIGGER IF EXISTS productUpdatenumber_trigger ON Orders;
CREATE TRIGGER productUpdatenumber_trigger
BEFORE INSERT
ON ProductUpdates
FOR EACH ROW
EXECUTE PROCEDURE productUpdateNumber();



CREATE OR REPLACE FUNCTION updateProductAfterSupplyOrder()
RETURNS "trigger" AS
$BODY$
BEGIN
    UPDATE Product P
    SET numberOfUnits = P.numberOfUnits + NEW.unitsRequested
    WHERE P.productName = NEW.productName AND P.storeID = NEW.storeID;
RETURN NULL;
END; 
$BODY$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS updateaftersupplyorder_trigger ON Orders;
CREATE TRIGGER updateaftersupplyorder_trigger
AFTER INSERT
ON ProductSupplyRequests
FOR EACH ROW
EXECUTE PROCEDURE updateProductAfterSupplyOrder();