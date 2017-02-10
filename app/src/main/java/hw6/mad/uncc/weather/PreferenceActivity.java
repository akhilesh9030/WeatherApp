package hw6.mad.uncc.weather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PreferenceActivity extends AppCompatActivity {
    LinearLayout l;
    public static final String TEMP_PREF = "temp_pref";
    public static final String CHOOSEN_TEMP = "choosen_temp";
    int selectedtemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String[] temps={"c","f"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        l = (LinearLayout) findViewById(R.id.linearLayout);

        AlertDialog.Builder buider= new AlertDialog.Builder(this);
        buider.setTitle("Choose Unit")
        .setSingleChoiceItems(temps, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sp = PreferenceActivity.this.getSharedPreferences(TEMP_PREF, Context.MODE_PRIVATE);
                selectedtemp=which;
                SharedPreferences.Editor sp_editor = sp.edit();
                sp_editor.putString(CHOOSEN_TEMP,temps[which]);
                sp_editor.commit();

            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("which",which+"");
                if(selectedtemp==0) {
                    Toast.makeText(PreferenceActivity.this, "Temperature changed  to Celsius", Toast.LENGTH_SHORT).show();
                }
                else if(selectedtemp==1){
                    Toast.makeText(PreferenceActivity.this, "Temperature changed  to Farenheit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog al = buider.create();

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                al.show();
            }
        });



    }
}
