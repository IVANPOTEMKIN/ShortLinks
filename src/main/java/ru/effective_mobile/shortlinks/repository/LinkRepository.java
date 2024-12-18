package ru.effective_mobile.shortlinks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.effective_mobile.shortlinks.entity.Link;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByAlias(String alias);

    List<Link> findAllByAliasIsContainingIgnoreCaseOrderByAlias(String alias);

    List<Link> findAllByTtlLessThanEqualOrderByTtl(LocalDate ttl);

    @Query("""
            SELECT l.originalUrl
            FROM links l
            WHERE l.alias = :alias
            """)
    Optional<String> findOriginalUrl(@Param("alias") String alias);

    @Modifying
    @Query("""
            UPDATE links l
            SET l.alias = :alias,
                l.shortUrl = :shortUrl
            WHERE l.id = :id
            """)
    int updateAlias(@Param("id") Long id,
                    @Param("alias") String alias,
                    @Param("shortUrl") String shortUrl);

    @Modifying
    @Query("""
            UPDATE links l
            SET l.ttl = :ttl
            WHERE l.id = :id
            """)
    int updateTtl(@Param("id") Long id,
                  @Param("ttl") LocalDate ttl);

    @Modifying
    @Query("""
            DELETE FROM links l
            WHERE l.id = :id
            """)
    int deleteLinkById(@Param("id") Long id);

    @Modifying
    @Query("""
            DELETE FROM links l
            WHERE l.alias = :alias
            """)
    int deleteLinkByAlias(@Param("alias") String alias);

    @Modifying
    @Query("""
            DELETE FROM links l
            WHERE l.ttl < :ttl
            """)
    void deleteAllLinksByTtlBefore(@Param("ttl") LocalDate ttl);
}