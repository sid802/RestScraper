import java.util.*;

public class Main {

    public static void main(String[] args) {
        RestParser rParser = new RestParser(new String[]{"תל אביב", "דרום", "סידני"});
        HashMap<String, ArrayList<Restaurant>> restaurants = rParser.getRestaurants();
        Iterator<ArrayList<Restaurant>> rest = restaurants.values().iterator();
        ArrayList<Restaurant> allRests = new ArrayList<>();
        while (rest.hasNext()) {
            allRests.addAll(rest.next());
        }

        SerializeObjects.saveToFile("C:\\Users\\Sid\\Desktop\\objects.csv",allRests.iterator());
    }
}
