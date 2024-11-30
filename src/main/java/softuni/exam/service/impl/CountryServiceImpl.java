package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryImportDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//ToDo - Implement all the methods
@Service
public class CountryServiceImpl implements CountryService {

    private static final String COUNTRIES_PATH = "src/main/resources/files/json/countries.json";

    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {

        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountryFileContent() throws IOException {

        return Files.readString(Path.of(COUNTRIES_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();

        CountryImportDTO[] countryImportDTOs = this.gson.fromJson(readCountryFileContent(), CountryImportDTO[].class);
        for (CountryImportDTO countryImportDTO : countryImportDTOs) {
            if (this.countryRepository.findByName(countryImportDTO.getName()).isPresent() ||
                    !this.validationUtil.isValid(countryImportDTO)) {
                sb.append("Invalid country").append(System.lineSeparator());
                continue;
            }

            this.countryRepository.saveAndFlush(this.modelMapper.map(countryImportDTO, Country.class));
            sb.append(String.format("Successfully imported country %s",
                            countryImportDTO.getName()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
