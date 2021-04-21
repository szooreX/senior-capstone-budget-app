package com.example.senior_capstone_budget_app.goals;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Goal {
    private int goalId;
    private String title;
    private String description;
    private Date deadline;
    private ArrayList<Tasks> goalTasks;
    private Date[] reminders;
    private boolean recurring;
    private int completion = 0;
    private int percent;

    public Goal(int goalId) {
        this.goalId = generateID();
    }

    public Goal(int goalId, String title, String description, Date deadline, ArrayList<Tasks> goalTasks, Date[] reminders, boolean recurring) {
        this.goalId = goalId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.goalTasks = goalTasks;
        this.reminders = reminders;
        this.recurring = recurring;
        this.percent = 0;
        calculatePercent();
    }

    public int calculateDays(){
        Calendar cal = Calendar.getInstance();
        long start = cal.getTimeInMillis();
        long end = deadline.getTime();

        return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(end-start));
    }

    public void calculatePercent(){
        completion = 0;
        for(Tasks t: goalTasks){
            if (t.isCompleted()){
                completion++;
            }
        }
        this.percent = (int) ((completion/((double) goalTasks.size()))*100);
    }

    public void addTask(String  title){
        int size = goalTasks.size();
        goalTasks.add(new Tasks(size+1, title, -1));
        calculatePercent();
    }
    public void removeTask(int index){
        goalTasks.remove(index);
        calculatePercent();
    }

    /**
     * Generates a unique GoalID less than 100000
     * @return A unique int GoalID
     */
    public int generateID(){
        SecureRandom random = new SecureRandom();
        int i = random.nextInt(100000);

        if(checkUnique(i)) {return i;}
        else{return generateID();}
    }

    /**
     * Checks the database to see if a task with the generated ID already exists
     * @param i Integer input to be checked
     * @return True if unique false if not
     */
    private boolean checkUnique(int i){
        //Write code to check goals database for i
        return true;
    }

    @Override
    public String toString() {
        return title + "," + description + "," + deadline.getTime();
    }

    //====================================Getters====================================//
    public int getGoalId() {return goalId;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public Date getDeadline() {return deadline;}
    public ArrayList<Tasks> getGoalTasks() {return goalTasks;}
    public Tasks getGoalTask(int index) {return goalTasks.get(index);}
    public Date[] getReminders() {return reminders;}
    public Date getReminder(int index) {return reminders[index];}
    public boolean isRecurring() {return recurring;}
    public int getPercent() {return percent;}

    //====================================Setters====================================//
    public void setGoalId(int goalId) {this.goalId = goalId;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setDeadline(Date deadline) {this.deadline = deadline;}
    public void setGoalTasks(ArrayList<Tasks> goalTasks) {this.goalTasks = goalTasks;}
    public void setGoalTask(Tasks goalTask, int index) {this.goalTasks.set(index, goalTask);}
    public void setReminders(Date[] reminders) {this.reminders = reminders;}
    public void setReminder(Date reminder, int index) {this.reminders[index] = reminder;}
    public void setRecurring(boolean recurring) {this.recurring = recurring;}
}
