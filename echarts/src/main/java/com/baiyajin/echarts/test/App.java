package com.baiyajin.echarts.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;


import com.alibaba.fastjson.JSON;

import com.baiyajin.echarts.util.EchartsUtil;
import com.baiyajin.echarts.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.apache.http.client.ClientProtocolException;
import sun.misc.BASE64Decoder;

public class App {
    public static void main(String[] args) throws ClientProtocolException, IOException, TemplateException {
        // 变量
//        String[] categories = new String[] { "苹果", "香蕉", "西瓜" };
//        int[] values = new int[] { 3, 2, 1 };

        // 模板参数
        HashMap<String, Object> datas = new HashMap<>();
//        datas.put("categories", JSON.toJSONString(categories));
//        datas.put("values", JSON.toJSONString(values));

        // 生成option字符串
        String option = FreemarkerUtil.generateString("zxt.ftl", "echarts", datas);

        // 根据option参数
        String base64 = EchartsUtil.generateEchartsBase64(option);

        System.out.println("BASE64:" + base64);


        //在填写文件路径时，一定要写上具体的文件名称（xx.txt），否则会出现拒绝访问。
        String path = "D:/echarts/t3.png";
        File file = new File(path);
        if(!file.exists()){
            //先得到文件的上级目录，并创建上级目录，在创建文件
            file.getParentFile().mkdir();
            try {
                //创建文件
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        generateImage(base64, path);
    }

    public static void generateImage(String base64, String path) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        try (OutputStream out = new FileOutputStream(path)){
            // 解密
            byte[] b = decoder.decodeBuffer(base64);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
        }
    }
}
