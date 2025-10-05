package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.mark.services.MarkService;
import com.chronus.app.user.User;
import com.chronus.app.utils.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MarkServiceTest {
    MarkService sut = new MarkService();

    @Test
    @DisplayName("Adding a mark to a user")
    public void addingANewMarkTest() {
        MarkRepository repositoryMock = mock(MarkRepository.class);
        sut.repository = repositoryMock;
        LocalDate date = LocalDate.of(2022, 3, 22);
        LocalTime time = LocalTime.of(8, 25);
        User user = new User("Flaco Lópes", "password", "flacomatador@sep.com");
        Mark mark = new Mark(user, LocalDateTime.of(date, time));

        when(repositoryMock.save(mark)).thenReturn(mark);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(201, "Mark added with success!", mark));

        verify(repositoryMock, atLeast(1)).save(mark);
    }

    @Test
    @DisplayName("Adding a new mark to a date already marked")
    public void addingANewMarkToAnUnavailableDate() {
        MarkRepository repositoryMock = mock(MarkRepository.class);
        sut.repository = repositoryMock;
        sut.validator = new MarkValidator(repositoryMock);
        LocalDate date = LocalDate.of(2022, 3, 22);
        LocalTime time = LocalTime.of(8, 25);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        User user = new User("Flaco Lópes", "password", "flacomatador@sep.com");
        Mark mark = new Mark(user, dateTime);

        when(repositoryMock.getMarkByMarkTime(dateTime)).thenReturn(List.of(new Mark(user, dateTime)));
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "Already exists a mark to this date!", null));

        verify(repositoryMock, atLeast(1)).save(mark);
    }
}
