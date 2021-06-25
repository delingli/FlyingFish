package com.itc.screen_saver.rabbit;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.dc.baselib.BaseApplication;
import com.dc.baselib.constant.Constants;
import com.itc.screen_saver.utils.DeviceIdUtil;
import com.itc.screen_saver.utils.NetUtils;
import com.itc.screen_saver.utils.RootCheck;
import com.itc.screen_saver.utils.SystemUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitDispatcher {

    private static String QUEUE_NAMES = "ReportQRM";
    private static String QUEUE_EXCHANGE = "CERM";
    private static String tag = "RabbitDispatcher";
    private static String FACE_EXCHANGE = "FACE_EXC";
    private static String FACEP_QUEUE = "face.*";


    private Channel channel;
    private Map<String, Object> map;
    private Channel report;
    private Connection connection;
    private int deviceStroge;
    private ConnectionFactory factory;
    private long lastMsgId = -1;
    private RabbitEventListener eventListener;
    private String consumerTag;
    private boolean underDestroy = false;

    public interface RabbitEventListener {
        void onMessageReceived(String message);

        void onShutdownSignaled(String consumerTag, String sig);
    }

    public RabbitDispatcher(RabbitEventListener listener) {
        LogUtils.dTag(tag, "RabbitDispatcher Construction ongoing...");
        eventListener = listener;
        deviceStroge = getDeviceStroge();
        prepareRabbit();
        LogUtils.dTag(tag, "RabbitDispatcher Construction done with eventListener: " + eventListener);
    }

    private void prepareRabbit() {
        initRabbitDispatcher();
        LogUtils.dTag(tag, "程序开始注册了1");
    }


    public void initRabbitDispatcher() {
        map = new HashMap<>();
        map.put("device_sn", DeviceIdUtil.getDeviceId(BaseApplication.Companion.getInstances()));
        LogUtils.dTag(tag, DeviceIdUtil.getDeviceId(BaseApplication.Companion.getInstances()));
        map.put("device_ip", Objects.requireNonNull(NetUtils.getLocalIpAddress()));
        map.put("device_model", SystemUtil.getSystemModel());//设备型号
        map.put("device_version", SystemUtil.getSystemVersion());//设备系统版本号
        map.put("app_version", "1.0");//app版本号
        map.put("root", RootCheck.isRoot() ? 1 : 0);//
        map.put("memory", (int) (SystemUtil.getTotalMemory() / 1024));//运行总内存
        map.put("storage", deviceStroge);//存储总内存
        map.put("resolution", ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight());//存储总内存
        initConnectFactor();
    }

    private void initConnectFactor() {
        factory = new ConnectionFactory();
        factory.setHost(Constants.SERVER_HOST);
        factory.setUsername(Constants.RABBIT_NAME);
        factory.setPassword(Constants.RABBIT_PASSWORD);
        factory.setPort(Constants.RABBIT_PORT);
        factory.setClientProperties(map);
        LogUtils.dTag(tag, "uid=" + map.get("uid"));
        factory.setConnectionTimeout(10000);
        factory.setRequestedHeartbeat(10);//是否断网
        factory.setAutomaticRecoveryEnabled(false);
        //   factory.setNetworkRecoveryInterval(10);// 设置 10s ，重试一次
        factory.setTopologyRecoveryEnabled(false);// 设置不重新声明交换器，队列等信息。
        factory.setSharedExecutor(new ThreadPoolExecutor(
                3, 5,
                Integer.MAX_VALUE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        ));
    }

    public class RabbitConsumer extends DefaultConsumer {
        public RabbitConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag,
                                   Envelope envelope,
                                   AMQP.BasicProperties properties,
                                   byte[] body)
                throws IOException {
            String routingKey = envelope.getRoutingKey();
            String contentType = properties.getContentType();
            LogUtils.dTag(tag, "handle message with routingKey:" + routingKey + " contentType: " + contentType);
            long deliveryTag = envelope.getDeliveryTag();
            /*TODO: process the message components here ...*/
            String message = new String(body);
            LogUtils.dTag(tag, "message:" + message + " dtag: " + deliveryTag);
            if (lastMsgId != deliveryTag) {
                lastMsgId = deliveryTag;
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    int message_type = jsonObject.optInt("message_type", -1);
                    if (message_type == 882) {
                        LogUtils.dTag(tag, "设备注册回调...");
                    } else if (message_type == 997) {//刷新屏保数据
                        LogUtils.dTag(tag, "通知刷新屏保数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.dTag(tag, "message:" + message);
            }
            channel.basicAck(deliveryTag, false);
//            if(eventListener != null && !underDestroy) {
            if (eventListener != null && underDestroy == false) {
                eventListener.onMessageReceived(message);
            }
        }

        @Override
        public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
            // the work to do here
            LogUtils.dTag(tag, "ShutdownSignal received with consumerTag: " + consumerTag + " sig: " + sig.toString());
            try {
                report.close();
                channel.basicCancel(consumerTag);
                channel.close();
                connection.close();
            } catch (IOException e) {
                LogUtils.dTag(tag, "IOException emitted!");
                e.printStackTrace();
            } catch (TimeoutException e) {
                LogUtils.dTag(tag, "TimeoutException emitted!");
                e.printStackTrace();
            } finally {
                report = null;
                channel = null;
                connection = null;
                if (eventListener != null && underDestroy == false) {
                    LogUtils.dTag(tag, "trigger callback listener: " + eventListener);
                    eventListener.onShutdownSignaled(consumerTag, sig.toString());
                }
            }
        }
    }

    public boolean reCreateRabbitConnection() {

        boolean ret = CreateRabbitConnection();
        if (ret) {
            LogUtils.dTag(tag, "Create Rabbit Connection successful.");
        } else {
            LogUtils.dTag(tag, "Create Rabbit Connection failed.");
        }
        return ret;
    }

    private boolean CreateRabbitConnection() {
        LogUtils.dTag(tag, "RabbitDispatcher try connection & channel creation.");
        try {
            if (factory == null) {
                initConnectFactor();
            }
            if (factory != null) {
                connection = factory.newConnection();
                channel = connection.createChannel();
                //设备上报的channel
                report = connection.createChannel();
                // 声明队列
                channel.queueDeclare(DeviceIdUtil.getDeviceId(BaseApplication.Companion.getInstances()), true, false, false, null);
                consumerTag = channel.basicConsume(DeviceIdUtil.getDeviceId(BaseApplication.Companion.getInstances()), false, new RabbitConsumer(channel));
            }
        } catch (IOException | TimeoutException e) {
            LogUtils.dTag(tag, "IOException emitted!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void publishReport(String deviceInfo) {
        if (report != null) {
            try {
                report.basicPublish(QUEUE_EXCHANGE, QUEUE_NAMES, null, deviceInfo.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean publishFaceReport(String deviceInfo) {
        if (report != null) {
            try {
                report.basicPublish(FACE_EXCHANGE, FACEP_QUEUE, null, deviceInfo.getBytes());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void cleanConnectionResource(String consumerTag) {
        new Thread(() -> {
            try {
                if (report != null) report.close();
                if (consumerTag != null && channel != null) {
                    channel.basicCancel(consumerTag);
                }
                if (channel != null) channel.close();
                if (connection != null) connection.close();
                LogUtils.dTag(tag, "Clean Rabbit's connection resource successfully!");
            } catch (IOException e) {
                LogUtils.dTag(tag, "Clean Rabbit's connection resource, IOException emitted!");
                e.printStackTrace();
            } catch (Exception e) {
                LogUtils.dTag(tag, "Clean Rabbit's connection resource, TimeoutException emitted!");
                e.printStackTrace();
            } finally {
                channel = null;
                report = null;
                connection = null;
            }
        }).start();
    }

    public void destroyDispatcher() {
        underDestroy = true;
        LogUtils.dTag(tag, "DestroyDispatcher called!");
        cleanConnectionResource(consumerTag);
        factory = null;

    }

    private int getDeviceStroge() {
        int totalStroge = 0;
        try {
            String s1 = SystemUtil.showROMTotal();
            String s2 = SystemUtil.showROMAvail();
            String substring = s1.substring(s1.length() - 2, s1.length());
            String substring2 = s2.substring(s2.length() - 2, s2.length());

            if (substring.contains("MB")) {
                String[] mbs = s1.split("MB");
                String mb = mbs[0];
                String[] split = mb.split(",");
                totalStroge = Integer.parseInt(split[0] + split[1]);
            }
        } catch (Exception e) {
            LogUtils.iTag("RabbitServer", e.toString());

        }
        return totalStroge;
    }
}
