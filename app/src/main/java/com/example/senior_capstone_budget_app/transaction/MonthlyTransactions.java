package com.example.senior_capstone_budget_app.transaction;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import com.example.senior_capstone_budget_app.data.database.DataStoreAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MonthlyTransactions extends AppCompatActivity{
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
    private ArrayList<Transaction> allTransactions;

    private double total = 0;
    private double minus1Month = 0;
    private double minus2Month = 0;
    private double minus3Month = 0;
    private double minus4Month = 0;
    private double minus5Month = 0;
    private double monthlyAvg = 0;
    private int percentOfAvg = 0;

    private ArrayList<Double> totals;
    private ArrayList<String> history;
    private double[] categoryTotals;
    private int[] categoryPercents;
    private DataStoreAdapter arvioDatabase;

    /**
     * MonthlyTransaction object constructor with no context passed
     */
    public MonthlyTransactions() {
        this.history = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        currentMonth = cal.getTime();
        int now = cal.MONTH;
        currentTimestamp = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now + 1+12)%12]);

        cal.add(Calendar.MONTH, 1);
        nextMonth = cal.getTime();
        nextTimestamp = new Timestamp(cal.getTimeInMillis());

        cal.add(Calendar.MONTH, -2);
        timestampMinus1 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now +12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus2 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 1+12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus3 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 2+12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus4 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 3+12)%12]);

        cal.add(Calendar.MONTH, -1);
        timestampMinus5 = new Timestamp(cal.getTimeInMillis());
        history.add(monthNames[(now - 4+12)%12]);

        this.categoryTotals = new double[9];
        this.categoryPercents = new int[9];
        this.currentTransactions = new ArrayList<>();
        this.totals = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            totals.add(0.0);
        }
        this.allTransactions = new ArrayList<>();
        this.arvioDatabase = new DataStoreAdapter();
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
     * For use when reading from a file/database
     * '\n' separates each transaction ',' separates each transaction pieces
     * @param input File/Database input as a string
     */
    public void loadTransactions2(String input){
        String[] split = input.split("\n");
        int currentCounter = 0;
        ArrayList<Transaction> oldTransactions = new ArrayList<>();

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
                total += amount;
                currentCounter ++;
            }
            if(timestampMinus1.compareTo(time)<=0 && currentTimestamp.compareTo(time)>0){
                minus1Month += amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus2.compareTo(time)<=0 &&  timestampMinus1.compareTo(time)>0){
                minus2Month += amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus3.compareTo(time)<=0 &&  timestampMinus2.compareTo(time)>0){
                minus3Month += amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus4.compareTo(time)<=0 &&  timestampMinus3.compareTo(time)>0){
                minus4Month += amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
            if(timestampMinus5.compareTo(time)<=0 &&  timestampMinus4.compareTo(time)>0){
                minus5Month += amount;
                oldTransactions.add(new Transaction(amount, t[1], time, cat));
            }
        }
        allTransactions.addAll(currentTransactions);
        allTransactions.addAll(oldTransactions);

        totals.add(0,total);
        totals.add(1,minus1Month);
        totals.add(2,minus2Month);
        totals.add(3,minus3Month);
        totals.add(4,minus4Month);
        totals.add(5,minus5Month);

        calculateAvg();

        //loadFromDatabase();
    }

    private void calculateAvg() {
        double totalSpending = 0;
        int count = 0;

        for (double d: totals){
            totalSpending += d;
            if (d != 0){
                count++;
            }
        }
        monthlyAvg = totalSpending/count;
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
            double a = t.getAmount();
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
            calculatePercents();
        }
    }

    public void categorizeTransaction(Transaction t){
        double starting;
        double ending;
        double a = 0.0;

        a = t.getAmount();


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
        calculatePercents();
    }

    /**
     * Add a single transaction to transactions list
     * @param t the transaction object to be added
     */
    public void addTransaction(double d, String payee, Timestamp t, int c){
        Transaction trans = new Transaction(d, payee, t, c);

        if(currentTimestamp.compareTo(t)<= 0 && nextTimestamp.compareTo(t)>0){
            currentTransactions.add(trans);
            total += d;
            totals.set(0,total);
        }
        if(timestampMinus1.compareTo(t)<=0 && currentTimestamp.compareTo(t)>0){
            minus1Month = totals.get(1);
            minus1Month += d;
            totals.set(1,minus1Month);
        }
        if(timestampMinus2.compareTo(t)<=0 &&  timestampMinus1.compareTo(t)>0){
            minus2Month = totals.get(2);
            minus2Month += d;
            totals.set(2,minus2Month);
        }
        if(timestampMinus3.compareTo(t)<=0 &&  timestampMinus2.compareTo(t)>0){
            minus3Month = totals.get(3);
            minus3Month += d;
            totals.set(3,minus3Month);
        }
        if(timestampMinus4.compareTo(t)<=0 &&  timestampMinus3.compareTo(t)>0){
            minus4Month = totals.get(4);
            minus4Month += d;

            totals.set(4,minus4Month);
        }
        if(timestampMinus5.compareTo(t)<=0 &&  timestampMinus4.compareTo(t)>0){
            minus5Month = totals.get(5);
            minus5Month += d;
            totals.set(5,minus5Month);
        }
        categorizeTransaction(trans);
        allTransactions.add(trans);
    }

    /**
     * Remove a specifc transaction from the transactions list
     * @param index the  integer index of the transaction to be removed
     */
    public void removeTransaction(int index){
        Transaction t = currentTransactions.get(index);
        double a = t.getAmount();
        int cat = t.getCategory().getVal();
        currentTransactions.remove(index);
        allTransactions.remove(index);

        total -= a;
        categoryTotals[cat] -= a;

        totals.set(0, total);
        calculatePercents();
    }

    public void saveTransactions(String user){
        String filename = user + "transactions";
        String data = this.toString();

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String readTransactions(String user){
        String filename = user + "transactions";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader((new InputStreamReader(
                    context.openFileInput(filename))));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    private void calculatePercents(){
        for (int k = 0; k < 9; k++){
            double catTotal = categoryTotals[k];
            categoryPercents[k] = (int) ((catTotal/total) * 100);
        }
        percentOfAvg = (int) ((total/monthlyAvg)*100);
    }

    @Override
    public String toString() {
        String transactions = "";

        for (Transaction t: allTransactions){
            transactions = transactions + t.toString() + "\n";
        }

        return transactions;
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
    public int getLength(){return currentTransactions.size();}
    public String getHistory(int index) {return history.get(index);}
    public int getPercentOfAvg() {return percentOfAvg;}

    //====================================Setters====================================//
    public void setContext(Context context){this.context = context;}
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
