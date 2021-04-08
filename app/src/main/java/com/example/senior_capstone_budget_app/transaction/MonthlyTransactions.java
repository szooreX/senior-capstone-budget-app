package com.example.senior_capstone_budget_app.transaction;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Timestamp;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MonthlyTransactions extends AppCompatActivity{
    //Example transaction set for uses when testing the class without context passing issues
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

    String[] monthNames = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};

    private Context context;
    private Date currentMonth;
    private Date nextMonth;

    private Timestamp currentTimestamp;
    private Timestamp nextTimestamp;
    private Timestamp timestampMinus1;
    private Timestamp timestampMinus2;
    private Timestamp timestampMinus3;
    private Timestamp timestampMinus4;
    private Timestamp timestampMinus5;

    private ArrayList<Transaction> currentTransactions;

    private double total = 0;
    private double minus1Month = 0;
    private double minus2Month = 0;
    private double minus3Month = 0;
    private double minus4Month = 0;
    private double minus5Month = 0;

    private ArrayList<Double> totals;
    private double[] categoryTotals;
    private int[] categoryPercents;

    /**
     * MonthlyTransaction object constructor with no context passed
     */
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

        cal.add(Calendar.MONTH, -2);
        timestampMinus1 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus2 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus3 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus4 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus5 = new Timestamp(cal.getTimeInMillis());

        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.currentTransactions = new ArrayList<>();
        this.totals = new ArrayList<>();
    }

    /**
     * Monthly Transaction Object constructor for use if context is passed
     * @param c Android application context
     */
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

        cal.add(Calendar.MONTH, -2);
        timestampMinus1 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus2 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus3 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus4 = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -1);
        timestampMinus5 = new Timestamp(cal.getTimeInMillis());

        this.context = c;
        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.currentTransactions = new ArrayList<>();
        this.totals = new ArrayList<>();
    }

    /**
     * Creates a MonthlyTransactions object for use when populating the transactions history view
     * @param date The start date of the month we want history for
     */
    public void MonthlyTransactionsHistory(Date date) {
        this.currentMonth = date;
        currentTimestamp = new Timestamp(currentMonth.getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentMonth);
        cal.add(Calendar.MONTH, 1);

        nextMonth = cal.getTime();
        nextTimestamp = new Timestamp(cal.getTimeInMillis());

        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.currentTransactions = new ArrayList<>();
    }

    /**
     * !!! Modify for database when ready
     * For reading from internal array
     * Deprecated
     */
    public void loadTransactions(){
        int counter = 0;
        for (String s: temp){
            String[] t = s.split(",");
            double amount = Double.parseDouble(t[0]);
            long l = Long.parseLong(t[2]);
            Timestamp time = new Timestamp(l);
            int cat = Integer.parseInt(t[3]);

            if(currentTimestamp.compareTo(time)<= 0 && nextTimestamp.compareTo(time)>0){
                currentTransactions.add(counter, new Transaction(amount, t[1], time, cat));
                counter ++;
            }
        }
    }


    /**
     * For use when reading from a file/database
     * '\n' separates each transaction ',' separates each transaction pieces
     * @param input File/Database input as a string
     */
    public void loadTransactions2(String input){
        String[] split = input.split("\n");
        int currentCounter = 0;


        for (String s: split){
            s = s.replaceAll("[^\\x00-\\x7F]", "");
            s = s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            s = s.replaceAll("\\p{C}", "");
            String[] t = s.split(",");
            double amount = Double.parseDouble(t[0]);
            long l = Long.parseLong(t[2]);
            Timestamp time = new Timestamp(l);
            int cat = Integer.parseInt(t[3]);
            if(currentTimestamp.compareTo(time)<= 0 && nextTimestamp.compareTo(time)>0){
                currentTransactions.add(currentCounter, new Transaction(amount, t[1], time, cat));
                total += -1 * amount;
                currentCounter ++;
            }
            if(timestampMinus1.compareTo(time)<=0 && currentTimestamp.compareTo(time)>0){
                minus1Month += -1 * amount;
            }
            if(timestampMinus2.compareTo(time)<=0 &&  timestampMinus1.compareTo(time)>0){
                minus2Month += -1 * amount;
            }
            if(timestampMinus3.compareTo(time)<=0 &&  timestampMinus2.compareTo(time)>0){
                minus3Month += -1 * amount;
            }
            if(timestampMinus4.compareTo(time)<=0 &&  timestampMinus3.compareTo(time)>0){
                minus4Month += -1 * amount;
            }
            if(timestampMinus5.compareTo(time)<=0 &&  timestampMinus4.compareTo(time)>0){
                minus5Month += -1 * amount;
            }
        }
        totals.add(minus5Month);
        totals.add(minus4Month);
        totals.add(minus3Month);
        totals.add(minus2Month);
        totals.add(minus1Month);
        totals.add(total);
    }

    /**
     * Loop trough the transaction list to calculate the total spent, total spent per category,
     * and the percent spent per category.
     */
    public void transactionLoop(){
        total = 0;
        double starting;
        double ending;

        for (Transaction t : currentTransactions){
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
                case FINANCIAL:
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

    /**
     * Add a single transaction to transactions list
     * @param t the transaction object to be added
     */
    public void addTransaction(Transaction t){
        currentTransactions.add(t);
    }

    /**
     * Remove a specifc transaction from the transactions list
     * @param index the  integer index of the transaction to be removed
     */
    public void removeTransaction(int index){
        currentTransactions.remove(index);
    }

    //====================================Getters====================================//
    public Date getCurrentMonth() {return currentMonth;}
    public Date getNextMonth() {return nextMonth;}
    public Timestamp getCurrentTimestamp() {return currentTimestamp;}
    public Timestamp getNextTimestamp() {return nextTimestamp;}
    public ArrayList<Transaction> getCurrentTransactions() {return currentTransactions;}
    public Transaction getTransaction(int index) {return currentTransactions.get(index);}
    public double getTotal() {return total;}
    public ArrayList<Double> getTotals() {return totals;}
    public int getCategoryPercents(int index) {return categoryPercents[index];}


    //====================================Setters====================================//
    public void setCurrentMonth(Date currentMonth) {this.currentMonth = currentMonth;}
    public void setNextMonth(Date nextMonth) {this.nextMonth = nextMonth;}
    public void setCurrentTimestamp(Timestamp currentTimestamp) {this.currentTimestamp = currentTimestamp;}
    public void setNextTimestamp(Timestamp nextTimestamp) {this.nextTimestamp = nextTimestamp;}
    public void setCurrentTransactions(ArrayList<Transaction> currentTransactions) {
        this.currentTransactions = currentTransactions;
    }
    public void setTotal(double total) {this.total = total;}
    public void setMonthlyTransactions(Transaction t, int index) {
        this.currentTransactions.set(index, t);
    }
}
