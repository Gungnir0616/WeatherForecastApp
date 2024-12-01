package com.lhanman.lhanweather.fragment;

import android.content.SharedPreferences;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.base.LazyFragment;
import com.lhanman.lhanweather.event.ReceiveEvent;
import com.lhanman.lhanweather.gson.Forecast;
import com.lhanman.lhanweather.gson.Weather;
import com.lhanman.lhanweather.util.HttpUtil;
import com.lhanman.lhanweather.util.Utility;

import java.io.IOException;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class WeatherFragment1 extends LazyFragment {
    @BindView(R.id.weather_layout)
  ScrollView weatherLayout;
    @BindView(R.id.title_city)
   TextView titleCity;
    @BindView(R.id.title_update_time)
    TextView titleUpdateTime;
    @BindView(R.id.degree_txt)
    TextView degreeText;
    @BindView(R.id.weather_info_txt)
     TextView weatherInfoText;
    @BindView(R.id.forecast_layout)
 LinearLayout forecastLayout;
    @BindView(R.id.api_text)
     TextView aqiText;
    @BindView(R.id.pm25_text)
    TextView pm25Text;
    @BindView(R.id.comfort_text)
     TextView comfortText;
    @BindView(R.id.car_wash_text)
    TextView carWashText;
    @BindView(R.id.sport_text)
   TextView sportText;
    @BindView(R.id.bing_pic_img)
   ImageView bingPicImg;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
     String mWeatherId;
    @BindView(R.id.drawer_layout)
 DrawerLayout drawerLayout;
    @BindView(R.id.nav_button)
   Button navButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather;
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void receive(ReceiveEvent event){
        if (event != null){
            drawerLayout.closeDrawers();
            swipeRefresh.setRefreshing(true);
            requestWeather(event.weatherId);
            setmWeatherId(event.weatherId);
        }
    }
    @Override
    protected void loadData() {
        EventBus.getDefault().register(this);

        SharedPreferences prefs = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        String weatherString = prefs.getString("weather",null);
        if(weatherString != null)
        {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherid;
            showWeatherInfo(weather);
        }
        else
        {
            //无缓存时去服务器查询天气
//           String weatherId = getIntent().getStringExtra("weather_id");
            mWeatherId = getActivity().getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
//            requestWeather(weatherId);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

//        String bingPic = prefs.getString("bing_pic",null);
//        if(bingPic != null)
//        {
//            Glide.with(this).load(bingPic).into(bingPicImg);
//        }
//        else
//        {
        loadBingPic();
//        }

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    //根据天气id请求城市天气信息
    public void requestWeather(final String weatherId)
    {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +"&key=" +
                "f7622651fd39461cad24730270d638f2";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
               getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status))
                        {
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",
                                    MODE_PRIVATE).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "获取天气信息失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    //处理并展示Weather实体类的数据
    private void showWeatherInfo(Weather weather)
    {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime;
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList)
        {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item,forecastLayout
                    ,false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        if(weather.aqi != null)
        {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度: " + weather.suggestion.comfort.info;
        String carWash = "洗车指数: " + weather.suggestion.carwash.info;
        String sport = "运动建议： " + weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    //加载必应每日一图
    private void loadBingPic()
    {
        String requestBingPic = "https://api.ixiaowai.cn/api/api.php";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final byte[] bingPic = response.body().bytes();
//                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE)
//                        .edit();
//                editor.putString("bing_pic",bingPic);
//                editor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public void setmWeatherId(String mWeatherId) {
        this.mWeatherId = mWeatherId;
    }
}