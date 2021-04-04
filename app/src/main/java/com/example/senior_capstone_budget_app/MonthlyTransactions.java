package com.example.senior_capstone_budget_app;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class MonthlyTransactions extends AppCompatActivity{
    String[] temp = new String[]{"-100.00,Amazon,1617249600000,0",
            "-60.42,Harris Teeter,1617249600000,4",
            "-7.82,McDonald's,1617336000000,4",
            "-25.68,Shell,1617336000000,3",
            "-450.00,Keystone,1617595200000,1",
            "-45.97,Amazon,1618286400000,5",
            "-200.00,USAA,1618459200000,8",
            "-34.76,Harris Teeter,1618459200000,4",
            "-100.00,IRS,1618718400000,7",
            "-12.32,Walgreens,1618977600000,6",
            "-73.45,Duke Power,1619496000000,2"};

    private Context context;
    private Date currentMonth;
    private Date nextMonth;
    private Timestamp currentTimestamp;
    private Timestamp nextTimestamp;
    private ArrayList<Transaction> transactions;

    private double total;
    private double[] categoryTotals;
    private int[] categoryPercents;

    public MonthlyTransactions() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        currentMonth = cal.getTime();
        currentTimestamp = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, 1);

        nextMonth = cal.getTime();
        nextTimestamp = new Timestamp(cal.getTimeInMillis());

        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.transactions = new ArrayList<Transaction>();
    }

    public MonthlyTransactions(Context c) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        currentMonth = cal.getTime();
        currentTimestamp = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, 1);

        nextMonth = cal.getTime();
        nextTimestamp = new Timestamp(cal.getTimeInMillis());

        this.context = c;
        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.transactions = new ArrayList<Transaction>();
    }

    public void printList(String s){
        String[] a = s.split("\n");
        for (String t:a){

        }
    }


    //!!! Modify for database when ready
    public void loadTransactions(){
        int counter = 0;
        for (String s: temp){
            String[] t = s.split("\\,");
            double amount = Double.parseDouble(t[0]);
            long l = Long.parseLong(t[2]);
            Timestamp time = new Timestamp(l);
            int cat = Integer.parseInt(t[3]);

            if(currentTimestamp.compareTo(time)<= 0 && nextTimestamp.compareTo(time)>0){
                transactions.add(counter, new Transaction(amount, t[1], time, cat));
                counter ++;
            }
        }
    }

    public void loadTransactions2(String input){
        String[] split = input.split("\n");
        int counter = 0;
        for (String s: split){
            s = s.replaceAll("[^\\x00-\\x7F]", "");
            s = s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            s = s.replaceAll("\\p{C}", "");
            String[] t = s.split("\\,");
            double amount = Double.parseDouble(t[0]);
            long l = Long.parseLong(t[2]);
            Timestamp time = new Timestamp(l);
            int cat = Integer.parseInt(t[3]);
            if(currentTimestamp.compareTo(time)<= 0 && nextTimestamp.compareTo(time)>0){
                transactions.add(counter, new Transaction(amount, t[1], time, cat));
                counter ++;
            }
        }
    }

    public void transactionLoop(){
        total = 0;
        double starting;
        double ending;

        for (Transaction t : transactions){
            double a = -1.0 * t.getAmount();
            total += a;
            switch (t.getCategory()) {
                case UNCATEGORIZED:
                    starting = categoryTotals[0];
                    ending = starting + a;
                    categoryTotals[0] = ending;
                    break;
                case RENT:
                    starting = categoryTotals[1];
                    ending = starting + a;
                    categoryTotals[1] = ending;
                    break;
                case UTILITIES:
                    starting = categoryTotals[2];
                    ending = starting + a;
                    categoryTotals[2] = ending;
                    break;
                case TRANSPORTATION:
                    starting = categoryTotals[3];
                    ending = starting + a;
                    categoryTotals[3] = ending;
                    break;
                case HOUSEHOLD:
                    starting = categoryTotals[4];
                    ending = starting + a;
                    categoryTotals[4] = ending;
                    break;
                case PERSONAL:
                    starting = categoryTotals[5];
                    ending = starting + a;
                    categoryTotals[5] = ending;
                    break;
                case MEDICAL:
                    starting = categoryTotals[6];
                    ending = starting + a;
                    categoryTotals[6] = ending;
                    break;
                case DEBT:
                    starting = categoryTotals[7];
                    ending = starting + a;
                    categoryTotals[7] = ending;
                    break;
                case SAVINGS:
                    starting = categoryTotals[8];
                    ending = starting + a;
                    categoryTotals[8] = ending;
                    categoryPercents[8] = (int) ((ending / total) * 100);
                    break;
            }
            for (int k = 0; k < 9; k++){
                double catTotal = categoryTotals[k];
                categoryPercents[k] = (int) ((catTotal/total) * 100);
            }
        }
    }

    public void addTransaction(Transaction t){
        transactions.add(t);
    }

    public void removeTransaction(int index){
        transactions.remove(index);
    }

    //====================================Getters====================================//
    public Date getCurrentMonth() {return currentMonth;}
    public Date getNextMonth() {return nextMonth;}
    public Timestamp getCurrentTimestamp() {return currentTimestamp;}
    public Timestamp getNextTimestamp() {return nextTimestamp;}
    public ArrayList<Transaction> getTransactions() {return transactions;}
    public Transaction getTransaction(int index) {return transactions.get(index);}
    public int getCategoryPercents(int index) {return categoryPercents[index];}

    //====================================Setters====================================//
    public void setCurrentMonth(Date currentMonth) {this.currentMonth = currentMonth;}
    public void setNextMonth(Date nextMonth) {this.nextMonth = nextMonth;}
    public void setCurrentTimestamp(Timestamp currentTimestamp) {this.currentTimestamp = currentTimestamp;}
    public void setNextTimestamp(Timestamp nextTimestamp) {this.nextTimestamp = nextTimestamp;}
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
    public void setMonthlyTransactions(Transaction t, int index) {
        this.transactions.set(index, t);
    }
}
