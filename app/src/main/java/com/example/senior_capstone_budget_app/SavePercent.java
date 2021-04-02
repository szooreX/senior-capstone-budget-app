package com.example.senior_capstone_budget_app;

import java.util.Date;

public class SavePercent implements Tracker{
    trackerEnum type = trackerEnum.PERCENT;
    int limit = 0;
    Categories cat = Categories.UNCATEGORIZED;
    trackerEnum direction = trackerEnum.GREATER;
    Date reminder = null;

    public SavePercent (int limit, Categories cat) {
        this.limit = limit;
        this.cat = cat;
    }

    public SavePercent(int limit, Categories cat, Date reminder) {
        this.limit = limit;
        this.cat = cat;
        this.reminder = reminder;
    }

    @Override
    public double percentSuccessful() {
        return 0;
    }
}

