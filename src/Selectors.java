/**
 * Created by Sid on 5/27/2016.
 */
public final class Selectors {
    public static final String cityUrlTemplate = "ul[class=\"RestaurantsList\"] a[title*=\"מסעדות ב%s\"]";
    public static final String allCityUrls = "div.Lists div.RestList:nth-of-type(3) a[title*=\"מסעדות ב\"]";
    public static final String restaurantBlock = "div.ResultBlock";
    public static final String pageAmount = "a.NumBtn";
    public static final String restName = "div.RestName";
    public static final String restRanking = "div.Stars>span";
    public static final String restDescr = "div.AboutText";
    public static final String restCategory = "div.RestIcon + div.InfoTxt";
    public static final String restFoodKinds = "div.TypesTooltip  div.Text";
    public static final String restPhones = "div.PhoneIcon + div.InfoTxt";
    public static final String deliverPhones = "div.DeliveryIcon + div.InfoTxt";
    public static final String restAddress = "div.MapIcon + div.InfoTxt";
    public static final String restPrice = "div.PriceTooltip div.Text";
    public static final String restKosher = "div.KosherTooltip div.Text";
    public static final String restPicUrl = "a[data-name=\"aRestName\"] > img";
    public static final String restCoordsUrl = "a#aMapTab";
    public static final String restCoords = "input#pointList";

}
