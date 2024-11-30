package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AttractionImportDTO;
import softuni.exam.models.entity.Attraction;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.AttractionRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.AttractionService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//ToDo - Implement all the methods
@Service
public class AttractionServiceImpl implements AttractionService {

    private static final String ATTRACTIONS_PATH = "src/main/resources/files/json/attractions.json";

    private final AttractionRepository attractionRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CountryRepository countryRepository;

    @Autowired
    public AttractionServiceImpl(AttractionRepository attractionRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, CountryRepository countryRepository) {
        this.attractionRepository = attractionRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.countryRepository = countryRepository;
    }

    @Override
    public boolean areImported() {

        return this.attractionRepository.count() > 0;
    }

    @Override
    public String readAttractionsFileContent() throws IOException {

        return Files.readString(Path.of(ATTRACTIONS_PATH));
    }

    @Override
    public String importAttractions() throws IOException {
        StringBuilder sb = new StringBuilder();

        AttractionImportDTO[] attractionImportDTOs = this.gson.fromJson(readAttractionsFileContent(), AttractionImportDTO[].class);
        for (AttractionImportDTO attractionImportDTO : attractionImportDTOs) {
            if (this.attractionRepository.findByName(attractionImportDTO.getName()).isPresent() ||
                    !this.validationUtil.isValid(attractionImportDTO)) {
                sb.append("Invalid attraction").append(System.lineSeparator());
                continue;
            }

            Country country = this.countryRepository.findById((long) attractionImportDTO.getCountry()).get();
            Attraction attraction = this.modelMapper.map(attractionImportDTO, Attraction.class);
            attraction.setCountry(country);

            this.attractionRepository.save(attraction);

            sb.append(String.format("Successfully imported attraction %s", attractionImportDTO.getName()))
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }


    @Override
    public String exportAttractions() {
        StringBuilder sb = new StringBuilder();

        this.attractionRepository.findByTypeInAndElevationGreaterThanEqualOrderByNameAscCountryAsc(Arrays.asList("historical site", "archaeological site"),300)
                .forEach(a -> {
                    sb.append(String.format("Attraction with ID%d:%n" +
                            "***%s - %s at an altitude of %dm. somewhere in %s.",
                                    a.getId(),
                                    a.getName(),
                                    a.getDescription(),
                                    a.getElevation(),
                                    a.getCountry().getName()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }
}
