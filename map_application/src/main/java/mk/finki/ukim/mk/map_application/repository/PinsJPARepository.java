package mk.finki.ukim.mk.map_application.repository;

import mk.finki.ukim.mk.map_application.model.Category;
import mk.finki.ukim.mk.map_application.model.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PinsJPARepository extends JpaRepository<Pin, Integer> {
    @Query("select p from Pin p where p.category = ?1")
    List<Pin> findAllByCategory(Category category);
}
