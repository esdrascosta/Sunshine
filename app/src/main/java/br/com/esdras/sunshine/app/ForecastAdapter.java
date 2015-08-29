package br.com.esdras.sunshine.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.esdras.sunshine.app.data.DayInfo;
import br.com.esdras.sunshine.app.data.Utils;

/**
 * Created by esdras on 28/08/15.
 */
public class ForecastAdapter extends ArrayAdapter<DayInfo> {


    public ForecastAdapter(Context context, int idLayout , List<DayInfo> list){
        super(context, idLayout, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DayInfo dayInfo = (DayInfo) getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_forecast, parent, false);
        }

        ImageView image = (ImageView)convertView.findViewById(R.id.list_item_icon);
        image.setImageResource(Utils.getIconResourceForWeatherCondition(dayInfo.weatherConditionId));

        TextView dateTextView = (TextView) convertView.findViewById(R.id.list_item_date_textview);
        String day = Utils.getFriendlyDayString(getContext(), dayInfo.dateTime);
        dateTextView.setText(day);

        TextView forecastTextView = (TextView)  convertView.findViewById(R.id.list_item_forecast_textview);
        forecastTextView.setText(dayInfo.description);

        TextView highTemperatureTextView = (TextView)  convertView.findViewById(R.id.list_item_high_textview);
        highTemperatureTextView.setText(Utils.formatTemperature(getContext(), dayInfo.maxTemperature));

        TextView lowTemperatureTextView = (TextView)  convertView.findViewById(R.id.list_item_low_textview);
        lowTemperatureTextView.setText(Utils.formatTemperature(getContext(), dayInfo.minTemperature));

        return convertView;
    }
}
