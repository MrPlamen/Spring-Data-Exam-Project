package softuni.exam.models.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "visitors")
@XmlAccessorType(XmlAccessType.FIELD)
public class VisitorImportRootDTO {

    @XmlElement(name = "visitor")
    private List<VisitorImportDTO> visitorImportDTOS;

    public List<VisitorImportDTO> getVisitorImportDTOS() {
        return visitorImportDTOS;
    }

    public void setVisitorImportDTOS(List<VisitorImportDTO> visitorImportDTOS) {
        this.visitorImportDTOS = visitorImportDTOS;
    }
}