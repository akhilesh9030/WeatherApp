package hw6.mad.uncc.weather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by sures on 10/19/2016.
 */
public class GetAsyncTaskWeather extends AsyncTask<String, Void, ArrayList<ArrayList<Weather>>> {

    IData activity;
    //CityWeatherActivity activity;

    public GetAsyncTaskWeather(IData activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<ArrayList<Weather>> doInBackground(String... params) {
        try {
            URL url=new URL(params[0]);
            Log.d("params",params[0]);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode=connection.getResponseCode();

            if(statusCode==HttpURLConnection.HTTP_OK){
                Log.d("statuscode",String.valueOf(statusCode));
                BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder=new StringBuilder();
                String line=reader.readLine();
                while(line!=null){
                    stringBuilder.append(line);
                    line=reader.readLine();
                    Log.d("sb",stringBuilder.toString());

                }
                return WeatherUtil.WeatherJSONParser.parseWeather(stringBuilder.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<Weather>> weathers) {
        super.onPostExecute(weathers);

            activity.setWeathers(weathers);
        


    }
    interface IData
    {
        public void setWeathers(ArrayList<ArrayList<Weather>> weathers);
    }
}
