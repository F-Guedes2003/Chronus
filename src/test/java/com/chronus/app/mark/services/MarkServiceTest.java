package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.MarkType;
import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.mark.services.MarkService;
import com.chronus.app.user.User;
import com.chronus.app.utils.HttpResponse;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MarkServiceTest {
    MarkService sut = new MarkService();
    @Mock MarkRepository repositoryMock;

    @BeforeEach
    public void setup() {
        sut.repository = repositoryMock;
        sut.validator = new MarkValidator();
        sut.validator.repository = repositoryMock;
    }

    @Test
    @DisplayName("Adding a mark to a user")
    public void addingANewMarkTest() {
        LocalDate date = LocalDate.of(2022, 3, 22);
        User user = new User("Flaco Lópes", "password", "flacomatador@sep.com");
        Mark mark = new Mark(user, LocalTime.of(8, 25), date);

        when(repositoryMock.getMarkByMarkTimeAndMarkDate(mark.getMarkTime(), mark.getMarkDate())).thenReturn(List.of());
        when(repositoryMock.save(mark)).thenReturn(mark);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(201, "Mark added with success!", mark));

        verify(repositoryMock, atLeast(1)).save(mark);
    }

    @Test
    @DisplayName("Adding a new mark to a date already marked")
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
    public void editingAInexistentMark(){
        LocalDate inexistentDate = LocalDate.of(2022,3,28);
        LocalTime inexistentTime = LocalTime.of(10,30);
        User user = new User("Bruno Fuchs","raça123","brunofuchs3@sep.com");
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(inexistentTime,inexistentDate)).thenReturn(List.of());
        assertThat(sut.editMark(new Mark(user,inexistentTime,inexistentDate))).isEqualTo(new HttpResponse<Mark>(400,"Inexistent mark for this user.",null));
    }

    @Test
    @DisplayName("Editing a valid mark")
    public void editingValidMark(){
        LocalDate date = LocalDate.of(2022,3,26);
        LocalTime time = LocalTime.of(7,59);
        User user = new User("Bruno Fuchs","raça123","brunofuchs3@sep.com");
        Mark mark = new Mark(user,time,date,true,MarkType.ENTRY);
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(time,date)).thenReturn(List.of(mark));
        Mark markEdit = new Mark(user,LocalTime.of(12,0),date,true,MarkType.EXIT);
        assertThat(sut.editMark(mark)).isEqualTo(new HttpResponse<Mark>(200,"Mark successfully edited",mark));
    }

    @ParameterizedTest
    @EnumSource(value = MarkType.class,names = {"ENTRY","EXIT"})
    @DisplayName("Editing mark with redundant mark type in list")
    public void editingMarkRedundantMarkTypeInList(MarkType mType){
        LocalDate date = LocalDate.of(2022,3,26);
        LocalTime time = LocalTime.of(7,59);
        User user = new User("Bruno Fuchs","raça123","brunofuchs3@sep.com");
        Mark mark = new Mark(user,time,date,true, mType);
        when(repositoryMock.getMarkByMarkTimeAndMarkDate(time,date)).thenReturn(List.of(mark));
        when(repositoryMock.existsByTypeAndDate(mType, date)).thenReturn(true);
        assertThat(sut.editMark(mark)).isEqualTo(new HttpResponse<Mark>(400,"Already has the mark type for this day",null));
    }
}
