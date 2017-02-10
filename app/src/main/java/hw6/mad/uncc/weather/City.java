package hw6.mad.uncc.weather;
import java.io.Serializable;
/**
 * Created by sures on 10/18/2016.
 */
public class City implements Serializable{
    private long _id;
    String cityname;
    String country;
    String temperature;
    int favourite;
    private String date;
    public City() {
    }

    public City(String cityName, String country, String temperature, int favorite) {
        this.cityname = cityName;
        this.country = country;
        this.temperature = temperature;
        this.favourite = favorite;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "City{" +
                "_id=" + _id +
                ", cityname='" + cityname + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", favourite=" + favourite +
                ", date='" + date + '\'' +
                '}';
    }
}
