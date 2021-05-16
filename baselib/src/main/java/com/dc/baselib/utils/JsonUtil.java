package com.dc.baselib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;


public class JsonUtil {
    private final static int ALLFILDES_MODE = 0;
    private final static int EXPOSE_MODE = 1;

    /*
     *	用法
     *   Node nodeExpose = (Node) JsonUtil.fromJsonExpose(jsonStr, new TypeToken<Node>() {}.getType());
     *   String jsonStrExpose = JsonUtil.toJsonExpose(nodeExpose);
     *   
     *   Node node       = (Node) JsonUtil.fromJson(tmpStr, new TypeToken<Node>() {}.getType());
	 *   String tmpStr   = JsonUtil.toJson(node);
     *   
     *  仅处理注解的属性时，属性用下列注解  
     *  @Expose
     *  @SerializedName("newName")
	 *
     */
    //将对象所有属性转为json串
    public static <T> String toJson(T t) {
        return toJsonBase(t, ALLFILDES_MODE);
    }
    public static <T> T fromJson(String vJson, Type vType) {
        return fromJsonBase(vJson, vType, ALLFILDES_MODE);
    }

    //将对象@Expost注解的属性转为json串
    public static <T> String toJsonExpose(T t) {
        return toJsonBase(t, EXPOSE_MODE);
    };
    public static <T> T fromJsonExpose(String vJson, Type vType) {
        return fromJsonBase(vJson, vType, EXPOSE_MODE);
    };

    /*************************************************************************
     * 内部私有方法
     * ***********************************************************************/
    private static Gson getGsonByMode(int vMode) {
        Gson gson = null;
        switch (vMode) {
            case EXPOSE_MODE:
                gson  = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
                            //.enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
                            //.serializeNulls() //是否序列化null的属性，默认不序列化
                            //.setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
                            //.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
                            //.setPrettyPrinting() //对json结果格式化.
                            //.setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                            //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                            //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
                        .create();
                break;
            case ALLFILDES_MODE:
            default:
                gson = new Gson();
                break;
        };
        return gson;
    };
    private  static <T> T fromJsonBase(String vJson, Type vType, int vMode) {
        Gson gson = getGsonByMode(vMode);
        T tmpObject;
        try {
            tmpObject = gson.fromJson(vJson, vType);
            return tmpObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };

    private static <T> String toJsonBase(T t, int vMode) {
        Gson gson = getGsonByMode(vMode);
        try {
            String jsonStr = gson.toJson(t);
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };


}
