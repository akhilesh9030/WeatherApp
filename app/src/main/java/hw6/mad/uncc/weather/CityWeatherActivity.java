package hw6.mad.uncc.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CityWeatherActivity extends AppCompatActivity  implements GetAsyncTaskWeather.IData {

    public static final String ERROR_KEY = "error_msg";
    TextView location_currentcity, threehourlyperioddate;
    String url;
    ProgressDialog pd;
    String city,country,city_mod;


    RecycleViewDailyAdapter dayAdapter;
    RecycleViewHourlyAdapter hourAdapter;
    DatabaseDataManager dm;
    RecyclerView dayRecyclerView, hourlyRecyclerView;
    LinearLayoutManager daylinearLayoutManager, hourlinearLayoutManager;

    ArrayList<ArrayList<Weather>> daily_temp_list;
    List<City> cities_saved;

    String temp_unit=MainActivity.temp_unit;
    int pos=0;

    SharedPreferences sharedPreferences;
    TempOptionChangeListener mListner = null;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Hourly Data");

        location_currentcity = (TextView) findViewById(R.id.currentcity);
        threehourlyperioddate = (TextView) findViewById(R.id.threehourlyperioddate);

        inflateLists();

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        mListner = new TempOptionChangeListener();
        sharedPreferences.registerOnSharedPreferenceChangeListener(mListner);

        country = getIntent().getStringExtra(MainActivity.COUNTRY_KEY);
        city = getIntent().getStringExtra(MainActivity.CITY_KEY);
        city_mod = city.replace(" ","_");
        String url ="http://api.openweathermap.org/data/2.5/forecast?q="+city_mod+","+country+"&mode=json&appid=9da2b5bfeafa25958626d4536312211e";
        Log.d("url",url);
        new GetAsyncTaskWeather(this).execute(url);

        location_currentcity.setText("Daily Forecast for " + city + ", " + country);

        new GetAsyncTaskWeather(this).execute(url);

    }

    public void inflateLists(){
        dayRecyclerView = (RecyclerView) findViewById(R.id.fiveday_RecyclerView);
        daylinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        dayRecyclerView.setLayoutManager(daylinearLayoutManager);

        hourlyRecyclerView = (RecyclerView) findViewById(R.id.threehour_RecyclerView);
        hourlinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        hourlyRecyclerView.setLayoutManager(hourlinearLayoutManager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.menu_city,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dm = new DatabaseDataManager(CityWeatherActivity.this);
        City save_city = new City();
        switch (item.getItemId()) {
            case R.id.save_city:
                boolean alreadyexists = false;
                cities_saved = dm.getAllCities();
                int index = 0;
                for (int i = 0; i < cities_saved.size(); i++) {
                    if (cities_saved.get(i).getCityname().equals(city)) {
                        alreadyexists = true;
                        index = i;
                        break;
                    }
                }
                if (alreadyexists) {
                    save_city.set_id(cities_saved.get(index).get_id());
                    save_city.setCityname(city);
                    save_city.setCountry(country);
                    save_city.setTemperature(daily_temp_list.get(0).get(0).getTemp());
                    save_city.setDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                    save_city.setFavourite(0);
                    dm.updateCity(save_city);
                    Toast.makeText(this, "City Updated", Toast.LENGTH_SHORT).show();

                } else {
                    save_city.setCityname(city);
                    save_city.setDate(new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
                    save_city.setCountry(country);
                    save_city.setTemperature(daily_temp_list.get(0).get(0).getTemp());
                    dm.saveCity(save_city);
                    Toast.makeText(this, "City Saved", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.settings_temp:
                Intent i = new Intent(CityWeatherActivity.this, SelectPreference.class);
                startActivity(i);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void setWeathers(ArrayList<ArrayList<Weather>> weathers) {
        try{
            pd.dismiss();
            if(weathers == null){
               // Toast.makeText(CityWeatherActivity.this,"Invalid city and Country", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra(ERROR_KEY,"Invalid city and Country");
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }, 000);
            }
            else {
                daily_temp_list = weathers;
                String changed_temp = sharedPreferences.getString("unit_selected", "c");
                if (!temp_unit.equals(changed_temp)) {
                    Toast.makeText(CityWeatherActivity.this, "Temperature unit has been changed to " + changed_temp, Toast.LENGTH_SHORT).show();
                }
                temp_unit = changed_temp;

                dayAdapter = new RecycleViewDailyAdapter(daily_temp_list, temp_unit);
                dayRecyclerView.setAdapter(dayAdapter);
                dayAdapter.notifyDataSetChanged();
                threehourlyperioddate.setText(weathers.get(0).get(0).getDate());
                hourAdapter = new RecycleViewHourlyAdapter(daily_temp_list.get(0), temp_unit);
                hourlyRecyclerView.setAdapter(hourAdapter);
                hourAdapter.notifyDataSetChanged();
                dayRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(CityWeatherActivity.this, hourlyRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        threehourlyperioddate.setText(daily_temp_list.get(position).get(0).getDate());
                        pos = position;
                        String temp_unit = sharedPreferences.getString("unit_selected", "c");
                        hourAdapter = new RecycleViewHourlyAdapter(daily_temp_list.get(position), temp_unit);
                        hourlyRecyclerView.setAdapter(hourAdapter);
                        hourAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }

        } catch (Exception e){
            Toast.makeText(this,"City or County is invalid",Toast.LENGTH_LONG).show();
        }
    }


        private class TempOptionChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String temp_unit_change = sharedPreferences.getString("unit_selected","c");
            if (!temp_unit.equals(temp_unit_change)){
                Toast.makeText(CityWeatherActivity.this,"Temperature unit change to "+temp_unit_change,Toast.LENGTH_SHORT).show();
            }
            temp_unit=temp_unit_change;

            dayAdapter = new RecycleViewDailyAdapter(daily_temp_list,temp_unit);
            dayRecyclerView.setAdapter(dayAdapter);
            dayAdapter.notifyDataSetChanged();
            hourAdapter = new RecycleViewHourlyAdapter(daily_temp_list.get(pos),temp_unit);
            hourlyRecyclerView.setAdapter(hourAdapter);
            hourAdapter.notifyDataSetChanged();
        }
    }


}
