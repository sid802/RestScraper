/**
 * Created by Sid on 5/17/2016.
 */
public class Restaurant implements SerializableInterface {

    private static final String[] fieldOrder = new String[] {
            "id", "name", "category", "rating", "description",
            "foodKinds", "phoneRest", "phoneDelivery", "city", "address", "price", "kosherLevel", "picUrl",
            "longitude", "latitude"
    };

    private String name;
    private String id;
    private double rating;
    private String description;
    private double longitude;
    private double latitude;
    private String category;
    private String foodKinds;
    private String phoneRest;
    private String phoneDelivery;
    private String address;
    private String city;
    private String price;
    private String kosherLevel;
    private String picUrl;

    public Restaurant(String name, String id, double rating, String description, double longitude, double latitude, String category,
                      String foodKinds, String phoneRest, String phoneDelivery, String city, String address, String price,
                      String kosherLevel, String picUrl) {

        this.name = name;
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.foodKinds = foodKinds;
        this.phoneRest = phoneRest;
        this.phoneDelivery = phoneDelivery;
        this.city = city;
        this.address = address;
        this.price = price;
        this.kosherLevel = kosherLevel;
        this.picUrl = picUrl;

    }

    public Restaurant(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Restaurant() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFoodKinds() {
        return foodKinds;
    }

    public void setFoodKinds(String foodKinds) {
        this.foodKinds = foodKinds;
    }

    public String getPhoneRest() {
        return phoneRest;
    }

    public void setPhoneRest(String phoneRest) {
        this.phoneRest = phoneRest;
    }

    public String getPhoneDelivery() {
        return phoneDelivery;
    }

    public void setPhoneDelivery(String phoneDelivery) {
        this.phoneDelivery = phoneDelivery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKosherLevel() {
        return kosherLevel;
    }

    public void setKosherLevel(String kosherLevel) {
        this.kosherLevel = kosherLevel;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String[] getFieldOrder() {
        return fieldOrder;
    }

}
