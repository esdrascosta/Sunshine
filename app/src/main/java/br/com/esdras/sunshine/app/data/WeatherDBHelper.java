package br.com.esdras.sunshine.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.esdras.sunshine.app.data.WeatherContract.WeatherEntry;
import br.com.esdras.sunshine.app.data.WeatherContract.LocationEntry;
/**
 * Created by esdras on 23/08/15.
 */
public class WeatherDBHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    public static final String DATABASE_NAME="wheather.db";

    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final StringBuilder SQL_CREATE_TABLES = new StringBuilder();
        SQL_CREATE_TABLES.append(createLocationEntry());
        SQL_CREATE_TABLES.append(createWeatherEntry());
        sqLiteDatabase.execSQL(SQL_CREATE_TABLES.toString());
    }

    private StringBuilder createLocationEntry(){
        final StringBuilder SQL_CREATE_TABLE_LOCATION = new StringBuilder();
        SQL_CREATE_TABLE_LOCATION.append("CREATE TABLE "+ LocationEntry.TABLE_NAME +" ( ");
        SQL_CREATE_TABLE_LOCATION.append(LocationEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        SQL_CREATE_TABLE_LOCATION.append(LocationEntry.COLUMN_COORD_LAT +" REAL NOT NULL, ");
        SQL_CREATE_TABLE_LOCATION.append(LocationEntry.COLUMN_COORD_LONG +" REAL NOT NULL, ");
        SQL_CREATE_TABLE_LOCATION.append(LocationEntry.COLUMN_LOCATION_SETTINGS +" TEXT UNIQUE NOT NULL, ");
        SQL_CREATE_TABLE_LOCATION.append(LocationEntry.COLUMN_CITY_NAME +" TEXT NOT NULL );");
        return SQL_CREATE_TABLE_LOCATION;
    }


    private StringBuilder createWeatherEntry(){
        final StringBuilder SQL_CREATE_TABLE_WEATHER = new StringBuilder();
        SQL_CREATE_TABLE_WEATHER.append("CREATE TABLE " + WeatherEntry.TABLE_NAME + " ( ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, ");
        SQL_CREATE_TABLE_WEATHER.append(" FOREIGN KEY (" + WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES ");
        SQL_CREATE_TABLE_WEATHER.append(LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), ");
        SQL_CREATE_TABLE_WEATHER.append(" UNIQUE (" + WeatherEntry.COLUMN_DATE + ", ");
        SQL_CREATE_TABLE_WEATHER.append(WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);");
        return SQL_CREATE_TABLE_WEATHER;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
