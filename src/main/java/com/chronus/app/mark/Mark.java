package com.chronus.app.mark;

import com.chronus.app.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime markTime;

    public Mark() {}

    public Mark (User user, LocalDateTime markTime) {
        this.user = user;
        this.markTime = markTime;
    }

    public User getUser() {
        return user;
    }
}
