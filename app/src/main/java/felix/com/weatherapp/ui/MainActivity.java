package felix.com.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import felix.com.weatherapp.Constant;
import felix.com.weatherapp.R;
import felix.com.weatherapp.weather.Current;
import felix.com.weatherapp.weather.DailyForecast;
import felix.com.weatherapp.weather.Forecast;
import felix.com.weatherapp.weather.HourlyForecast;


public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;

    double mLatitude = -6.215117;
    double mLongitude = 106.824896;
    String mCity = "";
    String mCountry = "";

    private Forecast mForecast;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

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

        if (checkGooglePlayServices()) {
            buildGoogleApiClient();
        }

        Log.i(TAG, "old latitude " + String.valueOf(mLatitude));
        Log.i(TAG, "old longitude " + String.valueOf(mLongitude));


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
        mTimeLabel.setText(String.format(getString(R.string.mainTimeLabel), current.getFormattedTime()));
        mHumidityValue.setText(String.valueOf(current.getHumidity()));
        mPrecipValue.setText(String.format(getString(R.string.mainPrecipLabel), String.valueOf(current.getPrecipChance())));
        mSummaryLabel.setText(current.getSummary());
        Drawable drawable = getDrawable(Forecast.getIconId(current.getIcon()));
        mIconImageView.setImageDrawable(drawable);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
            mCountry = addresses.get(0).getCountryCode();
            mCity = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLocationLabel.setText(String.format("%s, %s", mCity, mCountry));

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

        String timeZone = jsonForecast.getString("timezone");
        DailyForecast[] dailyForecasts = new DailyForecast[jsonDailyData.length()];
        for (int i = 0; i < dailyForecasts.length; i++) {
            JSONObject jsonDailyObject = jsonDailyData.getJSONObject(i);

            DailyForecast daily = new DailyForecast();
            daily.setIcon(jsonDailyObject.getString("icon"));
            daily.setSummary(jsonDailyObject.getString("summary"));
            daily.setTime(jsonDailyObject.getLong("time"));
            daily.setMinTemperature(jsonDailyObject.getDouble("temperatureMin"));
            daily.setMaxTemperature(jsonDailyObject.getDouble("temperatureMax"));
            daily.setTimeZone(timeZone);
            dailyForecasts[i] = daily;
        }
        Log.i(TAG, "dailyForecast created successfully");
        return dailyForecasts;
    }

    private HourlyForecast[] getHourlyDetails(String jsonData) throws JSONException {
        JSONObject jsonForecast = new JSONObject(jsonData);
        JSONObject jsonHourly = jsonForecast.getJSONObject("hourly");
        JSONArray jsonHourlyData = jsonHourly.getJSONArray("data");

        String timeZone = jsonForecast.getString("timezone");
        HourlyForecast[] hourlyForecasts = new HourlyForecast[jsonHourlyData.length()];

        for (int i = 0; i < hourlyForecasts.length; i++) {
            JSONObject jsonHourlyObject = jsonHourlyData.getJSONObject(i);

            HourlyForecast hourly = new HourlyForecast();
            hourly.setIcon(jsonHourlyObject.getString("icon"));
            hourly.setSummary(jsonHourlyObject.getString("summary"));
            hourly.setTime(jsonHourlyObject.getLong("time"));
            hourly.setTemperature(jsonHourlyObject.getDouble("temperature"));
            hourly.setTimeZone(timeZone);

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
    public void startDailyActivity(View view) {
        if (mForecast != null) {
            Intent intent = new Intent(this, DailyForecastActivity.class);
            intent.putExtra(getString(R.string.key_daily), mForecast.getDailyForecast());
            startActivity(intent);
        }
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        if (mForecast != null) {
            Intent intent = new Intent(this, HourlyForecastActivity.class);
            intent.putExtra(getString(R.string.key_hourly), mForecast.getHourlyForecast());
            startActivity(intent);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
            mLongitude = mLocation.getLongitude();
            Log.i(TAG, "new latitude " + String.valueOf(mLatitude));
            Log.i(TAG, "new longitude " + String.valueOf(mLongitude));
            getForecast(mLatitude, mLongitude);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {

            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
        /*
        * Google Play Services is missing or update is required
		*  return code could be
		* SUCCESS,
		* SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
		* SERVICE_DISABLED, SERVICE_INVALID.
		*/
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return false;
        }

        return true;
    }
}
