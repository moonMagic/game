package com.charles.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.charles.entity.BaseRecyclable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * fastJson 中的JSONObject对象不能级联调用
 * 这里将它封装一层
 *
 * @author CharlesLee
 */
@Component
public class Json extends BaseRecyclable {

    private final JSONObject jsonObject = new JSONObject();


    public Json put(String key, String value) {
        jsonObject.put(key, value);
        return this;
    }

    public Json put(List<?> list) {
        for (int i = 1; i <= list.size(); i++) {
            jsonObject.put(i + "", list.get(i - 1));
        }
        return this;
    }

    public Json put(Map<String, Object> map) {
        for (String key : map.keySet()) {
            jsonObject.put(key, map.get(key));
        }
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(jsonObject);
    }

    @Override
    protected void recycle() {
        jsonObject.clear();
    }
}
