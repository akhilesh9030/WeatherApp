package hw6.mad.uncc.weather;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecycleViewSavedCitiesAdapter.RefreshSavedCities{

    static final String CITY_KEY ="CITY" ;
    static final String COUNTRY_KEY ="COUNTRY" ;
    public static final int REQ_CODE = 100;
    DatabaseDataManager dm;
    EditText cityText,countryText;
    Button submit_button;
    String c,countryvalue;
    List<City> savedcities;
    TextView msg;
    static  String temp_unit="c";
    RecyclerView cities_saved;
    LinearLayoutManager linearLayoutManager;
    RecycleViewSavedCitiesAdapter adapter;
    ImageView favourite;


    SharedPreferences sharedPreferences;
    TempOptionChangeListener mListner = null;

    @Override
    protected void onResume() {
        super.onResume();
        savedcities =  dm.getAllCities();
        Sort(savedcities);
        addcitytoRecyclerView();

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm = new DatabaseDataManager(this);
        cityText = (EditText) findViewById(R.id.cityEditText);
        countryText = (EditText) findViewById(R.id.countryEditText);
        submit_button = (Button) findViewById(R.id.submitButton);
        favourite = (ImageView) findViewById(R.id.favbut);
        msg = (TextView) findViewById(R.id.message);
        cities_saved = (RecyclerView) findViewById(R.id.cities_saved);
        linearLayoutManager = new LinearLayoutManager(this);
        cities_saved.setLayoutManager(linearLayoutManager);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mListner = new TempOptionChangeListener();
        sharedPreferences.registerOnSharedPreferenceChangeListener(mListner);

        savedcities =  dm.getAllCities();
        Sort (savedcities);
        addcitytoRecyclerView();

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cityText.getText().toString().equals("") || countryText.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Enter city name and country name", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isConnectedOnline()){
                        Intent i = new Intent(MainActivity.this,CityWeatherActivity.class);
                        i.putExtra(MainActivity.CITY_KEY,cityText.getText().toString());
                        i.putExtra(MainActivity.COUNTRY_KEY,countryText.getText().toString());
                        startActivityForResult(i,REQ_CODE);
                    }
                    else{
                        Toast.makeText(MainActivity.this,"No Internet Connection" ,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private boolean isConnectedOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nw = cm.getActiveNetworkInfo();

        if(nw != null && nw.isConnected())
        {
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            if(requestCode == REQ_CODE){
                String errorMsg  = data.getExtras().getString(CityWeatherActivity.ERROR_KEY);
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_settings:
                Intent i = new Intent(MainActivity.this, SelectPreference.class);
                startActivity(i);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void finish() {
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        dm.close();
        super.onDestroy();
    }

    @Override
    public void refresh() {
        Toast.makeText(this,"City Deleted",Toast.LENGTH_SHORT).show();
        onResume();
    }

    private class TempOptionChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            addcitytoRecyclerView();
        }
    }

    public void addcitytoRecyclerView(){
        String changed_temp = sharedPreferences.getString("unit_selected","c");
        if (!temp_unit.equals(changed_temp)){
            Toast.makeText(MainActivity.this,"Temperature unit changed to "+changed_temp,Toast.LENGTH_SHORT).show();
        }
        temp_unit=changed_temp;
        adapter = new RecycleViewSavedCitiesAdapter(savedcities, temp_unit, MainActivity.this);
        if (savedcities.size() > 0){
            msg.setText("Saved Cities");
            cities_saved.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else{
            msg.setText("There are no cities to display. Search the city from the search box and save.");
            cities_saved.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    public  void Sort (List<City> list){
        int index = 0;

        for(int i=0;i<list.size();i++){
            if(list.get(i).getFavourite()==1){
                City temp =list.get(i);
                list.remove(i);
                list.add(index,temp);
                index++;
            }
        }
    }
}
