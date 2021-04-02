package com.example.senior_capstone_budget_app;

import java.util.Date;

public class SpendLessThanX implements Tracker{
    trackerEnum type = trackerEnum.AMOUNT;
    int limit = 0;
    Categories cat = Categories.UNCATEGORIZED;
    trackerEnum direction = trackerEnum.LESS;
    Date reminder = null;

    public SpendLessThanX(int limit, Categories cat) {
        this.limit = limit;
        this.cat = cat;
    }

    public SpendLessThanX(int limit, Categories cat, Date reminder) {
        this.limit = limit;
        this.cat = cat;
        this.reminder = reminder;
    }

    @Override
    public double percentSuccessful() {
        return 0;
    }
}

