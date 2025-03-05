package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetWeather getWeather = new GetWeather();
        getWeather.execute();
    }
    class GetWeather extends AsyncTask<Void, Void, Void>
    {
        String body;
        @Override
        protected Void doInBackground(Void... params)
        {
            Document d_doc = null;
            try {
                d_doc = Jsoup.connect("https://api.weather.yandex.ru/v2/forecast?lat=52.37125&lon=4.89388")
                        .header("X-Yandex-API-Key", "9223b2cf-2f9e-4162-8cc9-0f4726b69fc5")
                        .ignoreContentType(true).get();
                body = d_doc.text();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (body != null) {
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONArray forecasts = jsonObject.getJSONArray("forecasts");
                    for (ArrayList<HourlyWeather> weatherList : weather.hourlyWeatherData) {
                        weatherList.clear();
                    }
                    for (int i = 0; i < forecasts.length(); i++) {
                        JSONObject dailyForecast = forecasts.getJSONObject(i);
                        JSONArray hours = dailyForecast.getJSONArray("hours");
                        int sumTemp = 0;
                        int count = 0;

                        for (int j = 0; j < hours.length(); j++) {
                            JSONObject hour = hours.getJSONObject(j);
                            int temp = hour.getInt("temp");
                            sumTemp += temp;
                            count++;

                            String time = hour.getString("hour") + ":00";
                            String condition = hour.getString("condition");
                            weather.hourlyWeatherData.get(i).add(new HourlyWeather(time, temp + "°C"));
                        }

                        if (i == 1) {
                            weather.mondayAvgTemp = (sumTemp / count) + "°C";
                        } else if (i == 2) {
                            weather.tuesdayAvgTemp = (sumTemp / count) + "°C";
                        }
                    }
                    addweather();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(body);
                TextView textView = findViewById(R.id.textView);
                TextView textView2 = findViewById(R.id.pog);

                String tempValue = jsonObject.getJSONObject("fact").getString("temp") + "°C";
                String condition = jsonObject.getJSONObject("fact").getString("condition");

                textView.setText(tempValue);
                textView2.setText(condition);

                TextView mondayAvgTextView = findViewById(R.id.monday_avg_temp);
                TextView tuesdayAvgTextView = findViewById(R.id.tuesday_avg_temp);

                mondayAvgTextView.setText(weather.mondayAvgTemp);
                tuesdayAvgTextView.setText(weather.tuesdayAvgTemp);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setWeather(View view)
    {
        setContentView(R.layout.weatherhous);
        addspiner();
        addHourlyWeather(0);
    }
    public void addweather()
    {
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.pog);
        textView.setText(weather.temp);
        textView2.setText(weather.could);


        TextView mondayAvgTextView = findViewById(R.id.monday_avg_temp);
        TextView tuesdayAvgTextView = findViewById(R.id.tuesday_avg_temp);

        mondayAvgTextView.setText(weather.mondayAvgTemp);
        tuesdayAvgTextView.setText(weather.tuesdayAvgTemp);
    }
    public void addspiner() {
        String[] add = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, add);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addHourlyWeather(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Spinner", "No item selected");
            }
        });
    }

    public void addHourlyWeather(int dayIndex) {

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        HourlyWeatherAdapter adapter = new HourlyWeatherAdapter(weather.hourlyWeatherData.get(dayIndex));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void Back(View view)
    {
        setContentView(R.layout.activity_main);
        GetWeather getWeather = new GetWeather();
        getWeather.execute();
    }

}