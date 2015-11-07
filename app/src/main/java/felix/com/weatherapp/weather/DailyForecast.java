package felix.com.weatherapp.weather;

/**
 * Created by fsoewito on 11/7/2015.
 */
public class DailyForecast {
    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private String mTImeZone;

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

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTImeZone() {
        return mTImeZone;
    }

    public void setTImeZone(String TImeZone) {
        mTImeZone = TImeZone;
    }
}
