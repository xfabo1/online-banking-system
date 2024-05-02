package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    default User findByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR u.firstName = :firstName) AND " +
            "(:lastName IS NULL OR u.lastName = :lastName) AND " +
            "(:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber) AND " +
            "(:email IS NULL OR u.email = :email) AND " +
            "(cast(:birthDate as date) IS NULL OR u.birthDate = :birthDate) AND " +
            "(:birthNumber IS NULL OR u.birthNumber = :birthNumber) AND " +
            "(:active IS NULL OR u.active = :active)")
    Page<User> findBySearchParams(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phoneNumber") String phoneNumber,
            @Param("email") String email,
            @Param("birthDate") LocalDate birthDate,
            @Param("birthNumber") String birthNumber,
            @Param("active") Boolean active,
            Pageable pageable
    );
}
