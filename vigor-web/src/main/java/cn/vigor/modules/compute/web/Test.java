package cn.vigor.modules.compute.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class Test
{
    public static void main(String[] args)
    {
        String s = "[{\"mtb\": 235,\"dtb\": \"111\",\"logic\": \"1\",},{\"mtb\": 1,\"dtb\": \"111\",\"logic\": \"1\",}]"; 
        JSONArray jsonArray = JSONArray.parseArray("");

        Object[] objs = jsonArray.toArray();

        for (Object object : objs) {

        JSONObject jsonObject = JSONObject.parseObject(object.toString());

        if(jsonObject.containsKey("mtb")){

            System.out.println(jsonObject.getString("mtb"));

        }

        }
    }
   
}
