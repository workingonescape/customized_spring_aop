package springframework.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Reece
 * @version 1.0.0
 * @ClassName SpringConfigParser.java
 * @Description TODO
 * @createTime 2021年05月09日 10:39:00
 */
public class SpringConfigParser {


    
   /**
   * @description: spring配置解析
   * @param: springConfig
   * @return: java.lang.String
   * @date: 2021-05-09 11:49:56
   */
    public static String getBaePackage(String springConfig) {
        InputStream inputStream = null;
        String basePackage = "";
        try {
            SAXReader reader = new SAXReader();
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(springConfig);
            Document document = reader.read(inputStream);
            Element rootElement = document.getRootElement();
            Element element = rootElement.element("component-scan");
            Attribute attribute = element.attribute("base-package");
            basePackage = attribute.getText();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return basePackage;
    }
    

}
