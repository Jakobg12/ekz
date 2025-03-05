package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {
    private List<HourlyWeather> hourlyWeatherList;
    public HourlyWeatherAdapter(List<HourlyWeather> hourlyWeatherList) {
        this.hourlyWeatherList = hourlyWeatherList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_itm, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyWeather hourlyWeather = hourlyWeatherList.get(position);
        holder.timeTextView.setText(hourlyWeather.getTime());
        holder.temperatureTextView.setText(hourlyWeather.getTemperature());

        switch (hourlyWeather.getTemperature()) {
            case "cloudy":
                holder.weatherImageView.setImageResource(R.drawable.cloudy);
                break;
            case "sunny":
                holder.weatherImageView.setImageResource(R.drawable.sun);
                break;
            case "rainy":
                holder.weatherImageView.setImageResource(R.drawable.rain);
                break;
        }
    }
    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTextView;
        public TextView temperatureTextView;
        public ImageView weatherImageView;
        public ViewHolder(View view) {
            super(view);
            timeTextView = view.findViewById(R.id.hours);
            temperatureTextView = view.findViewById(R.id.gradus);
            weatherImageView = view.findViewById(R.id.weather_image);
        }
    }
}
