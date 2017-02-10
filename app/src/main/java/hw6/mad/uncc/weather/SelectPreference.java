package hw6.mad.uncc.weather;

import android.os.Bundle;
import android.preference.PreferenceActivity;
/**
 * Created by sures on 10/19/2016.
 */
public class SelectPreference extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencesavailable);
    }


}
