package softuni.exam.service.impl;

import jakarta.xml.bind.JAXBException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.VisitorImportDTO;
import softuni.exam.models.dto.VisitorImportRootDTO;
import softuni.exam.models.entity.Visitor;
import softuni.exam.repository.AttractionRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.repository.PersonalDataRepository;
import softuni.exam.repository.VisitorRepository;
import softuni.exam.service.VisitorService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VisitorServiceImpl implements VisitorService {

    private static final String VISITORS_PATH = "src/main/resources/files/xml/visitors.xml";
    private final VisitorRepository visitorRepository;
    private final AttractionRepository attractionRepository;
    private final CountryRepository countryRepository;
    private final PersonalDataRepository personalDataRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;


    @Autowired
    public VisitorServiceImpl(VisitorRepository visitorRepository, XmlParser xmlParser, ValidationUtil validator, AttractionRepository attractionRepository, CountryRepository countryRepository, PersonalDataRepository personalDataRepository, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.visitorRepository = visitorRepository;
        this.xmlParser = xmlParser;
        this.attractionRepository = attractionRepository;
        this.countryRepository = countryRepository;
        this.personalDataRepository = personalDataRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.visitorRepository.count() > 0;
    }

    @Override
    public String readVisitorsFileContent() throws IOException {
        return Files.readString(Path.of(VISITORS_PATH));
    }

    @Override
    public String importVisitors() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        VisitorImportRootDTO visitorImportRootDTO = this.xmlParser.fromFile(VISITORS_PATH, VisitorImportRootDTO.class);
        for (VisitorImportDTO visitorImportDTO : visitorImportRootDTO.getVisitorImportDTOS()) {
            if (this.visitorRepository.findByPersonalDataId(visitorImportDTO.getPersonalDataId()).isPresent() ||
                    this.visitorRepository.findByFirstNameAndLastName(visitorImportDTO.getFirstName(),
                            visitorImportDTO.getLastName()).isPresent() ||
                    !this.validationUtil.isValid(visitorImportDTO)) {
                sb.append("Invalid visitor").append(System.lineSeparator());
                continue;
            }

            Visitor visitor = this.modelMapper.map(visitorImportDTO, Visitor.class);
            visitor.setAttraction(this.attractionRepository.findById((long) visitorImportDTO.getAttractionId()).get());
            visitor.setCountry(this.countryRepository.findById((long) visitorImportDTO.getCountryId()).get());
            visitor.setPersonalData(this.personalDataRepository.findById((long) visitorImportDTO.getPersonalDataId()).get());

            this.visitorRepository.saveAndFlush(visitor);
            sb.append(String.format("Successfully imported visitor %s %s",
                            visitorImportDTO.getFirstName(),
                            visitorImportDTO.getLastName()))
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
