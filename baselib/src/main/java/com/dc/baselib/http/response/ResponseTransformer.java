package com.dc.baselib.http.response;
import com.dc.baselib.http.exception.ApiException;
import com.dc.baselib.http.exception.CustomException;
import com.dc.baselib.http.newhttp.StatusCode;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 自定义rxjava转换类，统一处理返回信息，对响应信息进行再处理，转换成我们需要的实体,
 */
public class ResponseTransformer {
    //Observable转换器处理元数据流错误处理解析封装
 /*   public static <T> ObservableTransformer<HttpResponse<T>, T> observableRansformerResult() {
        return new ObservableTransformer<HttpResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<HttpResponse<T>> upstream) {
                return upstream.onErrorResumeNext(new ObservableErrorResumeFunction<T>()).flatMap(new ObservableResponseFunction<T>());
            }
        };

    }

    *//**
     * 非服务器产生的异常，本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     *//*
    private static class ObservableErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends HttpResponse<T>>> {


        @Override
        public ObservableSource<? extends HttpResponse<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(CustomException.handlerServerException(throwable));
        }
    }

    *//**
     * 解析服务器数据，处理异常or正常情况
     *
     * @param <T>
     *//*
    private static class ObservableResponseFunction<T> implements Function<HttpResponse<T>, ObservableSource<T>> {


        @Override
        public ObservableSource<T> apply(HttpResponse<T> tHttpResponse) throws Exception {
            String code = tHttpResponse.getMsg();
            String message = tHttpResponse.getMsg();
            if (code .equals(StatusCode.SUCESSCODE)) {
                //正常数据源
                return Observable.just(tHttpResponse.getData());
            } else {
                //server错误数据
                return Observable.error(new ApiException(code, message));
            }
        }
    }

*//*    public static <T> FlowableTransformer<T, T> handlerResult() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.onErrorResumeNext(new FlowableErrorResumeFunction<T>()).flatMap(new FlowableResponseFunction<T>());
            }
        };
    }*//*


    private static class FlowableErrorResumeFunction<T> implements Function<Throwable, Publisher<? extends HttpResponse<T>>> {
        @Override
        public Publisher<? extends HttpResponse<T>> apply(Throwable throwable) throws Exception {
            return Flowable.error(CustomException.handlerServerException(throwable));
        }
    }

    private static class FlowableResponseFunction<T> implements Function<HttpResponse<T>, Publisher<T>> {
        @Override
        public Publisher<T> apply(HttpResponse<T> tHttpResponse) throws Exception {
            int code = tHttpResponse.getCode();
            String message = tHttpResponse.getMsg();
            if (code .equals(StatusCode.SUCESSCODE)) {
                //正常数据源
                return Flowable.just(tHttpResponse.getData());
            } else {
                //server错误数据
                return Flowable.error(new ApiException(code, message));
            }


*//*        @Override
        public ObservableSource<T> apply(HttpResponse<T> tHttpResponse) throws Exception {
            int code = tHttpResponse.getErrorCode();
            String message = tHttpResponse.getErrorMsg();
            if (code == 0) {
                //正常数据源
                return Observable.just(tHttpResponse.getData());
            } else {
                //server错误数据
                return Observable.error(new ApiException(code, message));
            }
        }*//*
        }
    }


    *//**
     * Observable转换器不支持背压
     *
     * @param <T>
     * @return
     *//*
    public static <T> ObservableTransformer<T, T> obtionObservableIoMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    *//**
     * Flowable转换器支持背压
     *
     * @param <T>
     * @return
     *//*
    public static <T> FlowableTransformer<T, T> obtionFlowableIoMain() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }*/

}
