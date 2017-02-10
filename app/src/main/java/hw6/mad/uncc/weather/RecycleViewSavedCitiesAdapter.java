package hw6.mad.uncc.weather;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sures on 10/20/2016.
 */
public class RecycleViewSavedCitiesAdapter extends RecyclerView.Adapter<RecycleViewSavedCitiesAdapter.SavedCityHolder>{

    private List<City> cities;
    static DatabaseDataManager dm;
    RefreshSavedCities refreshSavedCities;
    Context context;
    String unit;

    public RecycleViewSavedCitiesAdapter(List<City> cities, String unit, Context context) {
        this.cities =  cities;
        this.unit = unit;
        this.context = context;
    }

    @Override
    public RecycleViewSavedCitiesAdapter.SavedCityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_cities_row, parent, false);
        return new SavedCityHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(SavedCityHolder holder, int position) {
        City itemCity = cities.get(position);
        holder.loadData(itemCity, unit);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class SavedCityHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        City attachCity;
        TextView city, temp, date;
        ImageButton fav;


        public SavedCityHolder(View itemView) {
            super(itemView);
            dm = new DatabaseDataManager(itemView.getContext());
            refreshSavedCities = (RefreshSavedCities) context;
            city = (TextView) itemView.findViewById(R.id.city);
            temp = (TextView) itemView.findViewById(R.id.temp);
            date = (TextView) itemView.findViewById(R.id.date);
            fav = (ImageButton) itemView.findViewById(R.id.favbut);
            fav.setClickable(true);
            fav.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.favbut){
                if(attachCity.getFavourite() == 0) {
                    attachCity.setFavourite(1);
                    fav.setImageResource(R.drawable.star_gold);
                } else if(attachCity.getFavourite() == 1){
                    attachCity.setFavourite(0);
                    fav.setImageResource(R.drawable.star_gray);
                }
                dm.updateCity(attachCity);
            } else{
                Intent intent = new Intent(itemView.getContext(),CityWeatherActivity.class);
                intent.putExtra(MainActivity.CITY_KEY, attachCity.getCityname());
                intent.putExtra(MainActivity.COUNTRY_KEY, attachCity.getCountry());
                v.getContext().startActivity(intent);
            }
        }


        public void loadData(City c, String units) {
            attachCity = c;
            String temp_unit=units;
            Double temp1=0.0;

            city.setText(c.getCityname()+", "+c.getCountry());
            if (temp_unit.equals("c") ){
                temp1 = Double.valueOf(Math.round(Double.parseDouble(c.getTemperature())-273.5));
                temp.setText(temp1+"\u2103");

            }else if (temp_unit.equals("f")){
                temp1=Double.valueOf(Math.round(Double.parseDouble(c.getTemperature())*9/5-459.67));
                temp.setText(temp1+"\u2109");

            }
            date.setText("Updated on: "+c.getDate());
            if(c.getFavourite() == 0)
                fav.setImageResource(R.drawable.star_gray);
            else if(c.getFavourite() == 1)
                fav.setImageResource(R.drawable.star_gold);
        }


        @Override
        public boolean onLongClick(View v) {
            dm.deleteCity(attachCity);
            refreshSavedCities.refresh();
            return true;
        }
    }

    public interface RefreshSavedCities{
        public void refresh();
    }
}
