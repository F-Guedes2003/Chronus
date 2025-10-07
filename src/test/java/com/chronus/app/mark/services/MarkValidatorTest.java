package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.MarkType;
import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.User;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

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

    static Stream<Arguments> marksProvider() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        List<Mark> listOne = List.of(new Mark(user, LocalDateTime.of(2025, 3, 12, 7, 25, 0)),
                new Mark(user, LocalDateTime.of(2025, 3, 12, 7, 50, 0)),
                new Mark(user, LocalDateTime.of(2025, 3, 12, 9, 0, 0)));

        List<Mark> listTwo = List.of(new Mark(user, LocalDateTime.of(2025, 3, 12, 7, 25, 0)),
                new Mark(user, LocalDateTime.of(2025, 3, 12, 7, 50, 0)),
                new Mark(user, LocalDateTime.of(2025, 3, 12, 9, 1, 0)));

        return Stream.of(Arguments.of(listOne, false), Arguments.of(listTwo, true));
    }

    @ParameterizedTest(name = "[{index}] -> Interval validation should return {1}")
    @MethodSource("marksProvider")
    @DisplayName("Verifying the interval limit between two points")
    public void verifyingMarkTimeInterval(List<Mark> repoReturn, Boolean result){
        var currentMark = new Mark(generalUser, LocalDateTime.of(2025, 3, 12, 21, 0, 0));

        when(repositoryMock.getMarkByDate(currentMark)).thenReturn(repoReturn);
        assertThat(sut.isValidMarkInterval(currentMark)).isEqualTo(result);
        verify(repositoryMock, atLeast(1)).getMarkByDate(currentMark);
    }

    static Stream<Arguments> markTypeProvider() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        LocalDate date = LocalDate.of(2025, 3, 12);
        List<Mark> listOne = List.of(
                new Mark(user, LocalDateTime.of(date, LocalTime.of(7, 25, 0)), true, MarkType.ENTRY),
                new Mark(user, LocalDateTime.of(date, LocalTime.of(8, 10, 0)), true, MarkType.EXIT));

        List<Mark> listTwo = List.of(
                new Mark(user, LocalDateTime.of(date, LocalTime.of(7, 25, 0)), true, MarkType.ENTRY),
                new Mark(user, LocalDateTime.of(date, LocalTime.of(7, 50, 0)), true, MarkType.EXIT),
                new Mark(user, LocalDateTime.of(date, LocalTime.of(9, 1, 0)), true, MarkType.ENTRY));

        return Stream.of(
                Arguments.of(listOne, new Mark(user, LocalDateTime.of(date, LocalTime.of(7, 50, 0)), false, MarkType.ENTRY), Arguments.of(listTwo, true)),
                Arguments.of());
    }

    @Test
    @DisplayName("mark should be denied if there is a valid mark of the same type before it")
    public void isValidMarkType() {
        LocalDate date = LocalDate.of(2025, 3, 12);
        var mark = new Mark(generalUser, LocalDateTime.of(date, LocalTime.of(7, 50, 0)), true, MarkType.ENTRY);
        List<Mark> markList = List.of(
                new Mark(generalUser, LocalDateTime.of(date, LocalTime.of(7, 25, 0)), true, MarkType.ENTRY),
                new Mark(generalUser, LocalDateTime.of(date, LocalTime.of(8, 10, 0)), true, MarkType.EXIT));
        when(repositoryMock.getMarkByDate(mark)).thenReturn(markList);


        assertThat(sut.isValidMarkType);
    }
}
