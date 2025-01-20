package softuni.exam.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;

import jakarta.xml.bind.JAXBException;
import java.io.File;

@Component
public class XmlParserImpl implements XmlParser {


    @Override
    @SuppressWarnings(value = "unchecked")
    public <T> T fromFile(String filePath, Class<T> tClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        T unmarshal = (T) unmarshaller.unmarshal(new File(filePath));

        return unmarshal;
    }
}
