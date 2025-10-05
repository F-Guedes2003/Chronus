package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarkValidatorTest {
    MarkValidator sut = new MarkValidator();

    @Test
    @DisplayName("Verifying if a dateTime is already marked")
    public void verifyingIfADateTimeIsMarked() {
        MarkRepository repositoryMock = mock(MarkRepository.class);
        var dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        var mark  = new Mark(new User("Flaco López", "password", "flaquitomatador@sep.com"), dateTime);
        when(repositoryMock.getMarkByMarkTime(dateTime)).thenReturn(List.of(mark));

        assertThat(sut.isDateTimeAlreadyMarked(dateTime)).isEqualTo(true);
    }
}
