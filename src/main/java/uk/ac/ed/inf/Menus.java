package uk.ac.ed.inf;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Menus {

    private static final HttpClient client = HttpClient.newHttpClient();
    public static ArrayList<Shop> shopList;
    public static final int DELIVERY_CHARGE = 50;
    public static HttpResponse<String> response;

    public static class Shop{

        public String name;
        public String location;
        public ArrayList<Menu> menu;

        public static class Menu{
            public String item;
            public int pence;
            public int getPence()
            {
                return pence;
            }
            public String item()
            {
                return item;
            }
        }
    }

    /**
     * Menus accepts two strings which specify first the name of the machine and second the
     * port where the web server is running. It is then allowing us to get our http web server
     * running. This web server contains the json files needed to get the delivery cost in
     * getDeliveryCost.
     * @param machine_name name of the machine
     * @param web_server_port where the web server is running
     */

    public Menus(String machine_name, String web_server_port){

        //Make a request so that we can get a response
        //HttpRequest assumes that it is a GET request by default
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://" + machine_name + ":" + web_server_port+"/menus/menus.json")).build();
        //Call the send method on the client created

        try {

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        }
        catch(IOException | InterruptedException ex)
        {
            System.out.println("Cannot retrieve data from server: check server name and query");
        }

        if (response.statusCode() != 200){
            System.exit(1);
        }
        else{

            Type listType = new TypeToken<ArrayList<Shop>>() {}.getType();
            // Use the ”fromJson(String, Type)” method
            shopList = new Gson().fromJson(response.body(), listType);
        }
    }

    /**
     * getDeliveryCost accepts a variable number of strings of type String... and returns the int cost
     * in pence of having all of these items delivered by drone, including the standard delivery charge
     * of 50p per delivery.
     * @param order list of strings with items from the menus file that somebody has ordered.
     * @return price of the order
     */
    public int getDeliveryCost(String ... order)
    {
        int deliveryCost = 0;

        /*
        for each order, check each shop's menu to see if any of the menu items match with
        the items on the order. If it does, add the price of the item to the deliveryCost
        initialised above.
         */
        for(String item: order)
        {
            for(Shop shop: shopList)
            {
                for(int i = 0; i < shop.menu.size(); i++)
                {
                    if(item.equals(shop.menu.get(i).item))
                    {
                        deliveryCost += shop.menu.get(i).pence;
                    }
                }
            }
        }
        /*
        Check if a delivery was made. If one was, add the delivery charge to the price of
        the order.
         */
        if (deliveryCost > 0)
        {
            deliveryCost += Menus.DELIVERY_CHARGE;
        }
        return deliveryCost;
    }
}
