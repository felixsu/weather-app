package felix.com.weatherapp.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;

import felix.com.weatherapp.R;
import felix.com.weatherapp.adapter.DayAdapter;
import felix.com.weatherapp.weather.DailyForecast;

public class DailyForecastActivity extends ListActivity {
    DailyForecast[] mDailyForecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(getString(R.string.key_daily));
        mDailyForecasts = Arrays.copyOf(parcelables, parcelables.length, DailyForecast[].class);

        DayAdapter adapter = new DayAdapter(this, mDailyForecasts);
        setListAdapter(adapter);
    }

}
