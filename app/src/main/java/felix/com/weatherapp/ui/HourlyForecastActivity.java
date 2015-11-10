package felix.com.weatherapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import felix.com.weatherapp.R;
import felix.com.weatherapp.adapter.HourAdapter;
import felix.com.weatherapp.weather.HourlyForecast;

public class HourlyForecastActivity extends AppCompatActivity {
    private HourlyForecast[] mHourlyForecasts;

    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly);
        ButterKnife.bind(this);

        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(getString(R.string.key_hourly));
        mHourlyForecasts = Arrays.copyOf(parcelables, parcelables.length, HourlyForecast[].class);

        HourAdapter adapter = new HourAdapter(mHourlyForecasts);
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

    }

}
