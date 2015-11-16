package felix.com.weatherapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import felix.com.weatherapp.Constant;
import felix.com.weatherapp.R;
import felix.com.weatherapp.adapter.DayAdapter;
import felix.com.weatherapp.weather.DailyForecast;

public class DailyForecastActivity extends AppCompatActivity {
    @Bind(android.R.id.list)
    ListView mListView;

    @Bind(android.R.id.empty)
    TextView mEmptyTextView;

    private DailyForecast[] mDailyForecasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        ButterKnife.bind(this);
        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(getString(R.string.key_daily));
        mDailyForecasts = Arrays.copyOf(parcelables, parcelables.length, DailyForecast[].class);

        DayAdapter adapter = new DayAdapter(this, mDailyForecasts);
        mListView.setAdapter(adapter);
        mListView.setEmptyView(mEmptyTextView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfWeek = mDailyForecasts[position].getDayOfTheWeek();
                String summary = mDailyForecasts[position].getSummary();
                String maxTemperature = String.valueOf(mDailyForecasts[position].getMaxTemperature(Constant.CELSIUS));

                String message = String.format("on %s it will be %s with max temperature as %s", dayOfWeek, summary, maxTemperature);
                Toast.makeText(DailyForecastActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
