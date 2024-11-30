package softuni.exam.models.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "personal_datas")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonalDataImportRootDTO {

    @XmlElement(name = "personal_data")
    private List<PersonalDataImportDTO> personalDataImportDTOS;

    public List<PersonalDataImportDTO> getPersonalDataImportDTOS() {
        return personalDataImportDTOS;
    }

    public void setPersonalDataImportDTOS(List<PersonalDataImportDTO> personalDataImportDTOS) {
        this.personalDataImportDTOS = personalDataImportDTOS;
    }
}
