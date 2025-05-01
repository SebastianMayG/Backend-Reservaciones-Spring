package service;

import model.Schedule;
import repository.ScheduleRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public class ScheduleService {
    private ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule update(Long id, Schedule scheduleUpdated){
        return scheduleRepository.findById(id).map(schedule -> {
            schedule.setStartTime(scheduleUpdated.getStartTime());
            schedule.setEndTime(scheduleUpdated.getEndTime());
            return scheduleRepository.save(schedule);
        }).orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public Schedule updatePartial(Long id,Map<String, Object> campos){
        return scheduleRepository.findById(id).map(schedule -> {
            if(campos.containsKey("startTime")){
                schedule.setStartTime((LocalTime) campos.get("startTime"));
            }
            if(campos.containsKey("endTime")){
                schedule.setEndTime((LocalTime) campos.get("endTime"));
            }
            return scheduleRepository.save(schedule);
        }).orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public void deleteSchedule(Long id) {
        if(!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }
}
