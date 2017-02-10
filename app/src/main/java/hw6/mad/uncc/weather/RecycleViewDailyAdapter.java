package hw6.mad.uncc.weather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by sures on 10/19/2016.
 */
public class RecycleViewDailyAdapter extends RecyclerView.Adapter<RecycleViewDailyAdapter.DayTempHolder>{
    static ArrayList<ArrayList<Weather>> dailytemp;
    String temp_unit;

    public RecycleViewDailyAdapter(ArrayList<ArrayList<Weather>> dailytemp, String unit) {

        this.dailytemp = dailytemp;
        temp_unit= unit;
    }

    @Override
    public RecycleViewDailyAdapter.DayTempHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_temp_layout, parent, false);
        return new RecycleViewDailyAdapter.DayTempHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecycleViewDailyAdapter.DayTempHolder holder, int position) {
        ArrayList<Weather> dayForecast = dailytemp.get(position);
        holder.loadData(dayForecast,temp_unit);
    }

   @Override
    public int getItemCount() {

        return dailytemp.size();
    }

    public static class DayTempHolder extends RecyclerView.ViewHolder {

        TextView date, temp;
        ImageView icon;
        View itemView;
        ArrayList<Weather> al;


        public DayTempHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            temp = (TextView) itemView.findViewById(R.id.daily_temp);
            date = (TextView) itemView.findViewById(R.id.daily_date);
            icon = (ImageView) itemView.findViewById(R.id.daily_icon);

        }


        public void loadData(ArrayList<Weather> list, String units){
            al = list;
            int median = list.size()/2;
            Double settemp;
            if (units.equals("c")){
                settemp = Double.valueOf(Math.round(avgTemp(list)-273.5));
                temp.setText(settemp+"\u2103");
            }else if (units.equals("f")){
                settemp = Double.valueOf(Math.round(avgTemp(list)*9/5-459.67));
                temp.setText(settemp+"\u2109");
            }
            date.setText(list.get(0).getDate());
            Picasso.with(itemView.getContext()).load(list.get(median).getIconurl()).into(icon);
        }



        private double avgTemp(ArrayList<Weather> list) {
            double sum = 0;
            Double d;
            for(int i = 0; i < list.size(); i++){
                sum = sum + Double.parseDouble(list.get(i).getTemp());
            }
            d=Double.valueOf(Math.round(sum/list.size()));
            return d;
        }
    }
}
