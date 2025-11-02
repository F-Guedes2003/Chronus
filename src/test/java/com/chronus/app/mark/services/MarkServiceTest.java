package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.MarkType;
import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.User;
import com.chronus.app.utils.HttpResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MarkServiceTest {
    MarkService sut = new MarkService();
    @Mock
    MarkRepository repositoryMock;

    @BeforeEach
    public void setup() {
        sut.repository = repositoryMock;
        sut.validator = new MarkValidator(repositoryMock);
    }

    @Test
    @DisplayName("Adding a mark to a user")
    @Tag("UnitTest")
    @Tag("Functional")
    public void addingANewMarkTest() {
        LocalDate date = LocalDate.of(2022, 3, 22);
        User user = new User("Flaco Lópes", "password", "flacomatador@sep.com");
        Mark mark = new Mark(user, LocalTime.of(8, 25), date, true, MarkType.ENTRY);

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
    @DisplayName("Adding a new mark to a date already marked")
    @Tag("UnitTest")
    @Tag("TDD")
    public void addingANewMarkToAnUnavailableDate() {
        LocalDate date = LocalDate.of(2022, 3, 22);
        LocalTime time = LocalTime.of(8, 25);
        User user = new User("Flaco Lópes", "password", "flacomatador@sep.com");
        Mark mark = new Mark(user, time, date);
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(mark.getMarkTime(), mark.getMarkDate())).thenReturn(List.of(new Mark(user, time, date)));
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "Already exists a mark to this date!", null));
    }

    @Test
    @DisplayName("Editing a inexistent mark for a user")
    @Tag("UnitTest")
    @Tag("TDD")
    public void editingAInexistentMark() {
        User user = new User("Bruno Fuchs", "raça123", "brunofuchs3@sep.com");
        Mark editedMark = new Mark(user, LocalTime.of(12, 0), LocalDate.of(2025, 3, 3));
        when(repositoryMock.getMarkById(editedMark.getId())).thenReturn(null);
        assertThat(sut.editMark(editedMark)).isEqualTo(new HttpResponse<Mark>(404, "Inexistent mark for this user.", null));
    }

    @Test
    @DisplayName("Editing a valid mark")
    @Tag("UnitTest")
    @Tag("Functional")
    public void editingValidMark() {
        LocalDate date = LocalDate.of(2022, 3, 26);
        User user = new User("Bruno Fuchs", "raça123", "brunofuchs3@sep.com");
        Mark markEdit = new Mark(user, LocalTime.of(8, 0), date, true, MarkType.ENTRY);
        when(repositoryMock.findMarkById(markEdit.getId())).thenReturn(true);
        when(repositoryMock.getMarkById(markEdit.getId())).thenReturn(markEdit);
        assertThat(sut.editMark(markEdit)).isEqualTo(new HttpResponse<Mark>(200, "Mark successfully edited", markEdit));
    }

    @ParameterizedTest
    @EnumSource(value = MarkType.class, names = {"ENTRY", "EXIT"})
    @DisplayName("Editing mark with redundant mark type in list")
    @Tag("UnitTest")
    @Tag("TDD")
    public void editingMarkRedundantMarkTypeInList(MarkType mType) {
        LocalDate date = LocalDate.of(2022, 3, 26);
        LocalTime time = LocalTime.of(7, 59);
        User user = new User("Bruno Fuchs", "raça123", "brunofuchs3@sep.com");
        Mark editedMark = new Mark(user, time, date, true, mType);
        when(repositoryMock.findMarkById(editedMark.getId())).thenReturn(true);
        when(repositoryMock.getMarkById(editedMark.getId())).thenReturn(editedMark);
        when(repositoryMock.existsByTypeAndDate(mType,date)).thenReturn(true);
        assertThat(sut.editMark(editedMark)).isEqualTo(new HttpResponse<Mark>(400, "Already has the mark type for this day", null));
    }

    @Test
    @DisplayName("Should return the working hours")
    @Tag("UnitTest")
    @Tag("TDD")
    public void shouldReturnTheWorkingHours(){
        LocalDate date = LocalDate.of(2025,1,6);
        User user = new User("Aislan","teste123","aislan@teste.com");
        Mark entry = new Mark(user,LocalTime.of(8,0),date,true,MarkType.ENTRY);
        Mark exit = new Mark(user,LocalTime.of(18,0),date,true,MarkType.EXIT);
        assertThat(sut.calculateWorkShift(List.of(entry,exit))).isEqualTo(Duration.ofHours(10));
    }

    @Test
    @DisplayName("Should return 400 if Mark time is null")
    @Tag("StructuralTest")
    @Tag("UnitTest")
    public void shouldReturn400IfMarkTimeIsNull(){
        User user = new User("Aislan","teste123","aislan@teste.com");
        Mark mark = new Mark(user,null,LocalDate.of(2025,1,1),true,MarkType.ENTRY);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "Mark time field must not be empty!", null));
        assertThat(sut.editMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "Mark time field must not be empty!", null));
    }

    @Test
    @DisplayName("Should return 400 if user is null")
    @Tag("StructuralTest")
    @Tag("UnitTest")
    public void shouldReturn400IfUserIsNull(){
        Mark mark = new Mark(null,LocalTime.of(8,0),LocalDate.of(2025,1,1),true,MarkType.ENTRY);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "User field must not be empty!", null));
        assertThat(sut.editMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "User field must not be empty!", null));
    }

    @Test
    @DisplayName("Should return 201 if exit mark is added without a entry mark")
    @Tag("MutationTest")
    @Tag("UnitTest")
    public void shouldReturn201IfExitMarkAddedWithoutEntryMark(){
        User user = new User("Aislan","teste123","aislan@teste.com");
        Mark mark = new Mark(user,LocalTime.of(18,0),LocalDate.of(2025,1,1),true,MarkType.EXIT);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(201, "Mark added with success, but there is needed to add an entry mark!", mark));
    }
}
