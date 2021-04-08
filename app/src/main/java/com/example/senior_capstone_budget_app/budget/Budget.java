package com.example.senior_capstone_budget_app.budget;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.senior_capstone_budget_app.transaction.Transaction;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class Budget {
    private Month month;
    private double expectedIncome;
    private double additionalIncome;
    private double[] limits = new double[7];
    private double totalExpenses;
    private int percentTotal;
    private int[] categoryPercents = new int[7];

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Budget() {
        this.month = LocalDate.now().getMonth();
    }

    public Budget(Month month, double expectedIncome, double additionalIncome, double[] limits, double totalExpenses) {
        this.month = month;
        this.expectedIncome = expectedIncome;
        this.additionalIncome = additionalIncome;
        this.limits = limits;
        this.totalExpenses = totalExpenses;
    }

    public void calculateCategoryPercent(double[] categoryTotals){
        for (int i = 0; i < categoryPercents.length; i++){
            categoryPercents[i] = (int)((categoryTotals[i]/limits[i]) * 100);
        }
    }

    public void calculatePercentTotal(double total) {
        double lim = 0;
        for (double d:limits){
            lim += d;
        }
        percentTotal = (int)((lim/total)*100);
    }

    //====================================Getters====================================//
    public Month getMonth() {return month;}
    public double getExpectedIncome() {return expectedIncome;}
    public double getAdditionalIncome() {return additionalIncome;}
    public double[] getLimits() {return limits;}
    public double getLimit(int index) {return limits[index];}
    public double getTotalExpenses() {return totalExpenses;}
    public int getPercentTotal() {return percentTotal;}
    public int[] getCategoryPercents() {return categoryPercents;}
    public int getCategoryPercent(int index) {return categoryPercents[index];}

    //====================================Setters====================================//
    public void setMonth(Month month) {this.month = month;}
    public void setExpectedIncome(double expectedIncome) {this.expectedIncome = expectedIncome;}
    public void setAdditionalIncome(double additionalIncome) {this.additionalIncome = additionalIncome;}
    public void setLimits(double[] limits) {this.limits = limits;}
    public void setLimit(double limit, int index){this.limits[index] = limit;}
    public void setTotalExpenses(double totalExpenses) {this.totalExpenses = totalExpenses;}
}
