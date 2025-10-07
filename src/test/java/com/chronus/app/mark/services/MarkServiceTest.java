package com.chronus.app.mark.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.chronus.app.MarkType;
import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.mark.services.MarkService;
import com.chronus.app.user.User;
import com.chronus.app.utils.HttpResponse;
import org.junit.jupiter.api.BeforeAll;
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
        LocalTime time = LocalTime.of(8, 25);
        User user = new User("Flaco Lópes", "password", "flacomatador@sep.com");
        Mark mark = new Mark(user, LocalDateTime.of(date, time));
        List<Mark> mockReturn = List.of(new Mark(user, LocalDateTime.of(2025, 3, 12, 7, 25, 0)),
                new Mark(user, LocalDateTime.of(2025, 3, 12, 7, 50, 0)),
                new Mark(user, LocalDateTime.of(2025, 3, 12, 9, 1, 0)));

        when(repositoryMock.save(mark)).thenReturn(mark);
        when(repositoryMock.getMarkByDate(mark)).thenReturn(mockReturn);
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(201, "Mark added with success!", mark));

        verify(repositoryMock, atLeast(1)).save(mark);
    }

    @Test
    @DisplayName("Adding a new mark to a date already marked")
    public void addingANewMarkToAnUnavailableDate() {
        LocalDate date = LocalDate.of(2022, 3, 22);
        LocalTime time = LocalTime.of(8, 25);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        User user = new User("Flaco Lópes", "password", "flacomatador@sep.com");
        Mark mark = new Mark(user, dateTime);

        when(repositoryMock.getMarkByMarkTime(dateTime)).thenReturn(List.of(new Mark(user, dateTime)));
        assertThat(sut.addNewMark(mark)).isEqualTo(new HttpResponse<Mark>(400, "Already exists a mark to this date!", null));
    }

    @Test
    @DisplayName("Editing a inexistent mark for a user")
    public void editingAInexistentMark(){
        LocalDate inexistentDate = LocalDate.of(2022,3,28);
        LocalTime inexistentTime = LocalTime.of(10,30);
        LocalDateTime inexistentMark = LocalDateTime.of(inexistentDate,inexistentTime);
        User user = new User("Bruno Fuchs","raça123","brunofuchs3@sep.com");
        when(repositoryMock.getMarkByMarkTime(inexistentMark)).thenReturn(List.of());
        assertThat(sut.editMark(new Mark(user,inexistentMark))).isEqualTo(new HttpResponse<Mark>(400,"Inexistent mark for this user.",null));
    }

    @Test
    @DisplayName("Editing a valid mark")
    public void editingValidMark(){
        LocalDate date = LocalDate.of(2022,3,26);
        LocalTime time = LocalTime.of(7,59);
        LocalDateTime dateTime = LocalDateTime.of(date,time);
        User user = new User("Bruno Fuchs","raça123","brunofuchs3@sep.com");
        Mark mark = new Mark(user,dateTime);
        when(repositoryMock.getMarkByMarkTime(dateTime)).thenReturn(List.of(mark));
        assertThat(sut.editMark(new Mark(user,dateTime))).isEqualTo(new HttpResponse<Mark>(200,"Mark successfully edited",mark));
    }

    @Test
    @DisplayName("Editing mark with redundant entry type in list")
    public void editingMarkRedundantEntryTypeInList(){
        LocalDate date = LocalDate.of(2022,3,26);
        LocalTime time = LocalTime.of(7,59);
        LocalDateTime dateTime = LocalDateTime.of(date,time);
        User user = new User("Bruno Fuchs","raça123","brunofuchs3@sep.com");
        Mark mark = new Mark(user,dateTime,true, MarkType.ENTRY);
        when(repositoryMock.getMarkByMarkTime(dateTime)).thenReturn(List.of(mark));
        assertThat(sut.editMark(new Mark(user,dateTime,true,MarkType.ENTRY))).isEqualTo(new HttpResponse<Mark>(400,"Already has a Entry Mark",mark));
    }
}
