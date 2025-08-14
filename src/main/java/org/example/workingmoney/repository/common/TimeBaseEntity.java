package org.example.workingmoney.repository.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeBaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
