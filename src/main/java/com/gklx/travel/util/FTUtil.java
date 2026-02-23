package com.gklx.travel.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
public class FTUtil {

    @Autowired
    private Configuration freemarkerConfig;

    /**
     * 根据模板生成字符串
     *
     * @param templateName 模板文件名（如 demo.ftl）
     * @param dataModel    模板所需的参数数据
     * @return 生成的字符串
     * @throws Exception 异常（实际项目建议捕获细分）
     */
    public String get(String templateName, Map<String, Object> dataModel) throws Exception {
        Template template = freemarkerConfig.getTemplate(templateName);
        Writer writer = new StringWriter();
        try {
            template.process(dataModel, writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new Exception("FreeMarker 模板渲染失败", e);
        } finally {
            writer.close();
        }
    }

    // 测试示例
    public static void main(String[] args) throws Exception {
        FTUtil util = new FTUtil();
        Configuration config = new Configuration(Configuration.VERSION_2_3_32);
        config.setClassForTemplateLoading(FTUtil.class, "/templates");
        config.setDefaultEncoding("UTF-8");
        util.freemarkerConfig = config;

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "FreeMarker 测试");
        Map<String, Object> user = new HashMap<>();
        user.put("name", "张三");
        user.put("age", 25);
        user.put("hobbies", new String[]{"看书", "跑步", "编程"});
        dataModel.put("user", user);

        // 生成字符串
        String result = util.get("demo.ftl", dataModel);
        // 打印结果
        System.out.println(result);
    }
}