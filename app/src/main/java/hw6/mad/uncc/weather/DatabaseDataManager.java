package hw6.mad.uncc.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by sures on 10/18/2016.
 */
public class DatabaseDataManager {
    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private CityDAO cityDAO;

    public DatabaseDataManager(Context mContext){
        this.mContext = mContext;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        cityDAO = new CityDAO(db);
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public CityDAO getCitiesDAO(){
        return this.cityDAO;
    }

    public long saveCity(City city){
        return this.cityDAO.save(city);
    }

    public boolean updateCity(City city){
        return this.cityDAO.update(city);
    }

    public boolean deleteCity(City city){
        return this.cityDAO.delete(city);
    }

    public City getCity(long id){
        return this.cityDAO.get(id);
    }

    public List<City> getAllCities(){
        return this.cityDAO.getAll();
    }
}
