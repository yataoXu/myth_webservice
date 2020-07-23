package websvc.utils;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 16:55
 * @Mail : xuyt@zendaimoney.com
 */
public class XMLUtil {
    public static String beanToXml(Object obj, Class<?> load) throws Exception {
        if(obj == null){
            obj = load.newInstance();
        }

        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}
