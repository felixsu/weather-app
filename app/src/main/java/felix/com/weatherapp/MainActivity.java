package felix.com.weatherapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getSimpleName();
    double mLatitude = 0;
    double mLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url(buildUrl(mLatitude, mLongitude)).
                build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                Log.v(TAG, response.body().string());
            }
        } catch (IOException e) {
            Log.e(TAG, "IO Exception");
        }
    }

    private String buildUrl(double latitude, double longitude) {
        String url = "https://api.forecast.io/forecast";
        String apiKey = "5c150722b10107dcbd3b1c60931cacc9";
        StringBuilder result = new StringBuilder();
        return result.append(url).append("/").append(apiKey).append("/").append(latitude).append(",").append(longitude).toString();
    }

}
