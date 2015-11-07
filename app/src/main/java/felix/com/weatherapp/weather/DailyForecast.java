package felix.com.weatherapp.weather;

import felix.com.weatherapp.Constant;

/**
 * Created by fsoewito on 11/7/2015.
 */
public class DailyForecast {
    private long mTime;
    private String mSummary;
    private double mMinTemperature;
    private double mMaxTemperature;
    private String mIcon;

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

    public double getMinTemperature() {
        return mMinTemperature;
    }

    public int getMinTemperature(int param) {
        switch (param) {
            case Constant.CELSIUS:
                return (int)Math.round((mMinTemperature - 32) * 5 / 9);
            case Constant.FAHRENHEIT:
                return (int)Math.round(mMinTemperature);
            default:
                return (int)Math.round(mMinTemperature);
        }
    }


    public void setMinTemperature(double minTemperature) {
        mMinTemperature = minTemperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public double getMaxTemperature() {
        return mMaxTemperature;
    }

    public int getMaxTemperature(int param){
        switch (param) {
            case Constant.CELSIUS:
                return (int)Math.round((mMaxTemperature - 32) * 5 / 9);
            case Constant.FAHRENHEIT:
                return (int)Math.round(mMaxTemperature);
            default:
                return (int)Math.round(mMaxTemperature);
        }
    }

    public void setMaxTemperature(double maxTemperature) {
        mMaxTemperature = maxTemperature;
    }
}
