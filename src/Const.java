import java.util.regex.Pattern;

/**
 * Created by Sid on 5/28/2016.
 */
public final class Const {
    public static final String restUrl = "http://rest.co.il";
    public static final Pattern srcMapUrlRegex = Pattern.compile("Gallery/(\\d+)/(\\d+)");
    public static final String dstMapUrlTemplate = "http://www.rest.co.il/Map/RestPageIframe?customerId=%s&tagId=%s&height=10&width=10";

}
