package felix.com.weatherapp.weather;

import felix.com.weatherapp.R;

/**
 * Created by fsoewito on 11/7/2015.
 */
public class Forecast {
    private Current mCurrent;
    private HourlyForecast[] mHourlyForecast;
    private DailyForecast[] mDailyForecast;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public HourlyForecast[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(HourlyForecast[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public DailyForecast[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(DailyForecast[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int getIconId(String iconString){
        int iconId = R.drawable.clear_day;
        switch (iconString) {
            case "clear_day":
                iconId = R.drawable.clear_day;
                break;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-clody-night":
                iconId = R.drawable.cloudy_night;
                break;
            default:
                break;
        }
        return iconId;
    }
}
