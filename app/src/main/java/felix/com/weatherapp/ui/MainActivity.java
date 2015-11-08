package felix.com.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import felix.com.weatherapp.Constant;
import felix.com.weatherapp.R;
import felix.com.weatherapp.weather.Current;
import felix.com.weatherapp.weather.DailyForecast;
import felix.com.weatherapp.weather.Forecast;
import felix.com.weatherapp.weather.HourlyForecast;


public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    double mLatitude = -6.215117;
    double mLongitude = 106.824896;

    private Forecast mForecast;

    @Bind(R.id.temperatureLabel)
    TextView mTemperatureLabel;

    @Bind(R.id.iconImageView)
    ImageView mIconImageView;

    @Bind(R.id.precipValue)
    TextView mPrecipValue;

    @Bind(R.id.humidityValue)
    TextView mHumidityValue;

    @Bind(R.id.timeLabel)
    TextView mTimeLabel;

    @Bind(R.id.summaryLabel)
    TextView mSummaryLabel;

    @Bind(R.id.refreshImageView)
    ImageView mRefreshImageView;

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Bind(R.id.locationLabel)
    TextView mLocationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        getForecast(mLatitude, mLongitude);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(mLatitude, mLongitude);
            }
        });
    }

    private void getForecast(double latitude, double longitude) {
        if (isNetworkAvailable()) {
            toggleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().
                    url(buildUrl(latitude, longitude)).
                    build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, response.body().string());
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(String.valueOf(current.getTemperature(Constant.CELSIUS)));
        mTimeLabel.setText("at " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(String.valueOf(current.getHumidity()));
        mPrecipValue.setText(String.valueOf(current.getPrecipChance()) + "%");
        mSummaryLabel.setText(current.getSummary());
        Drawable drawable = getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
        mLocationLabel.setText("Jakarta, ID");

    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setDailyForecast(getDailyDetails(jsonData));
        forecast.setHourlyForecast(getHourlyDetails(jsonData));
        return forecast;
    }

    private DailyForecast[] getDailyDetails(String jsonData) throws JSONException {
        JSONObject jsonForecast = new JSONObject(jsonData);
        JSONObject jsonDaily = jsonForecast.getJSONObject("daily");
        JSONArray jsonDailyData = jsonDaily.getJSONArray("data");

        DailyForecast[] dailyForecasts = new DailyForecast[jsonDailyData.length()];
        for (int i = 0; i < dailyForecasts.length; i++) {
            JSONObject jsonDailyObject = jsonDailyData.getJSONObject(i);

            DailyForecast daily = new DailyForecast();
            daily.setIcon(jsonDailyObject.getString("icon"));
            daily.setSummary(jsonDailyObject.getString("summary"));
            daily.setTime(jsonDailyObject.getLong("time"));
            daily.setMinTemperature(jsonDailyObject.getDouble("temperatureMin"));
            daily.setMaxTemperature(jsonDailyObject.getDouble("temperatureMax"));

            dailyForecasts[i] = daily;
        }
        Log.i(TAG, "dailyForecast created successfully");
        return dailyForecasts;
    }

    private HourlyForecast[] getHourlyDetails(String jsonData) throws JSONException {
        JSONObject jsonForecast = new JSONObject(jsonData);
        JSONObject jsonHourly = jsonForecast.getJSONObject("hourly");
        JSONArray jsonHourlyData = jsonHourly.getJSONArray("data");
        HourlyForecast[] hourlyForecasts = new HourlyForecast[jsonHourlyData.length()];

        for (int i = 0; i < hourlyForecasts.length; i++){
            JSONObject jsonHourlyObject = jsonHourlyData.getJSONObject(i);

            HourlyForecast hourly = new HourlyForecast();
            hourly.setIcon(jsonHourlyObject.getString("icon"));
            hourly.setSummary(jsonHourlyObject.getString("summary"));
            hourly.setTime(jsonHourlyObject.getLong("time"));
            hourly.setTemperature(jsonHourlyObject.getDouble("temperature"));

            hourlyForecasts[i] = hourly;
        }
        Log.i(TAG, "hourlyForecast created successfully");
        return hourlyForecasts;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timeZone);

        Log.i(TAG, "current forecast created successfully");
        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(), "error_dialog");
    }

    private String buildUrl(double latitude, double longitude) {
        String url = "https://api.forecast.io/forecast";
        String apiKey = "5c150722b10107dcbd3b1c60931cacc9";
        return url + "/" + apiKey + "/" + latitude + "," + longitude;
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        startActivity(intent);
    }


}
