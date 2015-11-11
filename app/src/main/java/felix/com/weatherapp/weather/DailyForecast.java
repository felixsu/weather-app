package felix.com.weatherapp.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import felix.com.weatherapp.Constant;

/**
 * Created by fsoewito on 11/7/2015.
 */
public class DailyForecast implements Parcelable{
    private long mTime;
    private String mSummary;
    private double mMinTemperature;
    private double mMaxTemperature;
    private String mIcon;


    public DailyForecast() {
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    private String mTimeZone;

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

    public String getDayOfTheWeek() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        return formatter.format(new Date(getTime()*1000));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getTime());
        dest.writeString(getSummary());
        dest.writeDouble(getMinTemperature());
        dest.writeDouble(getMaxTemperature());
        dest.writeString(getIcon());
        dest.writeString(getTimeZone());
    }

    private DailyForecast(Parcel parcel){
        mTime = parcel.readLong();
        mSummary = parcel.readString();
        mMinTemperature = parcel.readDouble();
        mMaxTemperature = parcel.readDouble();
        mIcon = parcel.readString();
        mTimeZone = parcel.readString();
    }

    public static final Creator<DailyForecast> CREATOR = new Creator<DailyForecast>() {
        @Override
        public DailyForecast createFromParcel(Parcel source) {
            return new DailyForecast(source);
        }

        @Override
        public DailyForecast[] newArray(int size) {
            return new DailyForecast[size];
        }
    };
}
