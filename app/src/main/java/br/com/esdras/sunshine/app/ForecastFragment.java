package br.com.esdras.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.esdras.sunshine.app.data.DayInfo;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private ForecastAdapter adapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_refresh) {
            updateWeather();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String locationDefault = sp.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        new FecthWeatherTask().execute(locationDefault);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        adapter =
                new ForecastAdapter(getActivity(), R.layout.list_item_forecast,new ArrayList<DayInfo>());


        View viewRoot = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) viewRoot.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> item, View view, int position, long id) {

                DayInfo itemSelected = (DayInfo) adapter.getItem(position);

                Intent intent = new Intent(ForecastFragment.this.getActivity(), DetailActivity.class)
                        .putExtra("SERIAL",itemSelected);

                ForecastFragment.this.startActivity(intent);

            }
        });

        return viewRoot;
    }

    public class FecthWeatherTask extends AsyncTask<String, Void, List<DayInfo> > {

        private final String LOG_TAG = FecthWeatherTask.class.getSimpleName();


        private String formatHighLows(double high, double low) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String typeUnit = sharedPreferences.getString(getString(R.string.pref_units_key), "");

            if (typeUnit.equals(getString(R.string.pref_units_imperial))) {
                high = (high * 1.8) + 32;
                low = (low * 1.8) + 32;
            } else if (!typeUnit.equals(getString(R.string.pref_units_metric))) {
                Log.d(LOG_TAG, "Units not found: " + typeUnit);
            }

            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);


            String highLowStr = roundedHigh + "/" + roundedLow;
            return highLowStr;
        }

        private List<DayInfo> getWeatherDataFromJson(String forecastJsonStr)
                throws JSONException {

            // Informação de Localização
            final String OWM_CITY = "city";
            final String OWM_CITY_NAME = "name";
            final String OWM_COORD = "coord";

            // Coordenadas da Localização
            final String OWM_LATITUDE = "lat";
            final String OWM_LONGITUDE = "lon";

            final String OWM_LIST = "list";

            final String OWM_PRESSURE = "pressure";
            final String OWM_HUMIDITY = "humidity";
            final String OWM_WINDSPEED = "speed";
            final String OWM_WIND_DIRECTION = "deg";

            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";

            final String OWM_WEATHER = "weather";
            final String OWM_DESCRIPTION = "main";
            final String OWM_WEATHER_ID = "id";
            final String OWM_WEATHER_ICON ="icon";

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            LinkedList<DayInfo> result = new LinkedList<>();


            Time dayTime = new Time();
            dayTime.setToNow();

            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            dayTime = new Time();

            for(int i = 0; i < weatherArray.length(); i++) {

                long dateTime;
                double pressure;
                int humidity;
                double windSpeed;
                double windDirection;

                double high;
                double low;

                String description;
                int weatherId;
                String icon;

                JSONObject dayForecast = weatherArray.getJSONObject(i);

                dateTime = dayTime.setJulianDay(julianStartDay + i);

                pressure = dayForecast.getDouble(OWM_PRESSURE);
                humidity = dayForecast.getInt(OWM_HUMIDITY);
                windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                JSONObject weatherObject =
                        dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                description = weatherObject.getString(OWM_DESCRIPTION);
                weatherId = weatherObject.getInt(OWM_WEATHER_ID);
                icon = weatherObject.getString(OWM_WEATHER_ICON);

                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                high = temperatureObject.getDouble(OWM_MAX);
                low = temperatureObject.getDouble(OWM_MIN);


                DayInfo day = new DayInfo();
                day.weatherConditionId = weatherId;
                day.dateTime = dateTime;
                day.description = description;
                day.maxTemperature = high;
                day.minTemperature = low;
                day.pressure = pressure;
                day.humidity = humidity;
                day.windSpeed = windSpeed;
                day.windDirection = windDirection;
                day.icon = icon;

                result.add(day);

            }


            return result;

        }

        @Override
        protected List<DayInfo> doInBackground(String... params) {


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String MODE = "json";
            final String METRIC = "metric";
            final int NUM_DAYS = 7;
            try {

                Uri builderURI = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter("q", params[0])
                        .appendQueryParameter("mode", MODE)
                        .appendQueryParameter("units", METRIC)
                        .appendQueryParameter("cnt", String.valueOf(NUM_DAYS))
                        .appendQueryParameter("lang","pt").build();

                URL url = new URL(builderURI.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FecthWeatherTask", "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FecthWeatherTask", "Error ao fechar o stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<DayInfo> dayInfos) {
            if (dayInfos != null) {

                adapter.clear();

                for (DayInfo result : dayInfos)
                    adapter.add(result);
            }
        }
    }
}