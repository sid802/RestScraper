import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.impl.dv.xs.DoubleDV;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.jetbrains.annotations.Contract;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by Sid on 5/17/2016.
 */
public class RestParser {

    public String[] cities;

    public RestParser(String[] cities) {
        // Constructor
        this.cities = cities;
    }

    /**
     * @return List of Restaurant instances from the pages
     */
    public HashMap<String, ArrayList<Restaurant>> getRestaurants() {
        HashMap<String, ArrayList<Restaurant>> restaurantLst = new HashMap<String, ArrayList<Restaurant>>();
        ArrayList<Restaurant> cityRestaurants;
        try {
            Document doc = Jsoup.connect(Const.restUrl).get();
            HashMap<String, String> cityUrls;
            if (this.cities.length > 0)
                cityUrls = getCitiesUrls(doc, this.cities);
            else
                cityUrls = getCitiesUrls(doc);

            String cityUrlString;
            for (String cityName: cityUrls.keySet()) {
                cityUrlString = cityUrls.get(cityName);
                cityRestaurants = getCityRestaurants(cityUrlString, cityName);
                restaurantLst.put(cityName, cityRestaurants);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return restaurantLst;
    }

    /**
     *
     * @param fullText Full text like "misadot be tel aviv"
     * @return only relevant part ("tel aviv")
     */
    private String getCityFromText(String fullText) {
        int startIndex = fullText.indexOf("מסעדות ב");
        return fullText.substring(startIndex + "מבסעדות ב".length() - 1);
    }

    /**
     *
     * @param html_doc Document of html source
     * @return HashMap of all cities and their urls
     */
    private HashMap<String, String> getCitiesUrls(Document html_doc) {
        HashMap<String, String> cityUrls = new HashMap<String, String>();
        Elements cityUrlElements;

        String cityUrl, cityName;
        cityUrlElements = html_doc.select(Selectors.allCityUrls);

        for (Element cityUrlElement: cityUrlElements) {
            cityUrl = cityUrlElement.attr("abs:href");
            cityName = getCityFromText(cityUrlElement.attr("title"));
            cityUrls.put(cityName, cityUrl);
        }
        return cityUrls;
    }

    /**
     *
     * @param html_doc Document of html_soruce
     * @param cities Cities we want to extract
     * @return HashMap of city name and its URL
     */
    private HashMap<String, String> getCitiesUrls(Document html_doc, String[] cities) {
        HashMap<String, String> cityUrls = new HashMap<String, String>();
        String cityUrl, cityName;
        Element cityUrlElement;

        for(String city: cities) {
            cityUrlElement = html_doc.select(String.format(Selectors.cityUrlTemplate, city)).first();
            if (cityUrlElement != null) {
                cityUrl = cityUrlElement.attr("abs:href");
                cityName = getCityFromText(cityUrlElement.attr("title"));
                cityUrls.put(cityName, cityUrl);
            }
        }
        return cityUrls;
    }

    /**
     *
     * @param cityUrl: city's url
     * @return ArrayList of all the city's restaurants
     */
    private ArrayList<Restaurant> getCityRestaurants(String cityUrl, String cityName) {
        ArrayList<Restaurant> cityRestaurants = new ArrayList<>();
        String cityPageUrlTemplate = "%s&page=%d"; // cityUrl example http://www.rest.co.il/rests?tags=e70041139
        String pageAmountSelector = Selectors.pageAmount;
        int lastPage;
        try {
            Document page_source = Jsoup.connect(cityUrl).get();
            lastPage = Integer.parseInt(page_source.select(pageAmountSelector).last().text());
            String cityPageUrl;
            ArrayList<Restaurant> pageRestaurants;
            System.out.println(String.format("Last page of country %s is: %d", cityName, lastPage));

            for (int pageIndex=1; pageIndex  <=lastPage; pageIndex++) {
                cityPageUrl = String.format(cityPageUrlTemplate, cityUrl, pageIndex);
                pageRestaurants = getPageRestaurants(cityPageUrl, cityName);
                System.out.println(String.format("Parsed %d pages from page %d in country %s",
                        pageRestaurants.size(),
                        pageIndex,
                        cityName
                ));
                cityRestaurants.addAll(pageRestaurants);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        return cityRestaurants;
    }

    /**
     *
     * @param pageUrl The Url's page we want to parse the restaurants from
     * @return List of parsed restaurants
     */
    private ArrayList<Restaurant> getPageRestaurants(String pageUrl, String cityName) {
        ArrayList<Restaurant> pageRestaurants = new ArrayList<>();

        try {
            Document page_html = Jsoup.connect(pageUrl).get();
            Elements restElements = page_html.select(Selectors.restaurantBlock);
            Restaurant currentRest;
            for (Element restElement: restElements) {
                currentRest = parseRestaurantFromElement(restElement, cityName);
                pageRestaurants.add(currentRest);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return pageRestaurants;
    }

    /**
     *
     * @param restElement Element used to extract all the restaurant's info
     * @return Restaurant instance
     * The CSS selectors are saved in the Constants class
     *
     */
    private Restaurant parseRestaurantFromElement(Element restElement, String cityName) {

        String id = restElement.select(Selectors.restRelUrl).attr("href").split("/")[2]; // href = "/rest/80215919
        String name = restElement.select(Selectors.restName).first().text();

        String rankingString = restElement.select(Selectors.restRanking).attr("style"); // Ranking is by width of red color

        double rankingDouble = 0;
        try {

            if (!rankingString.isEmpty()) {
                int start, end;
                start = rankingString.indexOf(':');
                end = rankingString.indexOf('%');
                if (end == -1)
                    rankingDouble = Double.parseDouble(rankingString.substring(start + 1));
                else
                    rankingDouble = Double.parseDouble(rankingString.substring(start + 1, end));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        String category = restElement.select(Selectors.restCategory).attr("title");
        String foodKinds = getTextFromElement(restElement.select(Selectors.restFoodKinds).first());
        String address = getTextFromElement(restElement.select(Selectors.restAddress).first());
        String price = getTextFromElement(restElement.select(Selectors.restPrice).first());
        String kosher = getTextFromElement(restElement.select(Selectors.restKosher).first());
        String picUrl = restElement.select(Selectors.restPicUrl).attr("src");

        Element descrElement = restElement.select(Selectors.restDescr).first();
        String description = null;
        if (descrElement != null)
            description = descrElement.text();

        Element phoneElement = restElement.select(Selectors.restPhones).first();
        String phone = null;
        if (phoneElement != null)
            phone = phoneElement.ownText();

        Element deliveryPhoneElement = restElement.select(Selectors.deliverPhones).first();
        String deliveryPhone = null;
        if (deliveryPhoneElement != null)
            deliveryPhone = deliveryPhoneElement.ownText();

        String origCoordsUrl = restElement.select(Selectors.restCoordsUrl).attr("abs:href").trim();
        double longitude = 0, latitude = 0;
        if (!origCoordsUrl.isEmpty()) {
            String coordUrl = regenerateMapUrl(origCoordsUrl);
            double[] coords = getCoordsFromUrl(coordUrl);
            latitude = coords[0];
            longitude = coords[1];
        }

        return new Restaurant(name, id, rankingDouble, description, longitude, latitude,
                category, foodKinds, phone, deliveryPhone, cityName, address, price, kosher, picUrl);
    }

    /**
     *
     * @param originalUrl Original Map Url
     * @return Actual Url needed to get the restaurant's coordinates
     */
    private String regenerateMapUrl(String originalUrl) {
        Matcher urlMatcher = Const.srcMapUrlRegex.matcher(originalUrl);
        try {
            urlMatcher.find();
            String restId, secondId;
            restId = urlMatcher.group(1);
            secondId = urlMatcher.group(2);
            return String.format(Const.dstMapUrlTemplate, restId, secondId);
        } catch (IllegalStateException e) {
            System.out.println("Error regenerating Url: " + originalUrl);
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param mapUrl the url to the map
     * @return Array of where 1st item is the latitude and the second is the longitude
     */
    private double[] getCoordsFromUrl(String mapUrl) {
        String[] coordsString = null;
        double[] coords = new double[2];
        try {
            Document html_doc = Jsoup.connect(mapUrl).get();
            Element fullCoordsElement = html_doc.select(Selectors.restCoords).first();
            String fullCoords = fullCoordsElement.attr("value");
            coordsString = fullCoords.split(",");
            coords[0] = Double.parseDouble(coordsString[0]);
            coords[1] = Double.parseDouble(coordsString[1]);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error parsing coords from url: " + mapUrl + "\n");
            e.printStackTrace();
        }
        return coords;
    }

    /**
     *
     * @param el element
     * @param attr Attribute
     * @return null if Element is null or it doesn't have the attribute. the attribute's value otherwise
     */
    private String getAttrFromElement(Element el, String attr) {
        if (el == null)
            return null;
        else if (el.hasAttr(attr))
            return el.attr(attr);
        return null;
    }

    /**
     *
     * @param el Element
     * @return Element's text, null if it doesn't have any
     */
    private String getTextFromElement(@Nullable Element el) {

        if (el == null || el.text().isEmpty())
            return null;
        return el.text();
    }

}
