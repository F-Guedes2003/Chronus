package com.chronus.app.mark;

public class MonthlyReport  {
    int hourDailyWorkShift = 10;
    int daysWorkShift = 22;
    double totalWorkingHours = daysWorkShift * hourDailyWorkShift;
    double missingHours = totalWorkingHours;
    double workedHours = 0;
    double salaryReceived = 0;

    public int getHourDailyWorkShift() {
        return hourDailyWorkShift;
    }

    public void setMissingHours(double missingHours) {
        this.missingHours = missingHours;
    }

    public int getDaysWorkShift() {
        return daysWorkShift;
    }

    public double getSalaryReceived() {
        return salaryReceived;
    }

    public void setSalaryReceived(double salaryReceived) {
        this.salaryReceived = salaryReceived;
    }

    public double getMissingHours() {
        return missingHours;
    }

    public double getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public double getWorkedHours() {
        return workedHours;
    }

    public void setWorkedHours(double workedHours) {
        this.workedHours = workedHours;
    }
}
