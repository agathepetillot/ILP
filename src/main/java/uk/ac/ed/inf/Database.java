package uk.ac.ed.inf;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    public Database(String machine_name, String web_server_port) throws SQLException {

        try (Connection conn = DriverManager.getConnection("jdbc:derby://" + machine_name + ":" + web_server_port + "/derbyDB")) {
            // Create a statement object that we can use for running various
            // SQL statement commands against the database.
            Statement statement = conn.createStatement();

            DatabaseMetaData databaseMetadata = conn.getMetaData();

            // Note: must capitalise STUDENTS in the call to getTables
            ResultSet resultSet = databaseMetadata.getTables(null, null, "ORDERS", null);

            // If the resultSet is not empty then the table exists, so we can drop it
            if (resultSet.next()) {
//                statement.execute("drop table orders");
            }

//            final String ordersQuery =  "select * from orders where customer=(?)";
//            PreparedStatement psOrderQuery = conn.prepareStatement(ordersQuery);
//            psOrderQuery.setString(1, "s2343597");
//            // Search for the student’s courses and add them to a list
//            ArrayList<String> customerList = new ArrayList<>();
//            ResultSet rs = psOrderQuery.executeQuery();
//            while (rs.next()) {
//                String customer = rs.getString("customer");
//                customerList.add(customer);
//                System.out.println(customer);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws SQLException {

        Database orders = new Database("localhost","9876");

    }


}
