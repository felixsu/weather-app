package felix.com.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import felix.com.weatherapp.Constant;
import felix.com.weatherapp.R;
import felix.com.weatherapp.weather.DailyForecast;
import felix.com.weatherapp.weather.Forecast;

/**
 * Created by fsoewito on 11/8/2015.
 *
 */
public class DayAdapter extends BaseAdapter {
    private Context mContext;
    private DailyForecast[] mDailyForecasts;

    public DayAdapter(Context context, DailyForecast[] dailyForecasts){
        mContext = context;
        mDailyForecasts = dailyForecasts;
    }

    @Override
    public int getCount() {
        return mDailyForecasts.length;
    }

    @Override
    public Object getItem(int position) {
        return mDailyForecasts[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            //create everything
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.setIconImageView((ImageView) convertView.findViewById(R.id.iconImageView));
            holder.setTemperatureLabel((TextView) convertView.findViewById(R.id.temperatureLabel));
            holder.setDayLabel((TextView) convertView.findViewById(R.id.dayNameLabel));

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        DailyForecast dailyForecast = mDailyForecasts[position];
        holder.getIconImageView().setImageResource(Forecast.getIconId(dailyForecast.getIcon()));
        holder.getTemperatureLabel().setText(String.valueOf(dailyForecast.getMaxTemperature(Constant.CELSIUS)));
        holder.getDayLabel().setText(dailyForecast.getDayOfTheWeek());

        return convertView;
    }

    private static class ViewHolder{
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;

        public ImageView getIconImageView() {
            return iconImageView;
        }

        public void setIconImageView(ImageView iconImageView) {
            this.iconImageView = iconImageView;
        }

        public TextView getTemperatureLabel() {
            return temperatureLabel;
        }

        public void setTemperatureLabel(TextView temperatureLabel) {
            this.temperatureLabel = temperatureLabel;
        }

        public TextView getDayLabel() {
            return dayLabel;
        }

        public void setDayLabel(TextView dayLabel) {
            this.dayLabel = dayLabel;
        }
    }
}
