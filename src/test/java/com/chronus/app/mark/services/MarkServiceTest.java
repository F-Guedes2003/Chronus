package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.MarkType;
import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.User;
import com.chronus.app.user.UserRepository;
import com.chronus.app.utils.HttpResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MarkServiceTest {
    MarkService sut = new MarkService();
    @Mock
    MarkRepository repositoryMock;
    @Mock
    UserRepository userRepositoryMock;

    @BeforeEach
    public void setup() {
        sut.repository = repositoryMock;
        sut.validator = new MarkValidator(repositoryMock);
        sut.userRepository = userRepositoryMock;
    }

    @Test
    @DisplayName("Adding a mark to a user")
    @Tag("UnitTest")
    @Tag("Functional")
    public void addingANewMarkTest() {
        var date = LocalDate.of(2022, 3, 22);
        var user = new User(1,"Flaco Lópes", "password", "flacomatador@sep.com",1850);
        var mark = new Mark(user, LocalTime.of(8, 25), date, true, MarkType.ENTRY);

        when(userRepositoryMock.findUserById(1L))
                .thenReturn(Optional.of(user));
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(mark.getMarkTime(), mark.getMarkDate()))
                .thenReturn(List.of());
        when(repositoryMock.getMarksByMarkDate(mark.getMarkDate()))
                .thenReturn(List.of());
        when(repositoryMock.save(mark)).
                thenReturn(mark);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(201, "Mark added with success!", mark));

        verify(repositoryMock, atLeast(1)).save(mark);
    }

    @Test
    @DisplayName("Should return 400 when user id is 0")
    @Tag("TDD")
    public void shouldReturn400WhenUserIdIsZero() {
        var date = LocalDate.of(2022, 3, 22);
        var user = new User("Flaco", "password", "flaco@sep.com",1850);
        var mark = new Mark(user, LocalTime.of(8, 25), date, true, MarkType.ENTRY);

        assertThat(sut.addNewMark(mark))
                .isEqualTo(new HttpResponse<>(400, "user is Empty", null));
    }

    @Test
    @DisplayName("Should return 400 when user does not exist in repository")
    @Tag("TDD")
    public void shouldReturn400WhenUserNotFound() {
        // Arrange
        var date = LocalDate.of(2022, 3, 22);
        var user = new User(1, "Flaco", "password", "flaco@sep.com",1850);
        var mark = new Mark(user, LocalTime.of(8, 25), date, true, MarkType.ENTRY);

        when(userRepositoryMock.findUserById(1L))
                .thenReturn(Optional.empty());
        assertThat(sut.editMark(mark.getId(), mark)).isEqualTo(new HttpResponse<>(404, "User is not found!", null));
        assertThat(sut.addNewMark(mark))
                .isEqualTo(new HttpResponse<>(404, "User is not found!", null));
    }


    @Test
    @DisplayName("Adding a new mark to a date already marked")
    @Tag("UnitTest")
    @Tag("TDD")
    public void addingANewMarkToAnUnavailableDate() {
        var date = LocalDate.of(2022, 3, 22);
        var time = LocalTime.of(8, 25);
        var user = new User(1,"Flaco Lópes", "password", "flacomatador@sep.com",1850);
        var mark = new Mark(user, time, date);

        when(userRepositoryMock.findUserById(1L)).thenReturn(Optional.of(user));
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(mark.getMarkTime(), mark.getMarkDate())).thenReturn(List.of(new Mark(user, time, date)));
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "Already exists a mark to this date!", null));
    }

    @Test
    @DisplayName("Editing a inexistent mark for a user")
    @Tag("UnitTest")
    @Tag("TDD")
    public void editingAInexistentMark() {
        User user = new User(1,"Bruno Fuchs", "raça123", "brunofuchs3@sep.com",1850);
        Mark editedMark = new Mark(user, LocalTime.of(12, 0), LocalDate.of(2025, 3, 3));
        when(userRepositoryMock.findUserById(1L)).thenReturn(Optional.of(user));
        when(repositoryMock.findById(editedMark.getId())).thenReturn(null);
        assertThat(sut.editMark(editedMark.getId(),editedMark)).isEqualTo(new HttpResponse<Mark>(404, "Inexistent mark for this user.", null));
    }

    @Test
    @DisplayName("Editing a valid mark")
    @Tag("UnitTest")
    @Tag("Functional")
    public void editingValidMark() {
        LocalDate date = LocalDate.of(2022, 3, 26);
        User user = new User(1,"Bruno Fuchs", "raça123", "brunofuchs3@sep.com",1850);
        Mark markEdit = new Mark(user, LocalTime.of(8, 0), date, true, MarkType.ENTRY);
        when(userRepositoryMock.findUserById(1L)).thenReturn(Optional.of(user));
        when(repositoryMock.existsById(markEdit.getId())).thenReturn(true);
        when(repositoryMock.findById(markEdit.getId())).thenReturn(Optional.of(markEdit));
        assertThat(sut.editMark(markEdit.getId(),markEdit)).isEqualTo(new HttpResponse<Mark>(200, "Mark successfully edited", markEdit));
    }

    @Test
    @DisplayName("Should return the working hours")
    @Tag("UnitTest")
    @Tag("TDD")
    public void shouldReturnTheWorkingHours(){
        LocalDate date = LocalDate.of(2025,1,6);
        User user = new User("Aislan","teste123","aislan@teste.com",1850);
        Mark entry = new Mark(user,LocalTime.of(8,0),date,true,MarkType.ENTRY);
        Mark exit = new Mark(user,LocalTime.of(18,0),date,true,MarkType.EXIT);
        assertThat(sut.calculateWorkShift(List.of(entry,exit))).isEqualTo(Duration.ofHours(10));
    }

    @Test
    @DisplayName("Should return real user salary in the month")
    public void shouldReturnRealUserSalaryInMonth(){
        User user = new User("Aislan","teste123","aislan@teste.com",1850);
        LocalDate date = LocalDate.of(2025,2,6);
        LocalDate anotherDate = LocalDate.of(2025,2,7);

        List<Mark> marks = new ArrayList<>();

        marks.add(new Mark(user,LocalTime.of(8,0),date,true,MarkType.ENTRY));
        marks.add(new Mark(user,LocalTime.of(18,0),date,true,MarkType.EXIT));

        marks.add(new Mark(user,LocalTime.of(8,0),anotherDate,true,MarkType.ENTRY));
        marks.add(new Mark(user,LocalTime.of(18,0),anotherDate,true,MarkType.EXIT));

        when(repositoryMock.findAllByYearAndMonth(2025,2)).thenReturn(marks);
        assertThat(sut.calculateSalary(date)).isEqualTo(new HttpResponse<Double>(200,"User's salary returned with success",84.09));
    }


    @Test
    @DisplayName("Should return 400 if Mark time is null")
    @Tag("StructuralTest")
    @Tag("UnitTest")
    public void shouldReturn400IfMarkTimeIsNull(){
        User user = new User(1,"Aislan","teste123","aislan@teste.com",1850);
        Mark mark = new Mark(user,null,LocalDate.of(2025,1,1),true,MarkType.ENTRY);
        when(userRepositoryMock.findUserById(1L)).thenReturn(Optional.of(user));
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "Mark time field must not be empty!", null));
    }

    @Test
    @DisplayName("Should return 400 if user is null")
    @Tag("StructuralTest")
    @Tag("UnitTest")
    public void shouldReturn400IfUserIsNull(){
        Mark mark = new Mark(null,LocalTime.of(8,0),LocalDate.of(2025,1,1),true,MarkType.ENTRY);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "User field must not be empty!", null));
    }

    @Test
    @DisplayName("Should return 204 if mark is deleted")
    @Tag("SructuralTest")
    @Tag("UnitTest")
    public void shouldReturn204IfMarkIsDeleted(){
        User user = new User("Aislan","teste123","aislan@teste.com",1850);
        Mark mark = new Mark(user,LocalTime.of(8,0),LocalDate.of(2025,1,1),true,MarkType.ENTRY);
        assertThat(sut.deleteById(mark.getId())).isEqualTo(new HttpResponse<>(204, "", null));
    }

    @Nested
    public class GetMarks {
        @Test
        @DisplayName("Should return all marks from a given month and year")
        @Tag("UnitTest")
        @Tag("Functional")
        public void shouldReturnAllMarksFromGivenMonthAndYear() {
            var date = LocalDate.of(2025, 11, 1);
            var user = new User(1, "Flaco", "password", "flaco@sep.com",1850);
            var mark1 = new Mark(user, LocalTime.of(8, 0), LocalDate.of(2025, 11, 2), true, MarkType.ENTRY);
            var mark2 = new Mark(user, LocalTime.of(18, 0), LocalDate.of(2025, 11, 2), true, MarkType.EXIT);

            when(repositoryMock.findAllByYearAndMonth(2025, 11)).thenReturn(List.of(mark1, mark2));

            var response = sut.getMarksByMonthAndYear(date);

            assertThat(response).isEqualTo(new HttpResponse<>(200, "Marks fetched successfully", List.of(mark1, mark2)));
            verify(repositoryMock, times(1)).findAllByYearAndMonth(2025, 11);
        }

        @Test
        @DisplayName("Should return empty list if there are no marks in the month")
        @Tag("UnitTest")
        @Tag("TDD")
        public void shouldReturnEmptyListIfNoMarksFoundForMonth() {
            var date = LocalDate.of(2025, 11, 1);

            when(repositoryMock.findAllByYearAndMonth(2025, 11)).thenReturn(List.of());

            var response = sut.getMarksByMonthAndYear(date);

            assertThat(response).isEqualTo(new HttpResponse<>(200, "There is no mark for this month", List.of()));
            verify(repositoryMock, times(1)).findAllByYearAndMonth(2025, 11);
        }

        @Test
        @DisplayName("Should return 400 if date is null")
        @Tag("UnitTest")
        @Tag("StructuralTest")
        public void shouldReturn400IfDateIsNull() {
            var response = sut.getMarksByMonthAndYear(null);

            assertThat(response).isEqualTo(new HttpResponse<>(400, "Date must not be null", List.of()));
            verify(repositoryMock, never()).findAllByYearAndMonth(anyInt(), anyInt());
        }

    }
}
