package com.wllt.filecrawling;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.wllt.filecrawling.entity.User;
import com.wllt.filecrawling.util.HttpParam;
import com.wllt.filecrawling.util.HttpResult;
import com.wllt.filecrawling.util.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: file-crawling
 * @description:
 * @author: wllt
 * @create: 2020-09-15 17:54
 **/
public class Dome {

    public static void main(String[] args) {
        getDataToLocalDataBase("参数1","参数2","参数3");
    }

    /**
     * 获取网页接口数据
     * @param param1 参数1
     * @param param2 参数2
     * @param param3 参数3
     */
    private static void getDataToLocalDataBase(String param1, String param2, String param3) {
        HttpParam httpParam = new HttpParam();
        httpParam.setApiUrl("爬取的网站");
        httpParam.setApiPath("接口地址");
        Map<String, String> parms = new HashMap<>();

        parms.put("param1 ", param1);
        parms.put("param2 ", param2);
        parms.put("param3 ", param3);
        //....更多参数...
        /*     parms.put("strCustName","");*/
        //创建格式化参数
        Gson paramGson = new GsonBuilder().create();
        String requestParam = paramGson.toJson(parms);
        try {
            //post请求
            HttpResult postResult = HttpUtil.post(httpParam, requestParam);
            String result = postResult.getResult();

            int status = postResult.getStatus();
            Gson gson = new Gson();
            if (status == 200) {
                if (!StringUtils.isEmpty(result)) {
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(result);
                    JsonElement jsonElement = jsonObject.get("result");
                    String newResult = jsonElement.toString();
                    //xxData 与接口值返回相同的实体类  List<xxData>这里也可也是其他类型 按需去做
                    List<User> list = gson.fromJson(newResult, new TypeToken<List<User>>() {
                    }.getType());

//                    System.out.println("数据有：" + list.size());
                    if (list != null && list.size() > 0) {
                        //业务代码...把数据插入到本地数据库
                    } else {
//                        System.out.println("无数据");
                    }
                } else {
//                    System.out.println("错误数据" + result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 获取文件
     */
    private static void getFileData(){
        //网页文件路径   注：该接口必须返回文件流
        String url = "https://www.baidu.com/api.pdf";
        //本地路径
        String localPath = "D:/upload/test.pdf";
        File folder = new File(localPath);
        try {
            FileUtils.copyURLToFile(new URL(url), folder);
            System.out.println("获取成功！");
        } catch (IOException e) {
            System.out.println("获取失败");
            e.printStackTrace();
        }
    }
}