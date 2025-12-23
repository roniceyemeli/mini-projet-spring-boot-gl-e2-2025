package com.service.student.dto.sync;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSyncDTO {
    private Long studentId;
    private UUID userId;
    private UUID advisorId;
    private String syncType; // USER, ADVISOR, BOTH
}