package hw6.mad.uncc.weather;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sures on 10/19/2016.
 */
public class CityDAO {
    private SQLiteDatabase db;


    public CityDAO(SQLiteDatabase db){
        this.db = db;
    }

    public long save(City city){
        ContentValues values = new ContentValues();
        values.put(CityTable.COLUMN_CITY, city.getCityname());
        values.put(CityTable.COLUMN_COUNTRY, city.getCountry());
        values.put(CityTable.COLUMN_DATE, city.getDate());
        values.put(CityTable.COLUMN_TEMP, city.getTemperature());
        values.put(CityTable.COLUMN_FAVOURITE, city.getFavourite());
        return db.insert(CityTable.TABLENAME, null, values);
    }

    public boolean update(City city){
        ContentValues values = new ContentValues();
        values.put(CityTable.COLUMN_CITY, city.getCityname());
        values.put(CityTable.COLUMN_COUNTRY, city.getCountry());
        values.put(CityTable.COLUMN_DATE, city.getDate());
        values.put(CityTable.COLUMN_TEMP, city.getTemperature());
        values.put(CityTable.COLUMN_FAVOURITE, city.getFavourite());
        return db.update(CityTable.TABLENAME, values, CityTable.COLUMN_ID + "= ?", new String[]{city.get_id()+""}) > 0;
    }

    public boolean delete(City city){
        return db.delete(CityTable.TABLENAME, CityTable.COLUMN_ID + "=?", new String[]{city.get_id()+""}) > 0;
    }

    public City get(long id){
        City city = null;
        Cursor c = db.query(true, CityTable.TABLENAME, new String[] {
                        CityTable.COLUMN_ID, CityTable.COLUMN_CITY, CityTable.COLUMN_COUNTRY,CityTable.COLUMN_DATE,
                        CityTable.COLUMN_TEMP, CityTable.COLUMN_FAVOURITE },
                CityTable.COLUMN_ID + "= ?" ,
                new String[] {id + ""}, null, null, null, null, null);

        if (c != null && c.moveToFirst()){
            city = buidCityFromCursor(c);
            if (!c.isClosed()){
                c.close();
            }
        }
        return city;
    }

    public List<City> getAll(){
        List<City> cities = new ArrayList<City>();
        Cursor c = db.query(CityTable.TABLENAME, new String[] {
                        CityTable.COLUMN_ID, CityTable.COLUMN_CITY, CityTable.COLUMN_COUNTRY,CityTable.COLUMN_DATE,
                        CityTable.COLUMN_TEMP, CityTable.COLUMN_FAVOURITE},
                null, null, null, null, null);

        if (c != null && c.moveToFirst()){
            do {
                City city = buidCityFromCursor(c);
                if(city != null){
                    cities.add(city);
                }
            } while (c.moveToNext());

            if (!c.isClosed()){
                c.close();
            }
        }
        return cities;
    }

    public City buidCityFromCursor(Cursor c){
        City city = null;
        if(c != null){
            city = new City();
            city.set_id(c.getLong(0));
            city.setCityname(c.getString(1));
            city.setCountry(c.getString(2));
            city.setDate(c.getString(3));
            city.setTemperature(c.getString(4));
            city.setFavourite(c.getInt(5));
        }
        return city;
    }
}
