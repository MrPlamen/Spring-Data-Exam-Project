package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Attraction;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    Optional<Attraction> findByName(String name);

    List<Attraction> findByTypeInAndElevationGreaterThanEqualOrderByNameAscCountryAsc (Collection<String> type, Integer elevation);
}
