import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.Month;

public class Budget {
    private Month month;
    private double expectedIncome;
    private double additionalIncome;
    private double[] limits = new double[13];
    private double totalExpenses;

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

    //====================================Getters====================================//
    public Month getMonth() {return month;}
    public double getExpectedIncome() {return expectedIncome;}
    public double getAdditionalIncome() {return additionalIncome;}
    public double[] getLimits() {return limits;}
    public double getLimit(int index) {return limits[index];}
    public double getTotalExpenses() {return totalExpenses;}

    //====================================Setters====================================//
    public void setMonth(Month month) {this.month = month;}
    public void setExpectedIncome(double expectedIncome) {this.expectedIncome = expectedIncome;}
    public void setAdditionalIncome(double additionalIncome) {this.additionalIncome = additionalIncome;}
    public void setLimits(double[] limits) {this.limits = limits;}
    public void setLimit(double limit, int index){this.limits[index] = limit;}
    public void setTotalExpenses(double totalExpenses) {this.totalExpenses = totalExpenses;}
}
