package mk.finki.ukim.mk.map_application.repository;

import mk.finki.ukim.mk.map_application.model.Map;
import mk.finki.ukim.mk.map_application.model.Visibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapsJPARepository extends JpaRepository<Map, Integer> {
    @Query("select m from Map m where m.owner.id = ?1 ")
    List<Map> findByOwnerId(Long id);

    @Query("select  m from Map m where m.visibility = 'PUBLIC' and m.approved = 0 ")
    List<Map> findAllPending();
}
