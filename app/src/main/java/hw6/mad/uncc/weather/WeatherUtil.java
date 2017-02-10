package hw6.mad.uncc.weather;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
/**
 * Created by sures on 10/18/2016.
 */
public class WeatherUtil {
    static public class WeatherJSONParser {
        static ArrayList<ArrayList<Weather>> parseWeather(String ln) throws JSONException, ParseException {
            ArrayList<ArrayList<Weather>> dailyweatherList = new ArrayList<>();
            JSONObject root = null;
            int index = 0;
            String checkdate = "";


            try {
                root = new JSONObject(ln);
                JSONArray JSONWeatherArray = root.getJSONArray("list");
                dailyweatherList.add(new ArrayList<Weather>());
                for(int i=0; i<JSONWeatherArray.length();i++){
                    JSONObject jsonObject = JSONWeatherArray.getJSONObject(i);
                    Weather weather = new Weather();

                    String dateTime = jsonObject.getString("dt_txt");
                    String[] parts = dateTime.split(" ");
                    weather.setDate(LayoutChange_Date(parts[0]));
                    weather.setTime(LayoutChange_Time(parts[1]));

                    if(checkdate.equals("")){
                        checkdate = weather.getDate();
                    }

                    JSONObject main=jsonObject.getJSONObject("main");
                    weather.setTemp(main.getString("temp"));
                    weather.setPressure(main.getString("pressure"));
                    weather.setHumidity(main.getString("humidity"));

                    JSONObject wind=jsonObject.getJSONObject("wind");
                    weather.setWs(wind.getString("speed"));
                    weather.setWd(wind.getString("deg"));

                    JSONArray cond = jsonObject.getJSONArray("weather");
                    JSONObject condition = cond.getJSONObject(0);
                    weather.setClimatetype(condition.getString("description"));
                    String icon = condition.getString("icon");

                    weather.setIconurl("http://openweathermap.org/img/w/"+icon+".png");

                    if(checkdate.equals(weather.getDate()))
                        dailyweatherList.get(index).add(weather);
                    else if(checkdate.compareTo(weather.getDate()) < 0){
                        dailyweatherList.add(new ArrayList<Weather>());
                        index++;
                        checkdate = weather.getDate();
                        dailyweatherList.get(index).add(weather);
                    }
                }
                return dailyweatherList;
            } catch (JSONException e) {
                return dailyweatherList;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String LayoutChange_Time(String source) throws ParseException {
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
            Date destination = sdf1.parse(source);
            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm aaa");
            return sdf2.format(destination);
        }

        private static String LayoutChange_Date(String source) throws ParseException {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date destination = sdf1.parse(source);
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy");
            return sdf2.format(destination);
        }
    }
    }

