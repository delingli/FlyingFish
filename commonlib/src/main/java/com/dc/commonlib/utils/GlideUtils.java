package com.dc.commonlib.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.dc.baselib.http.newhttp.RetrofitClient;
import com.dc.commonlib.R;
import com.dc.commonlib.glide.GlideCircleTransform;
import com.dc.commonlib.glide.GlideCircleWithBorderTransform;
import com.dc.commonlib.glide.GlideRoundTransform;

import java.io.File;
import java.util.List;

import okhttp3.Cookie;

public class GlideUtils {


    public static void loadLocalUrl(Context context, String absPicPath, ImageView img) {
        if (TextUtils.isEmpty(absPicPath) || img == null) {
            return;
        }
        Glide.with(context).load(Uri.fromFile(new File(absPicPath))).placeholder(R.drawable.default_hold).error(R.drawable.default_hold).into(img);
    }

    public static void loadLocalResourceUrl(Context context, int resourceId, ImageView img) {
        if (resourceId <= 0 || img == null) {
            return;
        }
        Glide.with(context).load(resourceId).transform(new GlideRoundTransform(context), new CenterCrop(context)).placeholder(R.drawable.default_hold).error(R.drawable.default_hold).into(img);
    }

    public static void loadLocalRoundPath(Context context, String path, ImageView img) {
        if (TextUtils.isEmpty(path) || img == null) {
            return;
        }
        Glide.with(context).load(path).transform(new CenterCrop(context), new GlideRoundTransform(context)).error(R.drawable.default_hold).placeholder(R.drawable.default_hold).into(img);
    }


    public static void loadCircleUrl(Context context, String url, ImageView img) {
        if (TextUtils.isEmpty(url) || img == null) {
            return;
        }
        Glide.with(context).load(url).transform(new GlideCircleTransform(context), new FitCenter(context)).placeholder(R.drawable.default_hold).error(R.drawable.default_hold).into(img);
    }

    public static void loadCircleWithBorderUrl(Context context, String url, ImageView img) {
        if (TextUtils.isEmpty(url) || img == null) {
            return;
        }
        Glide.with(context).load(url)
                .placeholder(R.drawable.default_hold)
                .transform(new GlideCircleWithBorderTransform(context, 1, context.getResources().getColor(R.color.white)), new CenterCrop(context))
                .error(R.drawable.default_hold)
                .into(img);
    }

    public static void loadRoundUrl(Context context, String url, ImageView img) {
        if (TextUtils.isEmpty(url) || img == null) {
            return;
        }
        Glide.with(context).load(url).transform(new CenterCrop(context), new GlideRoundTransform(context)).error(R.drawable.default_hold).placeholder(R.drawable.default_hold).into(img);
    }

    public static void loadUrl(Context context, String url, ImageView img) {
        if (TextUtils.isEmpty(url) || img == null) {
            return;
        }

        Glide.with(context).load(url).error(R.drawable.default_hold).placeholder(R.drawable.default_hold).into(img);
//        Glide.with(context).load(GlideUtils.url(url)).asBitmap().error(R.drawable.promise_shouye).placeholder(R.drawable.promise_shouye).into(img);
//        Glide.with(context).load(url).transform(new CenterCrop(context)).placeholder(R.drawable.promise_shouye).error(R.drawable.promise_shouye).into(img);
    }

    public static void loadFitCenterUrl(Context context, String url, ImageView img) {
        if (TextUtils.isEmpty(url) || img == null) {
            return;
        }
        Glide.with(context).load(url).asBitmap().fitCenter().error(R.drawable.default_hold).placeholder(R.drawable.default_hold).into(img);
    }
}
