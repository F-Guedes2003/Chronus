package com.chronus.app.mark;

import com.chronus.app.MarkType;
import com.chronus.app.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
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
    private Boolean isValid;
    private MarkType type;

    public Mark() {}

    public Mark (User user, LocalDateTime markTime) {
        this.user = user;
        this.markTime = markTime;
        this.isValid = true;
    }

    public Mark (User user, LocalDateTime markTime, Boolean isValid, MarkType type) {
        this.user = user;
        this.markTime = markTime;
        this.isValid = isValid;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getMarkTime() {
        return markTime;
    }

    public LocalDate getMarkDate(){
        return markTime.toLocalDate();
    }

    public void setMarkTime(LocalDateTime markTime) {
        this.markTime = markTime;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public MarkType getType() {
        return type;
    }

    public void setType(MarkType type) {
        this.type = type;
    }

    public Boolean sameType(Mark mark) {
        return mark.getType().equals(getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Mark other = (Mark) obj;
        return java.util.Objects.equals(user, other.user) &&
                java.util.Objects.equals(markTime, other.markTime) &&
                other.isValid == isValid;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(user, markTime, isValid);
    }
}
