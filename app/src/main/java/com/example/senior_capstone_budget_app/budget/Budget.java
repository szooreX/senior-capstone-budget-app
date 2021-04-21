package com.example.senior_capstone_budget_app.budget;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.senior_capstone_budget_app.transaction.Transaction;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;

public class Budget {
    private int month;
    private double expectedIncome;
    private double additionalIncome;
    private double[] limits = new double[9];
    private double totalExpenses;
    private int percentTotal;
    private int[] categoryPercents = new int[9];

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Budget() {
        Calendar cal = Calendar.getInstance();
        this.month = cal.MONTH;
        this.expectedIncome = 0.0;
        this.additionalIncome = 0.0;
        this.totalExpenses = 0.0;
        this.percentTotal = 0;
    }

    public Budget(int month, double expectedIncome, double additionalIncome, double[] limits, double totalExpenses) {
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

    public void loadBudget(String input){
        String[] split = input.split("\n");
        for (String s: split) {
            s = s.replaceAll("[^\\x00-\\x7F]", "");
            s = s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            s = s.replaceAll("\\p{C}", "");
        }
        expectedIncome = Double.parseDouble(split[0]);
        additionalIncome = Double.parseDouble(split[1]);
        String[] b = split[2].split(",");
        for(int k = 0; k < b.length; k++){
            limits[k] = Double.parseDouble(b[k]);
        }
        calculateTotal();
    }

    public void calculateTotal(){
        totalExpenses = 0;
        for (Double d: limits){
            totalExpenses += d;
        }
    }

    public void calculatePercentTotal(double total) {
        double lim = 0;
        for (double d:limits){
            lim += d;
        }
        percentTotal = (int)((lim/total)*100);
    }

    @Override
    public String toString() {
        String retVal = "";
        retVal = retVal + expectedIncome + "\n" + additionalIncome + "\n";
        for (double d: limits){
            retVal = retVal + d + ",";
        }
        retVal = retVal.substring(0, retVal.length()-1);
        return retVal;
    }

    //====================================Getters====================================//
    public int getMonth() {return month;}
    public double getExpectedIncome() {return expectedIncome;}
    public double getAdditionalIncome() {return additionalIncome;}
    public double[] getLimits() {return limits;}
    public double getLimit(int index) {return limits[index];}
    public double getTotalExpenses() {return totalExpenses;}
    public int getPercentTotal() {return percentTotal;}
    public int[] getCategoryPercents() {return categoryPercents;}
    public int getCategoryPercent(int index) {return categoryPercents[index];}

    //====================================Setters====================================//
    public void setMonth(int month) {this.month = month;}
    public void setExpectedIncome(double expectedIncome) {this.expectedIncome = expectedIncome;}
    public void setAdditionalIncome(double additionalIncome) {this.additionalIncome = additionalIncome;}
    public void setLimits(double[] limits) {this.limits = limits;}
    public void setLimit(double limit, int index){this.limits[index] = limit;}
    public void setTotalExpenses(double totalExpenses) {this.totalExpenses = totalExpenses;}
}
