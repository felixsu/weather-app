package felix.com.weatherapp.weather;

import felix.com.weatherapp.Constant;

/**
 * Created by fsoewito on 11/7/2015.
 */
public class HourlyForecast {
    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private String mTimeZone;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public int getTemperature(int param) {
        switch (param) {
            case Constant.CELSIUS:
                return (int) Math.round((mTemperature - 32) * 5 / 9);
            case Constant.FAHRENHEIT:
                return (int) Math.round(mTemperature);
            default:
                return (int) Math.round(mTemperature);
        }
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }
}
