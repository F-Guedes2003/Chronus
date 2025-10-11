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
    MarkValidator sut;
    User generalUser = new User("Flaco López", "password", "Flaquito Matador");
    @Mock MarkRepository repositoryMock;

    @BeforeEach
    public void setup() {
        sut = new MarkValidator(repositoryMock);
    }

    @Test
    @DisplayName("Verifying if a dateTime is already marked")
    public void verifyingIfADateTimeIsMarked() {
        var mark  = new Mark(new User("Flaco López", "password", "flaquitomatador@sep.com"), LocalTime.now(), LocalDate.now());
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(mark.getMarkTime(), mark.getMarkDate())).thenReturn(List.of(mark));

        assertThat(sut.isDateTimeAlreadyMarked(mark)).isEqualTo(true);
    }

    static Stream<Arguments> marksProvider() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        var date = LocalDate.of(2025, 3, 12);
        List<Mark> listOne = List.of(new Mark(user, LocalTime.of(7, 25, 0), date),
                new Mark(user, LocalTime.of(7, 50, 0), date),
                new Mark(user, LocalTime.of(9, 0, 0), date));

        List<Mark> listTwo = List.of(new Mark(user, LocalTime.of(7, 25, 0), date),
                new Mark(user, LocalTime.of(7, 50, 0), date),
                new Mark(user, LocalTime.of(9, 1, 0), date));

        return Stream.of(Arguments.of(listOne, false), Arguments.of(listTwo, true));
    }

    @ParameterizedTest(name = "[{index}] -> Interval validation should return {1}")
    @MethodSource("marksProvider")
    @DisplayName("Verifying the interval limit between two points")
    public void verifyingMarkTimeInterval(List<Mark> repoReturn, Boolean result){
        var currentMark = new Mark(generalUser, LocalTime.of(21, 0, 0), LocalDate.of(2025, 3, 12));

        when(repositoryMock.getMarksByMarkDate(currentMark.getMarkDate())).thenReturn(repoReturn);
        assertThat(sut.isValidMarkInterval(currentMark)).isEqualTo(result);
        verify(repositoryMock, atLeast(1)).getMarksByMarkDate(currentMark.getMarkDate());
    }

    static Stream<Arguments> markTypeProvider() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        LocalDate date = LocalDate.of(2025, 3, 12);
        List<Mark> listOne = List.of(
                new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                new Mark(user, LocalTime.of(8, 10, 0), date, true, MarkType.EXIT));

        List<Mark> listTwo = List.of(
                new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                new Mark(user, LocalTime.of(7, 50, 0), date, true, MarkType.EXIT),
                new Mark(user, LocalTime.of(9, 1, 0), date, true, MarkType.ENTRY));

        return Stream.of(
                Arguments.of(listOne, new Mark(user, LocalTime.of(7, 50, 0), date, false, MarkType.ENTRY), Arguments.of(listTwo, true)),
                Arguments.of());
    }

    static Stream<Arguments> marksTypeProviderOfValidNeighbours() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        var date = LocalDate.of(2025, 3, 12);
        return Stream.of(
                Arguments.of(List.of(
                    new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                    new Mark(user, LocalTime.of(8, 10, 0), date, true, MarkType.EXIT)),
                    new Mark(user, LocalTime.of(8, 50, 0), date, true, MarkType.EXIT), false),
                Arguments.of(List.of(
                        new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                                new Mark(user, LocalTime.of(8, 10, 0), date, true, MarkType.EXIT),
                                new Mark(user, LocalTime.of(12, 0, 0), date, true, MarkType.ENTRY)),
                        new Mark(user, LocalTime.of(7, 45, 0), date, true, MarkType.EXIT), false),
                Arguments.of(List.of(
                                new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                                new Mark(user, LocalTime.of(8, 10, 0), date, true, MarkType.EXIT),
                                new Mark(user, LocalTime.of(12, 0, 0), date, true, MarkType.ENTRY)),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), false),
                Arguments.of(List.of(
                                new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                                new Mark(user, LocalTime.of(8, 10, 0), date, true, MarkType.EXIT),
                                new Mark(user, LocalTime.of(12, 0, 0), date, true, MarkType.EXIT)),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), false));
    }

    @ParameterizedTest(name = "{index} - should return {2}")
    @MethodSource("marksTypeProviderOfValidNeighbours")
    @DisplayName("mark should be denied if there is a valid mark of the same type before or after it")
    public void isValidMarkTypeBetweenValidElements(List<Mark> markList, Mark mark, Boolean result) {
        when(repositoryMock.getMarksByMarkDate(mark.getMarkDate())).thenReturn(markList);

        assertThat(sut.isValidMarkType(mark)).isEqualTo(result);
    }

    static Stream<Arguments> marksTypeProviderOfInvalidNeighbours() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        var date = LocalDate.of(2025, 3, 12);
        return Stream.of(
                Arguments.of(List.of(
                                new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                                new Mark(user, LocalTime.of(8, 10, 0), date, false, MarkType.EXIT),
                                new Mark(user, LocalTime.of(12, 0, 0), date, true, MarkType.ENTRY)),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), true),
                Arguments.of(List.of(
                                new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                                new Mark(user, LocalTime.of(8, 10, 0), date, true, MarkType.EXIT),
                                new Mark(user, LocalTime.of(12, 0, 0), date, false, MarkType.ENTRY)),
                        new Mark(user, LocalTime.of(8, 40, 0), date, true, MarkType.ENTRY), true),
                Arguments.of(List.of(
                                new Mark(user, LocalTime.of(7, 25, 0), date, true, MarkType.ENTRY),
                                new Mark(user, LocalTime.of(8, 10, 0), date, true, MarkType.EXIT),
                                new Mark(user, LocalTime.of(12, 0, 0), date, false, MarkType.ENTRY)),
                        new Mark(user, LocalTime.of(12, 40, 0), date, true, MarkType.ENTRY), true));
    }

    @ParameterizedTest(name = "{index} - should return {2}")
    @MethodSource("marksTypeProviderOfInvalidNeighbours")
    @DisplayName("mark should be allowed if there is an invalid mark of the same type before or after it")
    public void isValidMarkTypeBetweenInvalidElements(List<Mark> markList, Mark mark, Boolean result) {
        when(repositoryMock.getMarksByMarkDate(mark.getMarkDate())).thenReturn(markList);

        assertThat(sut.isValidMarkType(mark)).isEqualTo(result);
    }

    static Stream<Arguments> marksBeforeOrAtFirstMarkOfTheDayProvider() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        var date = LocalDate.of(2025, 3, 12);

        return Stream.of(Arguments.of(
                new Mark(user, LocalTime.of(9, 0, 0), date, true, MarkType.EXIT), false),
                Arguments.of(
                        new Mark(user, LocalTime.of(8, 59, 0), date, true, MarkType.EXIT), false),
                Arguments.of(
                        new Mark(user, LocalTime.of(9, 51, 0), date, true, MarkType.EXIT), true));
    }

    @ParameterizedTest(name = "{1} - should return {1}")
    @MethodSource("marksBeforeOrAtFirstMarkOfTheDayProvider")
    @DisplayName("should not be able to insert an exit mark before the first mark of the day")
    public void cannotInsertAnExitMarkBeforeTheFirstMarkOfTheDay(Mark newMark, Boolean result) {
        LocalDate date = LocalDate.of(2025, 3, 12);
        Mark firstMarkOfTheDay = new Mark(generalUser, LocalTime.of(9, 0, 0), date, true, MarkType.ENTRY);
        when(repositoryMock.getMarksByMarkDate(newMark.getMarkDate()))
                .thenReturn(List.of(firstMarkOfTheDay));

        assertThat(sut.isValidMarkType(newMark)).isEqualTo(result);
    }

    static Stream<Arguments> exitMarksWithoutEntryProvider() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        var date = LocalDate.of(2025, 3, 12);

        return Stream.of(Arguments.of(List.of(),
                new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), true),

                Arguments.of(List.of(),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.ENTRY), false),

                Arguments.of(List.of(
                                new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.ENTRY)
                        ),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), false),

                Arguments.of(List.of(
                                new Mark(user, LocalTime.of(8, 45, 0), date, false, MarkType.ENTRY)
                        ),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), true),

                Arguments.of(List.of(
                            new Mark(user, LocalTime.of(8, 45, 0), date, false, MarkType.ENTRY),
                            new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT)
                        ),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), true));
    }

    @ParameterizedTest(name = "{index} - marks should return {2}")
    @MethodSource("exitMarksWithoutEntryProvider")
    @DisplayName("should not be able to insert an exit mark without an entry mark")
    public void cannotInsertAnExitMarkwithoutAnEntry(List<Mark> markList, Mark newMark, Boolean result) {
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(newMark.getMarkTime(), newMark.getMarkDate()))
                .thenReturn(markList);

        assertThat(sut.isExitMarkWithoutEntry(newMark)).isEqualTo(result);
    }

    static Stream<Arguments> futureMarkProvider() {
        User user = new User("Flaco López", "password", "Flaquito Matador");
        var date = LocalDate.of(2025, 3, 12);

        return Stream.of(Arguments.of(LocalDate.of(2025, 3, 12),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.EXIT), false),

                Arguments.of(LocalDate.of(2025, 3, 11),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.ENTRY), true),

                Arguments.of(LocalDate.of(2025, 3, 12),
                        new Mark(user, LocalTime.of(8, 45, 0), date, true, MarkType.ENTRY), false));
    }

    @ParameterizedTest(name = "{index} - marks should return {2}")
    @MethodSource("futureMarkProvider")
    @DisplayName("should not be able to insert a mark to a future date")
    public void verofyingFutureMark(LocalDate today, Mark mark, Boolean result) {

        assertThat(sut.isFutureMark(today, mark)).isEqualTo(result);
    }
}
