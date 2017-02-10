package hw6.mad.uncc.weather;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sures on 10/19/2016.
 */
public class RecycleViewHourlyAdapter extends RecyclerView.Adapter<RecycleViewHourlyAdapter.HourTempHolder>{
    private ArrayList<Weather> hourlytemp;
    String munit;
    public RecycleViewHourlyAdapter(ArrayList<Weather> hourlytemp, String unit) {
        this.hourlytemp = hourlytemp;
        munit= unit;
    }

    @Override
    public RecycleViewHourlyAdapter.HourTempHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_temp_layout, parent, false);
        return new RecycleViewHourlyAdapter.HourTempHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecycleViewHourlyAdapter.HourTempHolder holder, int position) {
        Weather forecast = hourlytemp.get(position);
        holder.loadData(forecast,munit);
    }

    @Override
    public int getItemCount() {
        return hourlytemp.size();
    }

    public static class HourTempHolder extends RecyclerView.ViewHolder {

        TextView time, temp, humid, pressure, wind, cond;
        ImageView icon;
        View itemView;

        public HourTempHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            time = (TextView) itemView.findViewById(R.id.hourly_time);
            temp = (TextView) itemView.findViewById(R.id.hourly_temp);
            cond = (TextView) itemView.findViewById(R.id.hourly_cond);
            pressure = (TextView) itemView.findViewById(R.id.hourly_pressure);
            humid = (TextView) itemView.findViewById(R.id.hourly_humid);
            wind = (TextView) itemView.findViewById(R.id.hourly_wind);
            icon = (ImageView) itemView.findViewById(R.id.hourly_icon);
        }

        public void loadData(Weather weather, String unit){
            Double temp1;
            time.setText(weather.getTime());
            Picasso.with(itemView.getContext()).load(weather.getIconurl()).into(icon);
            Log.d("hourly temp",weather.getTemp());
            Log.d("unit",unit);
            if (unit.equals("c")){
               temp1=Double.valueOf(Math.round(Double.parseDouble(weather.getTemp())-273.5));
                temp.setText("Temperature: "+temp1+"\u2103");
            }else if (unit.equals("f")){
                temp1= Double.valueOf(Math.round(Double.parseDouble(weather.getTemp())*9/5-459.67));
                temp.setText("Temperature: "+temp1+"\u2109");
            }
            cond.setText("Condition: " + weather.getClimatetype());
            pressure.setText("Pressure: " + weather.getPressure());
            humid.setText("Humidity: " + weather.getHumidity());
            wind.setText("Wind: " + weather.getWs()+", "+weather.getWd());
        }
    }
}
