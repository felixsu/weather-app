package felix.com.weatherapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import felix.com.weatherapp.Constant;
import felix.com.weatherapp.R;
import felix.com.weatherapp.weather.Forecast;
import felix.com.weatherapp.weather.HourlyForecast;

/**
 * Created by fsoewito on 11/10/2015.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {
    HourlyForecast[] mHourlyForecasts;
    Context mContext;

    public HourAdapter(Context context, HourlyForecast[] hourlyForecasts) {
        mHourlyForecasts = hourlyForecasts;
        mContext = context;
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

    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

            itemView.setOnClickListener(this);
        }

        public void bindHour(HourlyForecast hourlyForecast) {
            mTimeLabel.setText(hourlyForecast.getHour());
            mSummaryLabel.setText(hourlyForecast.getSummary());
            mTemperatureLabel.setText(String.valueOf(hourlyForecast.getTemperature(Constant.CELSIUS)));
            mIconImageView.setImageResource(Forecast.getIconId(hourlyForecast.getIcon()));
        }

        @Override
        public void onClick(View v) {

            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("at %s it will be %s degree %s", time, temperature, summary);

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

        }
    }
}
