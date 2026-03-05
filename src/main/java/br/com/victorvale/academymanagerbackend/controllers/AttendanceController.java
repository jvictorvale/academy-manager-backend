package br.com.victorvale.academymanagerbackend.controllers;

import br.com.victorvale.academymanagerbackend.dto.AttendanceDTO;
import br.com.victorvale.academymanagerbackend.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendances/v1")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Attendance", description = "Endpoints for Student Check-ins")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get Student History", description = "Returns all check-ins for a specific student",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceDTO.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<List<AttendanceDTO>> getStudentHistory(@PathVariable Long studentId) {
        var history = attendanceService.getStudentHistory(studentId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/class/{classDate}")
    @Operation(summary = "Get Class Attendance", description = "Returns all attendances for a specific date (Format: YYYY-MM-DD)",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AttendanceDTO.class)))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<List<AttendanceDTO>> getAttendancesByDate(@PathVariable LocalDate classDate) {
        var attendances = attendanceService.getAttendancesByDate(classDate);
        return ResponseEntity.ok(attendances);
    }

    @PostMapping("/checkin/{studentId}")
    @Operation(summary = "Register Check-in", description = "Registers a new attendance for a student",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(schema = @Schema(implementation = AttendanceDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<AttendanceDTO> registerCheckIn(@PathVariable Long studentId, @RequestParam(required = false) LocalDate classDate) {
        var checkIn = attendanceService.registerCheckIn(studentId, classDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(checkIn);
    }

    @PutMapping("/class/{classDate}")
    @Operation(summary = "Sync Class Attendance", description = "Sends a list of student IDs present on a specific date. Creates new check-ins and removes unchecked ones.",
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Void> syncClassAttendance(
            @PathVariable LocalDate classDate,
            @RequestBody List<Long> presentStudentIds) {

        attendanceService.syncClassAttendance(classDate, presentStudentIds);
        return ResponseEntity.noContent().build();
    }
}
