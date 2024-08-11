/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

// need date time
import java.text.SimpleDateFormat;
import java.util.Date;

// https://mkyong.com/java/java-display-double-in-2-decimal-points/
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Retail {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   // try to save id of logged in user
   public static int userID = -1;
   // unit of one mile
   // our 100x100 grid coordinate system does not define mile
   // we can define what one mile is using this variable
   public static int ONEMILE = 1;

   /**
    * Creates a new instance of Retail shop
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Retail(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Retail

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
       if(outputHeader){
         for(int i = 1; i <= numCol; i++){
         System.out.print(rsmd.getColumnName(i) + "\t");
         }
         System.out.println();
         outputHeader = false;
       }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
      for (int i=1; i<=numCol; ++i)
         record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
   Statement stmt = this._connection.createStatement ();

   ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
   if (rs.next())
      return rs.getInt(1);
   return -1;
   }

   /**String getx1 = String.format("SELECT longitude FROM Store WHERE storeID = '%s'", storeID);
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**quantity
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Retail.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Retail esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Retail object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Retail (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            // String authorisedUser = null;
            int authorisedUser = -1;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); userID = authorisedUser; break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser > 0) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Stores within 30 miles");
                System.out.println("2. View Product List");
                System.out.println("3. Place a Order");
                System.out.println("4. View 5 recent orders");

                //the following functionalities basically used by managers
                System.out.println("Manager Functions: ");
                System.out.println("---------");
                System.out.println("5. Update Product");
                System.out.println("6. View 5 recent Product Updates Info");
                System.out.println("7. View 5 Popular Items");
                System.out.println("8. View 5 Popular Customers");
                System.out.println("9. Place Product Supply Request to Warehouse");
                //added
                System.out.println("10. View All Orders Managers Manages ");
                // functions used by Admin
                //System.out.println("16.  View  All Products");
                System.out.println("18. Admin View Users");
                System.out.println("19. Admin Update Users");
                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewStores(esql); break;
                   case 2: viewProducts(esql); break;
                   case 3: placeOrder(esql); break;
                   case 4: viewRecentOrders(esql); break;
                   case 5: updateProduct(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewPopularProducts(esql); break;
                   case 8: viewPopularCustomers(esql); break;
                   case 9: placeProductSupplyRequests(esql); break;
                   //Added 
                   case 10: viewOrders(esql); break;
                   case 18: viewUsers(esql); break;
                   case 19: updateUsers(esql); break;

                   case 20: usermenu = false; authorisedUser = -1; userID = authorisedUser; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface                      \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine();       //enter lat value between [0.0, 100.0]
         System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         String longitude = in.readLine();
         
         String type="Customer";

         String query = String.format("INSERT INTO USERS (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   // /*
   //  * Check log in credentials for an existing user
   //  * @return User login or null is the user does not exist
   //  **/
   // public static String LogIn(Retail esql){
   //    try{
   //       System.out.print("\tEnter name: ");
   //       String name = in.readLine();
   //       System.out.print("\tEnter password: ");
   //       String password = in.readLine();

   //       String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
   //       int userNum = esql.executeQuery(query);
   //  if (userNum > 0)
   //    return name;
   //       return null;
   //    }catch(Exception e){
   //       System.err.println (e.getMessage ());
   //       return null;
   //    }
   // }//end

/*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   // return user ID instead of username
   public static int LogIn(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT userID FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
         // int userNum = esql.executeQuery(query);
         List<List<String>> userstuff = esql.executeQueryAndReturnResult(query);
         int userNum = Integer.parseInt(userstuff.get(0).get(0));
         if (userNum > 0)
            return userNum;
         // should never come down to this but unique negative number 
         // so we can identify if it does come down to it
         return -2;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return -3;
      }
   }//end

// Rest of the functions definition go in here

   public static int isAuthorized(Retail esql){
      try{
         // check if user is customer, manager, or admin
         // 0: customer
         // 1: manager
         // 2: admin
         int curuser = esql.userID;
         if(curuser < 1){
            // 0 or below, which it really should only be -1 if anything
            // nobody logged in, we shouldn't be running this at all
            return -1;
         }
         String getUserPermissions = String.format("SELECT type FROM Users WHERE userID = '%s'", curuser);
         List<List<String>> userPermissions = esql.executeQueryAndReturnResult(getUserPermissions);
         // System.out.println(userPermissions);
         String userStatus = userPermissions.get(0).get(0);
         userStatus = userStatus.replaceAll("\\s", "");
         // System.out.println(userStatus);
         if(userStatus.equals("customer")){
            return 0;
         }
         if(userStatus.equals("manager")){
            return 1;
         }
         if(userStatus.equals("admin")){
            return 2;
         }
         // we should not get here
         return -2;
         }
      catch(Exception e){
         System.err.println(e.getMessage());
         return -3;
      }
   }

   // just to make life easier
   public static String getResponse(Retail esql, String message){
      
      System.out.print(message);
      String userinput = "";
      try{
         userinput = in.readLine();
      }
      catch(Exception e){
         System.err.println(e.getMessage());
         return "something bad happened in getResponse...";         
      }
      return userinput;
   }

   // just to make life easier
   public static String getNonEmptyResponse(Retail esql, String message){
      String userinput = "";
      while(true){
         System.out.print(message);
         try{
            userinput = in.readLine();
         }
         catch(Exception e){
            System.err.println(e.getMessage());
            return "something bad happened in getNonEmptyResponse...";
         }
         if(!userinput.isEmpty()){
            return userinput;
         }
         System.out.println("\tInput cannot be empty!");
      }
      // return "something bad happened in getNonEmptyResponse...";
      // java doesn't care about unreachable code...
   }

   // losing steam...
   public static String pickWarehouse(Retail esql){
      // try except statements will be the end of me
      try{
         String getall = String.format("SELECT WarehouseID FROM Warehouse");
         // esql.executeQueryAndPrintResult(getall);
         // print manually
         List<List<String>> whlist = esql.executeQueryAndReturnResult(getall);
         System.out.print("\tWarehouse ID's: ");
         for(int i = 0; i < whlist.size()-1; i++){
            System.out.print(String.format("%s, ", whlist.get(i).get(0)));
         }
         System.out.println(whlist.get(whlist.size()-1).get(0));
         String warehouseID = "";
         BITCH:
         while(true){
            // System.out.println("\tEnter Warehouse ID: ");
            warehouseID = getNonEmptyResponse(esql, "\tEnter Warehouse ID: ");
            for(int i = 0; i < whlist.size(); i++){
               if(warehouseID.equals(whlist.get(i).get(0))){
                  // break BITCH;
                  return warehouseID;
               }
            }
            System.out.println("\tPlease pick a valid warehouse!");
         }
      }
      catch(Exception e){
         System.err.println(e.getMessage());
         return "";
      }
      // return "this should probbly not be returned...";
   }

   // given a list of stores
   // ensure that store is picked from list
   // more generic output for user
   public static String userSelectStore(Retail esql, List<List<String>> listofstoreids){
      String storeID = "";
      while(true){
         System.out.print("\tStores: ");
         // print all but last element
         for(int i = 0; i < listofstoreids.size() - 1; i++){
            System.out.print(String.format("%s, ", listofstoreids.get(i).get(0)));
         }
         System.out.println(listofstoreids.get(listofstoreids.size()-1).get(0));
         storeID = getNonEmptyResponse(esql, "\tEnter StoreID: ");
         for(int i = 0; i < listofstoreids.size(); i++){
            if(storeID.equals(listofstoreids.get(i).get(0))){
               return storeID;
            }
         }
         System.out.println("\tPlease pick a valid store!");
      }
   }

   // given a list of stores
   // ensure that store is picked from list
   public static String managerSelectStore(Retail esql, List<List<String>> managerstores){
      while(true){
         String storeID = "";
         System.out.print("\tManaged stores: ");
         for(int i = 0; i < managerstores.size() - 1; i++){
            System.out.print(String.format("%s, ", managerstores.get(i).get(0)));
         }
         System.out.println(managerstores.get(managerstores.size()-1).get(0));
         // System.out.print("\tEnter StoreID: ");
         System.out.println("\tLeave blank to operate across all managed stores.");
         storeID = getResponse(esql, "\tEnter StoreID: ");

         if(storeID.isEmpty()){
            return storeID;
         }

         for(int i = 0; i < managerstores.size(); i++){
            if(storeID.equals(managerstores.get(i).get(0))){
               return storeID;
            }
         }
         System.out.println("\tYou don't manage this store! Pick a different store...");
      }
      
   }

   // check user product selection
   // see if name actually returns
   public static boolean validProductSelection(Retail esql, String userinput){
      String thingy = String.format("SELECT * FROM Product WHERE productName = '%s'", userinput);
      try{
         int rowthing = esql.executeQuery(thingy);
         // does not error if exists
         // return true;
         if(rowthing == 0){
            return false;
         }
         return true;
      }
      catch(Exception e){
         return false;
      }
   }

   public static void viewStores(Retail esql){
      // return stores within 30 miles of USER'S LOCATION!!!!!
      // idea:
      // take coordinates of USER LOCATION
      // take coordinates of a store
      // vector between store and USER LOCATION should be \leq 30 miles
      // https://www.movable-type.co.uk/scripts/latlong.html
      // easier than real thing
      // assume that long/lat correspond to grid 100 x 100 miles

      // no idea what to set as one mile
      // use easiest/simplest unit for now
      // double esql.ONEMILE = 1;
      // moved to construcvtor
      // change as desired later
      try{
         // USE USER'S LOCATION INSTEAD!!!
         // System.out.print("\tEnter StoreID: ");
         // String storeID = in.readLine();

         // System.out.println(String.format("\tUser: %s", esql.userID));

         System.out.println(String.format("\tShowing all stores within 30 miles of user..."));
         // define unit for a mile M
         // get longitude/latitude of USER
         // let this be x1, y1
         // for each store and its long/lat x2, y2
         //    sqrt((x2-x1)^2 + (y2-y1)^2)
         //    let this result be a
         // if a \leq M include this store

         // throws complaint about calling non-static from static context...
         // call using 'esql'

         // https://stackoverflow.com/questions/7309121/preferred-order-of-writing-latitude-longitude-tuples-in-gis-services
         // latitude, longitude
         // String getx1 = String.format("SELECT latitude FROM Store WHERE storeID = '%s'", storeID);
         // String gety1 = String.format("SELECT longitude FROM Store WHERE storeID = '%s'", storeID);
         String getx1 = String.format("SELECT latitude FROM Users WHERE userID = '%s'", esql.userID);
         String gety1 = String.format("SELECT longitude FROM Users WHERE userID = '%s'", esql.userID);
         
         
         // nice, function provided to do this...
         // double calculateDistance(double, double, double, double)
         // System.out.println(esql.executeQueryAndReturnResult(getx1));
         List<List<String>> x1thing = esql.executeQueryAndReturnResult(getx1);
         List<List<String>> y1thing = esql.executeQueryAndReturnResult(getx1);
         double x1 = Double.parseDouble(x1thing.get(0).get(0));
         double y1 = Double.parseDouble(y1thing.get(0).get(0));

         // got the main store, now we want every other store's coordinates
         // EXCLUDE store we picked
         String getall = String.format("SELECT storeID, latitude, longitude FROM Store");
         List<List<String>> allthing = esql.executeQueryAndReturnResult(getall);

         // so messy...
         for(int i = 0; i < allthing.size(); i++){
            List<String> tmp = allthing.get(i);
            // list of elements at this point
            // 0: storeID
            // 1: latitude
            // 2: longitude
            double x2 = Double.parseDouble(tmp.get(1));
            double y2 = Double.parseDouble(tmp.get(2));
            double dist = esql.calculateDistance(x1, y1, x2, y2);

            if(dist <= 30*esql.ONEMILE){
               // ghetto round to one decimal place to look nicer
               dist = dist*10;
               dist = (double)((int) dist);
               dist = dist/10;
               System.out.println(String.format("\tStore %s: %s miles away", tmp.get(0), dist));
            }
         }
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void viewProducts(Retail esql){
      // view products offered at store
      try{
         System.out.println("\tShowing products at a store...");
         System.out.println("\tLeave blank to search across all stores.");
         String storesoktouse = "SELECT storeID FROM Store";
         List<List<String>> listofstoreids = esql.executeQueryAndReturnResult(storesoktouse);

         // System.out.print("\tEnter StoreID: ");
         // String storeID = in.readLine();
         String storeID = userSelectStore(esql, listofstoreids);

         String query = "";
         if(storeID.isEmpty()){
            query = String.format("SELECT productName, numberOfUnits, pricePerUnit FROM Product");
         }
         else{
            query = String.format("SELECT productName, numberOfUnits, pricePerUnit FROM Product WHERE storeID ='%s'", storeID);
         }
         
         int rowCount = esql.executeQueryAndPrintResult(query);  
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }
   public static void placeOrder(Retail esql){
      try{
         System.out.println("\tPlacing an order...");
         esql.viewStores(esql);
         
         List<List<String>> closestores = new ArrayList<>();
         // ghetto
         // calculating nearest stores again
         // we only do this twice and dont really wanna recode viewStores()
         // lifted from viewStores()
         String getx1 = String.format("SELECT latitude FROM Users WHERE userID = '%s'", esql.userID);
         String gety1 = String.format("SELECT longitude FROM Users WHERE userID = '%s'", esql.userID);
         List<List<String>> x1thing = esql.executeQueryAndReturnResult(getx1);
         List<List<String>> y1thing = esql.executeQueryAndReturnResult(getx1);
         double x1 = Double.parseDouble(x1thing.get(0).get(0));
         double y1 = Double.parseDouble(y1thing.get(0).get(0));
         String getall = String.format("SELECT storeID, latitude, longitude FROM Store");
         List<List<String>> allthing = esql.executeQueryAndReturnResult(getall);
         for(int i = 0; i < allthing.size(); i++){
            List<String> tmp = allthing.get(i);
            double x2 = Double.parseDouble(tmp.get(1));
            double y2 = Double.parseDouble(tmp.get(2));
            double dist = esql.calculateDistance(x1, y1, x2, y2);

            if(dist <= 30*esql.ONEMILE){
               // ghetto round to one decimal place to look nicer
               dist = dist*10;
               dist = (double)((int) dist);
               dist = dist/10;
               closestores.add(tmp);
            }
         }

         String storeID = userSelectStore(esql, closestores);

         String query = String.format("SELECT productName, numberOfUnits, pricePerUnit FROM Product WHERE storeID = '%s'", storeID);
         // print store's product list
         esql.executeQueryAndPrintResult(query);

         // product name loop
         String productToBuy = "";
         while(true){
            System.out.print("\tEnter product choice: ");
            productToBuy = in.readLine();
            if(validProductSelection(esql, productToBuy)){
               break;
            }
            System.out.println("\tPlease enter a valid product name!");
         }

         String qtyToBuya = "";
         int qtyToBuy = -1;
         while(true){
            System.out.print("\tEnter QTY to purchase: ");
            qtyToBuya = in.readLine();
            qtyToBuy = Integer.parseInt(qtyToBuya);
            if(qtyToBuy < 1){
               System.out.println("\tPlease enter a value larger than 0!");
            }
            else{
               break;
            }
         }
         

         // do checks to see if valid order
         
         query = String.format("SELECT * FROM Product WHERE storeID = '%s' AND productName = '%s'", storeID, productToBuy);
         // if user typed product name incorrect
         //    we error out here and return to main menu
         List<List<String>> thingy = esql.executeQueryAndReturnResult(query);
         // check order qty is available
         //    0: storeID
         //    1: productName
         //    2: numberOfUnits
         //    3: pricePerUnit

         int qtyAvailable = Integer.parseInt(thingy.get(0).get(2));

         while(qtyToBuy > qtyAvailable){
            System.out.println("\tQTY requested not available!");
            System.out.println("\tLeave blank to exit to main menu.");
            System.out.print("\tEnter QTY to purchase: ");
            qtyToBuya = in.readLine();
            if(qtyToBuya.isEmpty()){
               return;
            }
            qtyToBuy = Integer.parseInt(qtyToBuya);
         }
         
         // create record of order
         // need next order number
         // uses trigger now
         // String getreqnum = "SELECT orderNumber FROM Orders ORDER BY orderNumber DESC FETCH FIRST 1 ROWS ONLY";
         // List<List<String>> lastusedreqnum = esql.executeQueryAndReturnResult(getreqnum);

         // int reqnum = Integer.parseInt(lastusedreqnum.get(0).get(0));
         // reqnum = reqnum + 1;

         // need order time
         SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date date = new Date(System.currentTimeMillis());

         // insert record of order
         // query = String.format("INSERT INTO Orders VALUES(%s, %s, %s, '%s', %s, '%s')", reqnum, esql.userID, storeID, productToBuy, qtyToBuy, formatter.format(date));
         query = String.format("INSERT INTO Orders (customerID, storeID, productName, unitsOrdered, orderTime) VALUES(%s, %s, '%s', %s, '%s')", esql.userID, storeID, productToBuy, qtyToBuy, formatter.format(date));
         esql.executeUpdate(query);

         // subtract purchased items from DB
         query = String.format("UPDATE Product SET numberOfUnits = %s WHERE storeID = '%s' AND productName = '%s'", qtyAvailable - qtyToBuy, storeID, productToBuy);
         esql.executeUpdate(query);

         double subprice = Double.parseDouble(thingy.get(0).get(3))*qtyToBuy;
         
         DecimalFormat df = new DecimalFormat("0.00");
         df.setRoundingMode(RoundingMode.UP);

         String subtotal = String.format("\tExpected price for order: $%s", df.format(subprice));
         System.out.println(subtotal);
         System.out.println("\tOrder placed!");
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void viewRecentOrders(Retail esql){
      // show 5 most recent orders
      // return last 5 orders? simplest method
      // order number incremented as orders come in
      // logically follows that the largest order number is the most recent order number
      // return largest five order numbers that match NOT storeID BUT userID!!!
      try{
         System.out.println("\tUser's 5 most recent orders:");
         // this query returns all orders with matching order numbers
         // String query = String.format("SELECT * FROM Orders WHERE storeID = '%s'", storeID);

         // fetch first X rows in postgresql
         // https://stackoverflow.com/questions/13674031/how-to-get-the-top-10-values-in-postgresql

         // want bottom rows
         // https://stackoverflow.com/questions/1876606/how-to-select-bottom-most-rows

         // given a storeID, return the last 5 orders made to that store
         // String query = String.format("SELECT * FROM (SELECT * FROM Orders WHERE customerID = '%s' ORDER BY orderNumber DESC) SQ ORDER BY orderNumber DESC FETCH FIRST 5 ROWS ONLY", esql.userID);
         String query = String.format("SELECT * FROM (SELECT * FROM Orders WHERE customerID = '%s' ORDER BY orderNumber DESC FETCH FIRST 5 ROWS ONLY) SQ ORDER BY orderNumber ASC", esql.userID);

         int rowCount = esql.executeQueryAndPrintResult(query);
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   // all of these below are manager only functions

   // refactoring
   public static void updateProduct(Retail esql){
      // allow only if admin or is manager of store
      // admin can do whatever
      // manager can update products of their managed store only
      System.out.println("\tUpdating a product at a store...");

      int userstatus = esql.isAuthorized(esql);
      if(userstatus == 0){
         System.out.println("\tUser is not authorized!!!");
         return;
      }
      String productName = "";
      while(true){
         productName = esql.getNonEmptyResponse(esql, "\tEnter Product Name: ");
         if(validProductSelection(esql, productName)){
            break;
         }
         else{
            System.out.println("\tPlease enter a valid product name!");
         }
      }
      

      List<List<String>> listofstoreids;
      String userentry = "";
      
      String storeID = "";
      String query = "";
      if(userstatus == 2){
         storeID = getResponse(esql, "\tADMIN: Enter StoreID: ");
      }
      else{
         // get manager's store ID's
         query = String.format("SELECT storeID FROM Store WHERE managerID = '%s'", esql.userID);
         try{
            listofstoreids = esql.executeQueryAndReturnResult(query);
            storeID = managerSelectStore(esql, listofstoreids);
         }
         catch(Exception e){
         }
      }

      System.out.println("\tPlease select which column to update: ");
      // a little too permissive
      // System.out.println("\t\t0: storeID");
      // System.out.println("\t\t1: productName");
      // System.out.println("\t\t2: numberOfUnits");
      // System.out.println("\t\t3: pricePerUnit");
      System.out.println("\t\t0: numberOfUnits");
      System.out.println("\t\t1: pricePerUnit");
      
      String choice = getNonEmptyResponse(esql, "\tEnter selection: ");
      while(!(choice.equals("0") || choice.equals("1"))){
         System.out.println("\tPlease enter a valid choice!");
         choice = getNonEmptyResponse(esql, "\tEnter selection: ");
      }

      // let's just not allow empty update
      String newstuff = getNonEmptyResponse(esql, "\tUpdate entry to: ");

      int choicenum = Integer.parseInt(choice);
      query = "";
      String columntoupdate = "";
      // CSgraduate lol
      // use case next time
      // type issue with string vs float/int
      // we don't include the following
      // storeID: makes it too complicated
      // productName: issues with foreign keys
      // if(choicenum == 0){
      //    columntoupdate = "storeID";
      //    query = String.format("UPDATE Product SET %s = '%s' WHERE storeID = '%s' AND productName = '%s'", columntoupdate, newstuff, storeID, productName);
      // }
      // if(choicenum == 1){
      //       columntoupdate = "productName";
      //       query = String.format("UPDATE Product SET %s = '%s' WHERE storeID = '%s' AND productName = '%s'", columntoupdate, newstuff, storeID, productName);
      //    }
      //    else 
      if(storeID.isEmpty()){
         System.out.println("ufjewofhjweopignweroajgweoij");
      }
      else{
         if(choicenum == 0){ // formerly 2
            columntoupdate = "numberOfUnits";
            query = String.format("UPDATE Product SET %s = '%s' WHERE storeID = '%s' AND productName = '%s'", columntoupdate, Integer.parseInt(newstuff), storeID, productName);
         }
         else if(choicenum == 1){ // formerly 3
            columntoupdate = "pricePerUnit";
            query = String.format("UPDATE Product SET %s = '%s' WHERE storeID = '%s' AND productName = '%s'", columntoupdate, Float.parseFloat(newstuff), storeID, productName);
         }
         try{
            System.out.println(query);
            esql.executeUpdate(query);
            System.out.println("\tProduct updated!");
            
            try{
               SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               Date date = new Date(System.currentTimeMillis());

               String update = String.format("INSERT INTO ProductUpdates (managerID, storeID, productName, updatedOn) VALUES (%s, %s, '%s', '%s')", esql.userID, storeID, productName, formatter.format(date));

               esql.executeUpdate(update);
               System.out.println("\tUpdate recorded!");

            }
            catch(Exception e){
               System.err.println(e.getMessage());
            }
         }
         catch(Exception e){
            System.err.println(e.getMessage());
         }
      }
   }

   public static void viewRecentUpdates(Retail esql){
      int userstatus = esql.isAuthorized(esql);
      if(userstatus > 0){
         // show 5 most recent product update actions
         // products are unique per store
         // given a store, show the 5 most recent updates
         // we distinguish by store because each store has 'unique' products
         // even if they have same product, can be different price
         try{
            // refactor to show updates of managed stores
            System.out.println("\tShowing the 5 most recent product updates at a store...");

            if(userstatus == 2){
               // godmode
               System.out.println("\tADMIN: Leave blank to search across all stores.");
               System.out.print("\tEnter StoreID: ");
               String storeID = in.readLine();

               // copied from viewRecentOrders, with respective variables changed
               // any need for nested call? seems redundant but w/e...
               String query = "";

               if(storeID.isEmpty()){
                  // TODO: refactor queries (probably could be more efficient)
                  // query = String.format("SELECT * FROM (SELECT * FROM ProductUpdates ORDER BY updateNumber DESC) SQ ORDER BY updateNumber DESC FETCH FIRST 5 ROWS ONLY");
                  // query = String.format("SELECT * FROM ProductUpdates ORDER BY updateNumber DESC");
                  query = String.format("SELECT storeID FROM Store", esql.userID);
                  List<List<String>> allstores = esql.executeQueryAndReturnResult(query);
                  for(int i = 0; i < allstores.size(); i++){
                     query = String.format("SELECT * FROM (SELECT * FROM ProductUpdates WHERE storeID = '%s' ORDER BY updateNumber DESC FETCH FIRST 5 ROWS ONLY) SQ ORDER BY updateNumber ASC", storeID);
                     String msg = String.format("\tStore %s: ", allstores.get(i).get(0));
                     System.out.println(msg);
                     esql.executeQueryAndPrintResult(query);
                  }
               }
               else{
                  query = String.format("SELECT * FROM (SELECT * FROM ProductUpdates WHERE storeID = '%s' ORDER BY updateNumber DESC FETCH FIRST 5 ROWS ONLY) SQ ORDER BY updateNumber ASC", storeID);
                  int rowCount = esql.executeQueryAndPrintResult(query);
               }
            }
            else{
               // just a normal manager
               // show managed stores
               // get stores which user manages
               String query = String.format("SELECT storeID FROM Store WHERE managerID = '%s'", esql.userID);
               List<List<String>> managerstores = esql.executeQueryAndReturnResult(query);
               // System.out.println(managerstores);

               String storeID = "";

               boolean greenflag = false;
               // I would replace this with managerSelectStore but I pulled some shit with greenflag
               // effectively the same thing as managerSelectStore()
               // written 'cleaner' in viewPopularProducts
               while(!greenflag){
                  System.out.print("\tManaged stores: ");
                  for(int i = 0; i < managerstores.size(); i++){
                     System.out.print(String.format("%s, ", managerstores.get(i).get(0)));
                  }
                  System.out.println();
                  System.out.println("\tLeave blank to search across all managed stores.");
                  System.out.print("\tEnter StoreID: ");
                  storeID = in.readLine();

                  // check if store is really managed
                  // find id in our list
                  // boolean storemanaged = false;
                  // empty line is valid input
                  if(storeID.isEmpty()){
                     break;
                  }
                  for(int i = 0; i < managerstores.size(); i++){
                     if(storeID.equals(managerstores.get(i).get(0))){
                        greenflag = true;
                        break;
                     }
                  }
                  if(!greenflag){
                     System.out.println("\tYou don't manage this store! Pick a different store...");
                  }
               }
               // if greenflag is false
               // either something has gone catastrophically wrong or
               //    user empty carriage return
               if(!greenflag){
                  // print updates to all stores managed
                  System.out.println("\tShowing product updates for all managed stores...");
                  for(int i = 0; i < managerstores.size(); i++){
                     String curthing = managerstores.get(i).get(0);
                     System.out.println(String.format("\tStore %s's last 5 product updates: ", curthing));
                     query = String.format("SELECT * FROM (SELECT * FROM ProductUpdates WHERE storeID = '%s' ORDER BY updateNumber DESC FETCH FIRST 5 ROWS ONLY) SQ ORDER BY updateNumber ASC", curthing);
                     esql.executeQueryAndPrintResult(query);
                  }
               }
               else{
                  // print selected store's updates
                  query = String.format("SELECT * FROM (SELECT * FROM ProductUpdates WHERE storeID = '%s' ORDER BY updateNumber DESC FETCH FIRST 5 ROWS ONLY) SQ ORDER BY updateNumber ASC", storeID);
                  esql.executeQueryAndPrintResult(query);
               }
            }
         }
         catch(Exception e){
            System.err.println(e.getMessage());
         }
      }
      else{
         System.out.println("\tUser is not authorized!!!");
      }
   }

   public static void viewPopularProducts(Retail esql){
      int userstatus = esql.isAuthorized(esql);
      if(userstatus > 0){
         // show 5 most popular items at a store?
         //    orders across all stores?
         // how do we define popular? 
         //    highest num of unique individuals who purchased product
         // each order only has single product
         // strategy: count num of distinct people who ordered product
         //    return top 5 most ordered products
         
         System.out.println("\tShowing the 5 most frequently purchased products at a store...");
         String storeID = "";
         String query = "";
         List<List<String>> managerstores = null;
         try{
            if(userstatus == 2){
               // godmode
               System.out.println("\tADMIN: Leave blank to search across all stores.");
               storeID = getResponse(esql, "\tEnter StoreID: ");
            }
            else{
               boolean validinput = false;
               String thing = String.format("SELECT storeID FROM Store WHERE managerID = '%s'", esql.userID);
               managerstores = esql.executeQueryAndReturnResult(thing);

               storeID = managerSelectStore(esql, managerstores);
            }
            // so many goddamn if statements
            if(storeID.isEmpty()){
               // do something
               if(userstatus == 2){
                  query = String.format("SELECT productName, COUNT (productName) as productFreq FROM (SELECT * FROM Orders) SQ GROUP BY productName ORDER BY productFreq DESC LIMIT 5");         
                  esql.executeQueryAndPrintResult(query);
               }
               else{
                  for(int i = 0; i < managerstores.size(); i++){
                     System.out.println(String.format("\tStore %s's 5 most popular products: ", managerstores.get(i).get(0)));
                     query = String.format("SELECT productName, COUNT (productName) as productFreq FROM (SELECT * FROM Orders WHERE storeID = '%s') SQ GROUP BY productName ORDER BY productFreq DESC LIMIT 5", managerstores.get(i).get(0));
                     esql.executeQueryAndPrintResult(query);
                  }
               }
            }
            else{
               // other thing
               query = String.format("SELECT productName, COUNT (productName) as productFreq FROM (SELECT * FROM Orders WHERE storeID = '%s') SQ GROUP BY productName ORDER BY productFreq DESC LIMIT 5", storeID);
               esql.executeQueryAndPrintResult(query);
            }
         }
         catch(Exception e){
            System.err.println(e.getMessage());
         }
      }
      else{
         System.out.println("\tUser is not authorized!!!");
      }
   }

   public static void viewPopularCustomers(Retail esql){
      // popular customers? 
      // show 5 most regular customers
      // given a storeID, return the 5 most frequent customers
      int userstatus = esql.isAuthorized(esql);
      if(userstatus > 0){
         try{
            System.out.println("\tShowing the 5 most popular customers at a store...");
            String pp = "";
            // String msg = "";
            if(userstatus == 2){
               // godmode
               pp = "SELECT storeID FROM Store";
               // msg = "\tADMIN: Select StoreID: ";
            }
            else{
               pp = String.format("SELECT storeID FROM Store WHERE managerID = '%s'", esql.userID);
               // msg = "Select StoreID: "
            }
            List<List<String>> managedstores = esql.executeQueryAndReturnResult(pp);
            String storeID = managerSelectStore(esql, managedstores);

            String query = "";

            if(storeID.isEmpty()){
               for(int i = 0; i < managedstores.size(); i++){
                  System.out.println(String.format("\tStore %s's most popular customers: ", managedstores.get(i).get(0)));
                  query = String.format("SELECT customerID, COUNT (customerID) as custFreq FROM (SELECT * FROM Orders WHERE storeID = '%s') SQ GROUP BY customerID ORDER BY custFreq DESC LIMIT 5", managedstores.get(i).get(0));
                  int rowCount = esql.executeQueryAndPrintResult(query);
               }
            }
            else{
               query = String.format("SELECT customerID, COUNT (customerID) as custFreq FROM (SELECT * FROM Orders WHERE storeID = '%s') SQ GROUP BY customerID ORDER BY custFreq DESC LIMIT 5", storeID);
               int rowCount = esql.executeQueryAndPrintResult(query);
            }

            // all orders for a store
            // String query = String.format("SELECT * FROM Orders WHERE storeID = '%s'", storeID);
            
            // find most frequent values
            // https://stackoverflow.com/questions/12235595/find-most-frequent-value-in-sql-column
         }
         catch(Exception e){
            System.err.println(e.getMessage());
         }
      }
      else{
         System.out.println("\tUser is not authorized!!!");
      }
   }
   public static void placeProductSupplyRequests(Retail esql){
      int userstatus = esql.isAuthorized(esql);
      if(userstatus > 0){
         try{
            System.out.println("\tCreating product supply request...");

            String howlongcanavariablenamegetbeforejavacomplains = "";
            if(userstatus == 0){
               howlongcanavariablenamegetbeforejavacomplains = String.format("SELECT storeID FROM Store");
            }
            else{
               howlongcanavariablenamegetbeforejavacomplains = String.format("SELECT storeID FROM Store WHERE managerID = '%s'", esql.userID);
            }
            List<List<String>> managedstores = esql.executeQueryAndReturnResult(howlongcanavariablenamegetbeforejavacomplains);
            
            // System.out.print("\tEnter StoreID: ");
            // String storeID = in.readLine();
            String storeID = userSelectStore(esql, managedstores);

            // System.out.print("\tEnter Product Name: ");
            // String productName = in.readLine();
            String productToBuy = "";
            while(true){
               System.out.print("\tEnter Product Name: ");
               productToBuy = in.readLine();
               if(validProductSelection(esql, productToBuy)){
                  break;
               }
               System.out.println("\tPlease enter a valid product name!");
            }
            // so lazy
            String productName = productToBuy;

            // System.out.print("\tUnits Requested: ");
            // String unitsRequested = in.readLine();
            String qtyToBuya = "";
            int qtyToBuy = -1;
            while(true){
               System.out.print("\tEnter QTY to order: ");
               qtyToBuya = in.readLine();
               qtyToBuy = Integer.parseInt(qtyToBuya);
               if(qtyToBuy < 1){
                  System.out.println("\tPlease enter a value larger than 0!");
               }
               else{
                  break;
               }
            }
            // lazy lazy lazy
            String unitsRequested = qtyToBuya;

            String warehouseID = pickWarehouse(esql);

            // assign the next free request number
            // use trigger now
            //       String query = String.format("SELECT * FROM (SELECT * FROM Orders WHERE customerID = '%s' ORDER BY orderNumber DESC) SQ ORDER BY orderNumber DESC FETCH FIRST 5 ROWS ONLY", esql.userID);
            // String getreqnum = "SELECT requestNumber FROM ProductSupplyRequests ORDER BY requestNumber DESC FETCH FIRST 1 ROWS ONLY";
            // List<List<String>> lastusedreqnum = esql.executeQueryAndReturnResult(getreqnum);

            // int reqnum = Integer.parseInt(lastusedreqnum.get(0).get(0));
            // reqnum = reqnum + 1;
            // (requestNumber, managerID, warehouseID, storeID, productName, unitsRequested)
            // String query = String.format("INSERT INTO ProductSupplyRequests VALUES(%s, %s, %s, %s, '%s', %s)", reqnum, esql.userID, warehouseID, storeID, productName, unitsRequested);
            String query = String.format("INSERT INTO ProductSupplyRequests (managerID, warehouseID, storeID, productName, unitsRequested) VALUES(%s, %s, %s, '%s', %s)", esql.userID, warehouseID, storeID, productName, unitsRequested);

            esql.executeUpdate(query);
            // esql.executeQueryAndPrintResult("SELECT * FROM ProductSupplyRequests");
            System.out.println("\tSupply order placed!");
         }
         catch(Exception e){
            System.err.println(e.getMessage());
         }
      }
      else{
         System.out.println("\tUser is not authorized!!!");
      }
   }



   public static void viewOrders(Retail esql){
      
      int userstatus = esql.isAuthorized(esql);
         if(userstatus > 0){
            // show 5 most popular items at a store?
            //    orders across all stores?
            // how do we define popular? 
            //    highest num of unique individuals who purchased product
            // each order only has single product
            // strategy: count num of distinct people who ordered product
            //    return top 5 most ordered products
            System.out.println("\tManager can see all the orders information of the store(s) he/she manages.");
      
            String storeID = "";
            String query = "";
            List<List<String>> managerstores = null;
            try{
               if(userstatus == 2){
                  // godmode
                  System.out.println("\tADMIN: Leave blank to search across all stores.");
                  storeID = getResponse(esql, "\tEnter StoreID: ");
               }
               else{
                  boolean validinput = false;
                  String thing = String.format("SELECT storeID FROM Store WHERE managerID = '%s'", esql.userID);
                  managerstores = esql.executeQueryAndReturnResult(thing);

      
                  storeID = managerSelectStore(esql, managerstores);
               }
            
               if(storeID.isEmpty()){
                  // do something
                  if(userstatus == 2){
                     query = String.format("SELECT * FROM Orders ");         
                     esql.executeQueryAndPrintResult(query);
                  }
                  else{
                     for(int i = 0; i < managerstores.size(); i++){
                        System.out.println(String.format("\tView all orders", managerstores.get(i).get(0)));
                        query = String.format("SELECT * FROM Orders WHERE storeID = '%s' ", managerstores.get(i).get(0));
                        esql.executeQueryAndPrintResult(query);
                     }
                  }
               }
               else{
                  // other thing
                  query = String.format("SELECT * FROM Orders WHERE storeID = '%s' ", storeID);
                  esql.executeQueryAndPrintResult(query);
               }
            }
            catch(Exception e){
               System.err.println(e.getMessage());
            }
         }
         else{
            System.out.println("\tUser is not authorized!!!");
         }
      }





   public static void viewUsers(Retail esql){
      int userstatus = esql.isAuthorized(esql);
      if(userstatus == 2){
         try{
            System.out.println("\tAdmin can see all users...");
            String query  = String.format("SELECT * FROM Users");
            esql.executeQueryAndPrintResult(query);
         }
         catch(Exception e){
            System.err.println(e.getMessage());
         }  
      }
      else{
         System.out.println("\tUser is not authorized!!!");
      }
   }

   public static void updateUsers(Retail esql){
      int userstatus = esql.isAuthorized(esql);
      if(userstatus < 2){
         System.out.println("\tUser is not authorized!!!");
         return;
      }
      
      System.out.println("\tUpdating a user...");
      String userID = getResponse(esql, "\tADMIN: Enter userID: ");
      System.out.println("\tPlease select which column to update: ");
      System.out.println("\t\t0: userID ");
      System.out.println("\t\t1: name");
      System.out.println("\t\t2: password");
      System.out.println("\t\t3: latitude");
      System.out.println("\t\t4: longitude");
      System.out.println("\t\t5: type");

      String choice = getNonEmptyResponse(esql, "\tEnter selection: ");
      while(!(choice.equals("0") || choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4") || choice.equals("5"))){
         System.out.println("\tPlease enter a valid choice!");
         choice = getNonEmptyResponse(esql, "\tEnter selection: ");
      }

      // let's just not allow empty update
      String newstuff = getNonEmptyResponse(esql, "\tUpdate entry to: ");
      List<List<String>> listofusers;
      String userentry = "";
      int choicenum = Integer.parseInt(choice);
      String query = "";
      String columntoupdate = "";
      if(choicenum == 0){
         columntoupdate = "userID";
         query = String.format("UPDATE Users SET %s = '%s' WHERE userID = '%s' " , columntoupdate, newstuff, userID);
      }
      else if(choicenum == 1){
         columntoupdate = "name";
         query = String.format("UPDATE Users SET %s = '%s' WHERE userID = '%s'",columntoupdate, newstuff, userID);
      }
      else if(choicenum == 2){
         columntoupdate = "password";
         query = String.format("UPDATE Users SET %s = '%s' WHERE userID = '%s'", columntoupdate, newstuff, userID);
      }
      else if(choicenum == 3){
         columntoupdate = "latitude";
         query = String.format("UPDATE Users SET %s = '%s' WHERE userID = '%s'", columntoupdate, Float.parseFloat(newstuff), userID);
      }
      else if(choicenum == 4){
         columntoupdate = "longitude";
         query = String.format("UPDATE Users SET %s = '%s' WHERE userID = '%s'" , columntoupdate, Float.parseFloat(newstuff), userID);
      }
      else if(choicenum == 5){
         columntoupdate = "type";
         query = String.format("UPDATE Users SET %s = '%s' WHERE userID = '%s' " , columntoupdate, newstuff, userID);
      }

      try{
         esql.executeUpdate(query);
      }
      catch(Exception e){
         System.err.println(e.getMessage());
      }
      //
      // was going to use trigger to update order's customer ids when we change userID
      // suffices to include `ON DELETE CASCADE ON UPDATE CASCADE` to foreign keys
   }

}//end Retail










