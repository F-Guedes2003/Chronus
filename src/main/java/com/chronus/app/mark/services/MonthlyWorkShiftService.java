package com.chronus.app.mark.services;

import com.chronus.app.user.User;
import com.chronus.app.utils.HttpResponse;

public class MonthlyWorkShiftService {
    private final User user;
    private final int workingDays = 22;
    private double salaryToReceive;
    private double workingHours;
    private double extraHours;
    private double debtWorkingHours;

    public MonthlyWorkShiftService(User user) {
        this.user = user;
    }

    public HttpResponse<MonthlyWorkShiftService> calculateSalary(){
    }
}
