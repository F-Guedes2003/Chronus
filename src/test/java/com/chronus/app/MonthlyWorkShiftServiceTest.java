package com.chronus.app;

import com.chronus.app.mark.services.MonthlyWorkShiftService;
import com.chronus.app.user.User;
import com.chronus.app.utils.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class MonthlyWorkShiftServiceTest {
    User user = new User(1,"Aislan","12345","aislan.pepi@outlook.com",1850);
    MonthlyWorkShiftService sut = new MonthlyWorkShiftService(user);

    @Test
    @DisplayName("Should return the salary to user")
    void shouldReturnSalaryToUser(){
        assertThat(sut.calculateSalary()).isEqualTo(new HttpResponse<>(204,"", null));
    }
}