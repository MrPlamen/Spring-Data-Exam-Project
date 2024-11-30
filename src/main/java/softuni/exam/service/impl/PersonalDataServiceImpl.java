package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PersonalDataImportDTO;
import softuni.exam.models.dto.PersonalDataImportRootDTO;
import softuni.exam.models.entity.PersonalData;
import softuni.exam.repository.PersonalDataRepository;
import softuni.exam.service.PersonalDataService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//ToDo - Implement all the methods

@Service
public class PersonalDataServiceImpl implements PersonalDataService {

    private static final String PERSONAL_DATA_PATH = "src/main/resources/files/xml/personal_data.xml";

    private final PersonalDataRepository personalDataRepository;

    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public PersonalDataServiceImpl(PersonalDataRepository personalDataRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.personalDataRepository = personalDataRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {

        return this.personalDataRepository.count() > 0;
    }

    @Override
    public String readPersonalDataFileContent() throws IOException {

        return Files.readString(Path.of(PERSONAL_DATA_PATH));
    }

    @Override
    public String importPersonalData() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        PersonalDataImportRootDTO personalDataImportRootDTO = this.xmlParser.fromFile(PERSONAL_DATA_PATH, PersonalDataImportRootDTO.class);
        for (PersonalDataImportDTO personalDataImportDTO : personalDataImportRootDTO.getPersonalDataImportDTOS()) {
            if (this.personalDataRepository.findByCardNumber(personalDataImportDTO.getCardNumber()).isPresent() ||
                    !this.validationUtil.isValid(personalDataImportDTO)) {
                sb.append("Invalid personal data").append(System.lineSeparator());
                continue;
            }

            PersonalData personalData = this.modelMapper.map(personalDataImportDTO, PersonalData.class);

            this.personalDataRepository.saveAndFlush(personalData);
            sb.append(String.format("Successfully imported personal data for visitor with card number %s",
                    personalDataImportDTO.getCardNumber()))
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

}
