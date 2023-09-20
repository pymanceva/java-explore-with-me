package ru.practicum.ewm.main.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.compilation.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    int deleteCompilationById(Long id);

    @Query("SELECT c FROM Compilation AS c " +
            "WHERE (:pinned IS NULL OR c.isPinned = :pinned)")
    List<Compilation> findAllByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}
