package com.ypf.volleyutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ypf.volleyutil.customrequest.GsonRequest;
import com.ypf.volleyutil.customrequest.XmlRequest;
import com.ypf.volleyutil.model.Weather;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.Map;

/**
 * Created by Administrator on 2016/4/15.
 */
public class VolleyUtil {
    private static VolleyUtil volleyUtil = null;
    private RequestQueue mQueue = null;

    private VolleyUtil(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    /**
     * RequestQueue内部的设计就是非常合适高并发的，
     * 因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，
     * 这是非常浪费资源的，
     * 基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了。
     * @param context 上下文对象
     * @return
     */
    public static VolleyUtil getInstance(Context context) {
        if (volleyUtil == null) {
            volleyUtil = new VolleyUtil(context);
        }
        return volleyUtil;
    }

    /**
     * String类型GET请求
     *
     * @param url              url地址
     * @param responselistener 结果监听
     * @param errorlistener    失败监听
     */
    public void httpStringRequestGet(String url, Response.Listener<String> responselistener, Response.ErrorListener errorlistener) {
        mQueue.add(new StringRequest(url, responselistener, errorlistener));
    }


    /**
     * String类型POST请求
     *
     * @param url              url地址
     * @param map              map参数集合
     * @param responselistener 结果监听
     * @param errorlistener    失败监听
     */
    public void httpStringRequestPost(String url, final Map<String, String> map, Response.Listener<String> responselistener, Response.ErrorListener errorlistener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responselistener, errorlistener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    /**
     * Json类型GET请求
     *
     * @param url              url地址
     * @param responselistener 结果监听
     * @param errorlistener    失败监听
     */
    public void httpJsonRequestGet(String url, Response.Listener<JSONObject> responselistener, Response.ErrorListener errorlistener) {
        mQueue.add(new JsonObjectRequest(url, null, responselistener, errorlistener));
    }

    /**
     * Json类型POST请求
     *
     * @param url              url地址
     * @param jsonObject       参数数据
     * @param responselistener 结果监听
     * @param errorlistener    失败监听
     */
    public void httpJsonRequestPost(String url, JSONObject jsonObject, Response.Listener<JSONObject> responselistener, Response.ErrorListener errorlistener) {
        mQueue.add(new JsonObjectRequest(url, jsonObject, responselistener, errorlistener));
    }

    /**
     * JsonArray类型GET请求
     *
     * @param url              url地址
     * @param responselistener 结果监听
     * @param errorlistener    失败监听
     */
    public void httpJsonArrayRequestGet(String url, Response.Listener<JSONArray> responselistener, Response.ErrorListener errorlistener) {
        mQueue.add(new JsonArrayRequest(url, responselistener, errorlistener));
    }

    /**
     * 带有监听的ImageRequest请求
     *
     * @param url              url地址
     * @param responselistener 结果监听
     * @param maxwidth         图片最大宽度
     * @param maxheight        图片最大高度
     * @param config           bitmapConfig
     * @param errorlistener    失败监听
     */
    public void httpImageRequest(String url, Response.Listener<Bitmap> responselistener, int maxwidth, int maxheight, Bitmap.Config config, Response.ErrorListener errorlistener) {
        mQueue.add(new ImageRequest(url, responselistener, maxwidth, maxheight, config, errorlistener));
    }

    /**
     * 带有View的ImageRequest请求
     *
     * @param url              url地址
     * @param maxwidth         图片最大宽度
     * @param maxheight        图片最大高度
     * @param config           　　bitmapConfig
     * @param imageView        ImageView
     * @param defaultDrawbleId 默认图片ＩＤ
     */
    public void httpImageRequestwithView(String url, int maxwidth, int maxheight, Bitmap.Config config, final ImageView imageView, final int defaultDrawbleId) {
        mQueue.add(new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        }, maxwidth, maxheight, config, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imageView.setImageResource(defaultDrawbleId);
            }
        }));
    }

    /**
     * ImageLoader加载网络bitmap
     *
     * @param imageView     ImageView
     * @param default_image 默认图ID
     * @param failed_image  加载失败图ID
     * @param url           网络图片URL
     * @param bitmapCache   缓存
     * @param args          两个int型参数，图片的最大宽、高
     */
    public void httpImageLoader(ImageView imageView, int default_image, int failed_image, String url, BitmapCache bitmapCache, int... args) {
        ImageLoader imageLoader = new ImageLoader(mQueue, bitmapCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                default_image, failed_image);
        if (args != null && args.length == 2) {
            imageLoader.get(url, listener, args[0], args[1]);
        } else {
            imageLoader.get(url, listener);
        }
    }

    /**
     * NetWOrkImageView加载网络图片
     *
     * @param imageView     NetworkImageView实例
     * @param default_image 默认图ID
     * @param failed_image  加载失败图ID
     * @param url           网络图url
     * @param bitmapCache   缓存
     */
    public void httpNetWorkImageView(NetworkImageView imageView, int default_image, int failed_image, String url, BitmapCache bitmapCache) {
        imageView.setDefaultImageResId(default_image);
        imageView.setErrorImageResId(failed_image);
        imageView.setImageUrl(url, new ImageLoader(mQueue, bitmapCache));
    }

    /**
     * XML Get请求
     *
     * @param url              url地址
     * @param resonpselistener 结果监听
     * @param errorlistener    失败监听
     */
    public void httpXmlRequestGet(String url, Response.Listener<XmlPullParser> resonpselistener, Response.ErrorListener errorlistener) {
        mQueue.add(new XmlRequest(url, resonpselistener, errorlistener));
    }

    /**
     * XML Post请求
     *
     * @param url              url地址
     * @param map              参数
     * @param resonpselistener 结果监听
     * @param errorlistener    失败监听
     */
    public void httpXmlRequestPost(String url, final Map<String, String> map, Response.Listener<XmlPullParser> resonpselistener, Response.ErrorListener errorlistener) {
        XmlRequest xmlRequest = new XmlRequest(Request.Method.POST, url, resonpselistener, errorlistener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(xmlRequest);
    }

    /**
     * Gson GET请求
     *
     * @param url              URL地址
     * @param clazz            泛型(改成自己创建的实体类)
     * @param responselistener 结果监听
     * @param errorListener    错误监听
     */
    public void httpGsonRequestGet(String url, Class<Weather> clazz, Response.Listener<Weather> responselistener, Response.ErrorListener errorListener) {
        GsonRequest gsonRequest = new GsonRequest(url, clazz, responselistener, errorListener);
        mQueue.add(gsonRequest);
    }

    /**
     * Gson POST请求
     *
     * @param url              URL地址
     * @param map              参数
     * @param clazz            泛型 （改成自己的实体类）
     * @param responselistener 结果监听
     * @param errorListener    错误监听
     */
//    public void httpGsonRequestPost(String url, final Map<Object, Object> map, Class<Weather> clazz, Response.Listener<Weather> responselistener, Response.ErrorListener errorListener) {
//        GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, clazz, responselistener, errorListener) {
//            @Override
//            protected Map<Object, Object> getParams() throws AuthFailureError {
//                return map;
//            }
//        };
//        mQueue.add(gsonRequest);
//    }
}
