package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.dto.ApiResponse;
import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.model.User;
import com.example.backendreservacionesspring.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // GET /reservations
    @GetMapping
    public ApiResponse<List<Reservation>> index() {
        return ApiResponse.success(reservationService.findAll());
    }

    // GET /reservations/user
    @GetMapping("/user")
    public ApiResponse<List<Reservation>> userReservations() {
        //Long userId = Long.parseLong(userDetails.getUsername());
        return ApiResponse.success(reservationService.findByUserId(2L));
    }

    // GET /reservations/{date}/{space_id}
    @GetMapping("/{date}/{space_id}")
    public ApiResponse<List<Reservation>> search(
            @PathVariable LocalDate date,
            @PathVariable("space_id") Long spaceId) {
        return ApiResponse.success(reservationService.findByDateAndSpace(date, spaceId));
    }

    // GET /reservations/pagination
    @GetMapping("/pagination")
    public ApiResponse<Page<Reservation>> indexPaginated(){
        return ApiResponse.success(reservationService.findAllPaginated());
    }

    // POST /reservations
    @PostMapping
    public ApiResponse<Reservation> store(@RequestBody Map<String, Object> requestData) {
        //FALTA EN ENVIADO DEL EMAIL
        // Extrae los datos del mapa
        Long spaceId = Long.valueOf(requestData.get("space_id").toString());
        Long userId = Long.valueOf(requestData.get("user_id").toString());
        LocalDate date = LocalDate.parse(requestData.get("date").toString());
        LocalTime startTime = LocalTime.parse(requestData.get("start_time").toString());
        LocalTime endTime = LocalTime.parse(requestData.get("end_time").toString());

        // Crea la reservación
        Reservation reservation = new Reservation();
        reservation.setDate(date);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);

        // Asigna las relaciones (solo necesitamos los IDs)
        Space space = new Space();
        space.setId(spaceId);
        reservation.setSpace(space);

        User user = new User();
        user.setId(userId);
        reservation.setUser(user);

        return ApiResponse.created(reservationService.create(reservation));
    }

    // GET /reservations/{id}
    @GetMapping("/{id}")
    public ApiResponse<Reservation> show(@PathVariable Long id) {
        return reservationService.findById(id)
                .map(ApiResponse::success)
                .orElseThrow(() -> new EntityNotFoundException("Reservación no encontrada"));
    }

    // PUT /reservations/{id}
    @PutMapping("/{id}")
    public ApiResponse<Reservation> update(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {
        return ApiResponse.success(reservationService.update(id, reservation));
    }

    // DELETE /reservations/{id}
    @DeleteMapping("/{id}")
    public ApiResponse<Void> destroy(@PathVariable Long id) {
        reservationService.delete(id);
        return ApiResponse.success(null);
    }

    // PATCH /reservations/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            Reservation updated = reservationService.updatePartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha/hora inválido");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar reservación");
        }
    }
}