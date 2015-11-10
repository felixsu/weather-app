package felix.com.weatherapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import felix.com.weatherapp.Constant;
import felix.com.weatherapp.R;
import felix.com.weatherapp.weather.Forecast;
import felix.com.weatherapp.weather.HourlyForecast;

/**
 * Created by fsoewito on 11/10/2015.
 *
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    HourlyForecast[] mHourlyForecasts;

    public HourAdapter(HourlyForecast[] hourlyForecasts){
        mHourlyForecasts = hourlyForecasts;
    }
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder viewHolder = new HourViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHourlyForecasts[position]);
    }

    @Override
    public int getItemCount() {
        return mHourlyForecasts.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder{
        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;

        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabel);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryLabel);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.temperatureLabel);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
        }

        public void bindHour(HourlyForecast hourlyForecast){
            mTimeLabel.setText(hourlyForecast.getHour());
            mSummaryLabel.setText(hourlyForecast.getSummary());
            mTemperatureLabel.setText(String.valueOf(hourlyForecast.getTemperature(Constant.CELSIUS)));
            mIconImageView.setImageResource(Forecast.getIconId(hourlyForecast.getIcon()));
        }
    }
}
