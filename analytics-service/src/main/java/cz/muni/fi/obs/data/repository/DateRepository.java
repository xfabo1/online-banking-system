package cz.muni.fi.obs.data.repository;

import cz.muni.fi.obs.data.dbo.DateDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DateRepository extends JpaRepository<DateDimension, String>{
    @Query("SELECT d FROM DateDimension d WHERE d.yearNumber = ?1 AND d.monthNumber = ?2 AND d.dayNumber = ?3")
    Optional<DateDimension> findByYearAndMonthAndDay(int year, int monthValue, int dayOfMonth);
}
