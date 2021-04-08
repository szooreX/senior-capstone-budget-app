package com.example.senior_capstone_budget_app.trackers;

import com.example.senior_capstone_budget_app.data.Categories;

import java.util.Date;

public class SpendLessThanPercent implements Tracker{
    trackerEnum type = trackerEnum.PERCENT;
    int limit = 0;
    Categories cat = Categories.UNCATEGORIZED;
    trackerEnum direction = trackerEnum.LESS;
    Date reminder = null;

    public SpendLessThanPercent (int limit, Categories cat) {
        this.limit = limit;
        this.cat = cat;
    }

    public SpendLessThanPercent(int limit, Categories cat, Date reminder) {
        this.limit = limit;
        this.cat = cat;
        this.reminder = reminder;
    }

    @Override
    public double percentSuccessful() {
        return 0;
    }
}
