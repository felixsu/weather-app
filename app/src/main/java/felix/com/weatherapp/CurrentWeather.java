package felix.com.weatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by fsoewito on 11/6/2015.
 */
public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    public String getIcon() {
        return mIcon;
    }

    public int getIconId(){
        int iconId = R.drawable.clear_day;
        switch (getIcon()){
            case "clear_day" :
                iconId = R.drawable.clear_day;
                break;
            case "rain" :
                iconId = R.drawable.rain;
                break;
            case "snow" :
                iconId = R.drawable.snow;
                break;
            case "sleet" :
                iconId = R.drawable.sleet;
                break;
            case "wind" :
                iconId = R.drawable.wind;
                break;
            case "fog" :
                iconId = R.drawable.fog;
                break;
            case "cloudy" :
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day" :
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-clody-night" :
                iconId = R.drawable.cloudy_night;
                break;
            default:
                break;
        }
        return iconId;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        String timeString = formatter.format(new Date(getTime()*1000));

        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getPrecipChance() {
        return mPrecipChance;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }
}