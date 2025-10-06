package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarkValidatorTest {
    MarkValidator sut = new MarkValidator();
    User generalUser = new User("Flaco López", "password", "Flaquito Matador");
    @Mock MarkRepository repositoryMock;

    @BeforeEach
    public void setup() {
        sut.repository = repositoryMock;
    }

    @Test
    @DisplayName("Verifying if a dateTime is already marked")
    public void verifyingIfADateTimeIsMarked() {
        var dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        var mark  = new Mark(new User("Flaco López", "password", "flaquitomatador@sep.com"), dateTime);
        when(repositoryMock.getMarkByMarkTime(dateTime)).thenReturn(List.of(mark));

        assertThat(sut.isDateTimeAlreadyMarked(dateTime)).isEqualTo(true);
    }

    @Test
    @DisplayName("Verifying the interval limit between two points")
    public void verifyingMarkTimeInterval(){
        var firstMark = new Mark(generalUser, LocalDateTime.of(2025, 3, 12, 7, 25, 0));
        var secondMark = new Mark(generalUser, LocalDateTime.of(2025, 3, 12, 7, 50, 0));
        var thirdMark = new Mark(generalUser, LocalDateTime.of(2025, 3, 12, 9, 0, 0));
        var currentMark = new Mark(generalUser, LocalDateTime.of(2025, 3, 12, 21, 0, 0));

        List<Mark> repoReturn = List.of(firstMark, secondMark, thirdMark);
        when(repositoryMock.getMarkByDate(currentMark)).thenReturn(repoReturn);
        verify(repositoryMock, atLeast(1)).getMarkByDate(currentMark);

        assertThat(sut.isValidMarkInterval(currentMark)).isEqualTo(false);
    }
}
