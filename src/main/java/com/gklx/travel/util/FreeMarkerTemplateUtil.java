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
public class FreeMarkerTemplateUtil {

    @Autowired
    private Configuration freemarkerConfig;

    /**
     * 根据模板生成字符串
     * @param templateName 模板文件名（如 demo.ftl）
     * @param dataModel 模板所需的参数数据
     * @return 生成的字符串
     * @throws Exception 异常（实际项目建议捕获细分）
     */
    public String generateStringFromTemplate(String templateName, Map<String, Object> dataModel) throws Exception {
        // 1. 获取指定模板
        Template template = freemarkerConfig.getTemplate(templateName);
        // 2. 创建字符串写入器（用于接收模板渲染后的内容）
        Writer writer = new StringWriter();
        try {
            // 3. 渲染模板：将数据模型填充到模板中，输出到字符串写入器
            template.process(dataModel, writer);
            // 4. 将写入器内容转为字符串
            return writer.toString();
        } catch (TemplateException e) {
            throw new Exception("FreeMarker 模板渲染失败", e);
        } finally {
            // 5. 关闭写入器
            writer.close();
        }
    }

    // 测试示例
    public static void main(String[] args) throws Exception {
        // 模拟 Spring 环境（实际项目直接注入使用）
        FreeMarkerTemplateUtil util = new FreeMarkerTemplateUtil();
        // 手动初始化 Configuration（实际由 Spring 自动配置）
        Configuration config = new Configuration(Configuration.VERSION_2_3_32);
        config.setClassForTemplateLoading(FreeMarkerTemplateUtil.class, "/templates");
        config.setDefaultEncoding("UTF-8");
        util.freemarkerConfig = config;

        // 构造数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("title", "FreeMarker 测试");
        Map<String, Object> user = new HashMap<>();
        user.put("name", "张三");
        user.put("age", 25);
        user.put("hobbies", new String[]{"看书", "跑步", "编程"});
        dataModel.put("user", user);

        // 生成字符串
        String result = util.generateStringFromTemplate("demo.ftl", dataModel);
        // 打印结果
        System.out.println(result);
    }
}