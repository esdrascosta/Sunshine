package br.com.esdras.sunshine.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import br.com.esdras.sunshine.app.data.WeatherContract;
import br.com.esdras.sunshine.app.data.WeatherDBHelper;

/**
 * Created by esdras on 26/08/15.
 */
public class TestDB extends AndroidTestCase {

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    void deleteDataBase() {
        mContext.deleteDatabase(WeatherDBHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        deleteDataBase();
    }


    public void testLocationTable() {
        insertLocation();
    }

    private long insertLocation() {
        WeatherDBHelper weatherDBHelper = new WeatherDBHelper(getContext());


        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testCityName);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, testLongitude);
        contentValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTINGS, testLocationSetting);

        SQLiteDatabase writableDatabase = weatherDBHelper.getWritableDatabase();
        long locationRowId = writableDatabase.insert(WeatherContract.LocationEntry.TABLE_NAME, null,
                contentValues);
        writableDatabase.close();

        assertTrue(locationRowId != -1);


        SQLiteDatabase readableDatabase = weatherDBHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(
                WeatherContract.LocationEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: Nenhum valor foi retornado na consulta", cursor.moveToFirst());

        long indexInsertedValue = cursor.getLong(cursor.getColumnIndex(WeatherContract.LocationEntry._ID));

        assertFalse("Error: Mais de uma valor foi retornado da consulta", cursor.moveToNext());

        assertEquals("Os indices deveriam ser iguais", locationRowId, indexInsertedValue);

        cursor.close();
        readableDatabase.close();

        return indexInsertedValue;
    }

    public void testWeatherTable(){
        long locationRowId = insertLocation();
        assertFalse("Error: localização não inserida coretamento", locationRowId == -1L);

        WeatherDBHelper weatherDBHelper = new WeatherDBHelper(getContext());
        SQLiteDatabase db = weatherDBHelper.getWritableDatabase();

        final long TEST_DATE = 1419033600L;  // 20 de Dezembro de 2014

        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, TEST_DATE);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, 321);

        long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1);


        Cursor cursor = db.query(WeatherContract.WeatherEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Deveria retornar um resultado",cursor.moveToFirst());
        assertFalse("Não deveria haver outro valor na consulta", cursor.moveToNext());

        cursor.close();
        weatherDBHelper.close();
    }

}
