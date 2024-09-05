package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;  // Одноразовый токен

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;  // Пользователь, для которого сгенерирован токен

    @Column(nullable = false)
    private LocalDateTime expiryDate;  // Срок действия токена

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    // Конструктор, принимающий токен и пользователя, и устанавливающий дату истечения срока действия
    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusHours(24);  // Пример: токен действует 24 часа
    }
}
