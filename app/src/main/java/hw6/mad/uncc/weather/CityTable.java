package hw6.mad.uncc.weather;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sures on 10/19/2016.
 */
public class CityTable {
    static final String TABLENAME="cities";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_CITY="cityName";
    static final String COLUMN_COUNTRY = "country";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_TEMP="temperature";
    static final String COLUMN_FAVOURITE="favorite";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLENAME + " (");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_CITY + " text not null, ");
        sb.append(COLUMN_COUNTRY + " text not null, ");
        sb.append(COLUMN_DATE + " text not null, ");
        sb.append(COLUMN_TEMP + " real not null, ");
        sb.append(COLUMN_FAVOURITE + " integer not null);");

        try{
            db.execSQL(sb.toString());
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        CityTable.onCreate(db);
    }
}
