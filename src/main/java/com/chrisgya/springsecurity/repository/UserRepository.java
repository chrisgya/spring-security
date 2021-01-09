package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query(value = "SELECT bp.users.*, ts_rank_cd(search_field, query, 1) AS rank\n" +
            "FROM bp.users, to_tsquery(?1) query\n" +
            "WHERE query @@ search_field\n" +
            "ORDER BY rank DESC",
            countQuery = "SELECT count(*)\n" +
                    "FROM bp.users, to_tsquery(?1) query\n" +
                    "WHERE query @@ search_field",
            nativeQuery = true)
    Page<User> searchUsers(final String text, final Pageable pageable);

//    Page<Event> findByName(@Param("name") String name, Pageable pageable);
//    Page<Event> findByNameAndZoneId(@Param("name") String name, @Param("zoneId") ZoneId zoneId, Pageable pageable);
//    List<Comment> findByPost(Post post);
//    List<Comment> findAllByUser(User user);
}
