package org.example.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserProfileUpdatedEvent {
    private String username;
    private String email;
    private String oldAddress;
    private String newAddress;
    private String oldName;
    private String newName;
    private LocalDateTime updatedAt;
}