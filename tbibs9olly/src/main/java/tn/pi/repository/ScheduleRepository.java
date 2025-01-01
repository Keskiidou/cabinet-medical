package tn.pi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.pi.entity.Schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDoctor_Id(Long doctorId);  // Use the doctor relationship's id

    @Query("SELECT s FROM Schedule s WHERE s.dayOfWeek = :dayOfWeek AND s.doctor.id = :doctorId " +
            "AND (:startTime < s.endTime AND :endTime > s.startTime)")
    List<Schedule> findOverlappingSchedules(@Param("doctorId") Long doctorId,
                                            @Param("dayOfWeek") DayOfWeek dayOfWeek,
                                            @Param("startTime") LocalTime startTime,
                                            @Param("endTime") LocalTime endTime);
}
