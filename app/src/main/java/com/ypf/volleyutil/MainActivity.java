package com.ypf.volleyutil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.ypf.volleyutil.model.Weather;
import com.ypf.volleyutil.model.WeatherInfo;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    TextView textView;
    ImageView imageView;
    NetworkImageView networkImageView;
    VolleyUtil volleyutil;
    String url1 = "http://www.baidu.com";
    String url2 = "http://m.weather.com.cn/data/101010100.html";
    String url3 = "http://img.my.csdn.net/uploads/201407/17/1405567749_8669.jpg";
    String url4 = "http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg";
    String url5 = "http://img.my.csdn.net/uploads/201407/17/1405567749_8669.jpg";
    String url6 = "http://avatar.csdn.net/8/B/B/1_sinyu890807.jpg";
    String url7 = "http://flash.weather.com.cn/wmaps/xml/china.xml";
    String url8 = "http://www.weather.com.cn/data/sk/101010100.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volleyutil = VolleyUtil.getInstance(this);
        setContentView(R.layout.activity_main);
        initViews();

    }

    private void initViews() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        networkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
        volleyutil.httpNetWorkImageView(networkImageView, R.mipmap.ic_launcher, R.mipmap.fresco_logo, url6, new BitmapCache());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                volleyutil.httpStringRequestGet(url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        textView.setText(s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textView.setText(volleyError.getMessage());
                    }
                });
                break;
            case R.id.btn2:
                break;
            case R.id.btn3:
                volleyutil.httpJsonRequestGet(url8, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        textView.setText(jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textView.setText(volleyError.getMessage());
                    }
                });
                break;
            case R.id.btn4:
                break;
            case R.id.btn5:
                volleyutil.httpImageRequest(url3, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textView.setText(volleyError.getMessage());
                    }
                });
                break;
            case R.id.btn6:
                volleyutil.httpImageRequestwithView(url4, 0, 0, Bitmap.Config.RGB_565, imageView, R.mipmap.fresco_logo);
                break;
            case R.id.btn7:
                volleyutil.httpImageLoader(imageView, R.mipmap.ic_launcher, R.mipmap.fresco_logo, url5, new BitmapCache());
                break;
            case R.id.btn8:
                volleyutil.httpXmlRequestGet(url7, new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        try {
                            int eventType = response.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        String nodeName = response.getName();
                                        if ("city".equals(nodeName)) {
                                            String pName = response.getAttributeValue(0);
                                            Log.d("TAG", "cityname = " + pName);
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textView.setText(volleyError.getMessage());
                    }
                });
                break;
            case R.id.btn9:
                volleyutil.httpGsonRequestGet(url8, Weather.class, new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        WeatherInfo weatherInfo = weather.getWeatherinfo();
                        Log.d("TAG", "city is " + weatherInfo.getCity());
                        Log.d("TAG", "temp is " + weatherInfo.getTemp());
                        Log.d("TAG", "time is " + weatherInfo.getTime());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        textView.setText(volleyError.getMessage());
                    }
                });
                break;
        }
    }
}
