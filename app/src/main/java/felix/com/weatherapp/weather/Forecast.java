package felix.com.weatherapp.weather;

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
}
