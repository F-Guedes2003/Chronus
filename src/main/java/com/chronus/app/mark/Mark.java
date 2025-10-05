package com.chronus.app.mark;

import com.chronus.app.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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

    public LocalDateTime getMarkTime() {
        return markTime;
    }

    public void setMarkTime(LocalDateTime markTime) {
        this.markTime = markTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Mark other = (Mark) obj;
        return java.util.Objects.equals(user, other.user) &&
                java.util.Objects.equals(markTime, other.markTime);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(user, markTime);
    }
}
