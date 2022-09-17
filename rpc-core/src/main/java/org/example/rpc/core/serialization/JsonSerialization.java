package org.example.rpc.core.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Json序列化方式
 *
 * @author guolonghang
 * @Date 2022年09月17日10:58:04
 */
public class JsonSerialization implements RpcSerialization {

    private static final ObjectMapper MAPPER;

    static {
        //获取待反序列化的字段
        MAPPER = generateMapper(JsonInclude.Include.ALWAYS);
    }

    //定制一个ObjectMapper用于解析Json
    private static ObjectMapper generateMapper(JsonInclude.Include include) {
        ObjectMapper customMapper = new ObjectMapper();

        customMapper.setSerializationInclusion(include);

        //遇到未知属性忽略
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //枚举类型允许传数字
        customMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        //格式化日期
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return customMapper;
    }

    /**
     * 将传入的对象json化
     *
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return obj instanceof String ? ((String) obj).getBytes() : MAPPER.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 根据传入的实体类，进行反序列化，一一对应
     *
     * @param data
     * @param clz
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        return MAPPER.readValue(Arrays.toString(data), clz);
    }
}
