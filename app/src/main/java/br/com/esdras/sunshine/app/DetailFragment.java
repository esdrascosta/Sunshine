package br.com.esdras.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.esdras.sunshine.app.data.DayInfo;
import br.com.esdras.sunshine.app.data.Utils;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG="#SunShineApp";
    private DayInfo dayInfo;
    private ImageView mIconView;
    private TextView mDateView;
    private TextView mFriendlyDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.action_settings){
            Intent intent = new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("txt/plain");
        intent.putExtra(Intent.EXTRA_TEXT, dayInfo + FORECAST_SHARE_HASHTAG);
        return  intent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        ShareActionProvider shareActionProvider =
                (ShareActionProvider)MenuItemCompat.getActionProvider(item);

        if(shareActionProvider != null){
            shareActionProvider.setShareIntent( createShareIntent());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if(intent != null && intent.hasExtra("SERIAL")){

            dayInfo = (DayInfo) intent.getSerializableExtra("SERIAL");

            mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
            mIconView.setImageResource(Utils.getArtResourceForWeatherCondition(dayInfo.weatherConditionId));

            mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
            mDateView.setText(Utils.getFormattedMonthDay(getActivity(), dayInfo.dateTime));

            mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
            mFriendlyDateView.setText(Utils.getDayName(getActivity(), dayInfo.dateTime));

            mDescriptionView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
            mDescriptionView.setText(dayInfo.description);

            mHighTempView = (TextView) rootView.findViewById(R.id.detail_high_textview);
            mHighTempView.setText(Utils.formatTemperature(getActivity(),dayInfo.maxTemperature));

            mLowTempView = (TextView) rootView.findViewById(R.id.detail_low_textview);
            mLowTempView.setText(Utils.formatTemperature(getActivity(),dayInfo.minTemperature));

            mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
            mHumidityView.setText(getActivity().getString(R.string.format_humidity, dayInfo.humidity));

            mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
            mWindView.setText(Utils.getFormattedWind(getActivity(), dayInfo.windSpeed, dayInfo.windDirection));

            mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);
            mPressureView.setText(getActivity().getString(R.string.format_pressure, dayInfo.pressure));
        }

        return rootView;
    }
}
