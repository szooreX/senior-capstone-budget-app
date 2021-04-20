package com.example.senior_capstone_budget_app.goals;

import com.example.senior_capstone_budget_app.trackers.Tracker;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Goals {
    private ArrayList<Goal> goals;

    public Goals() {
        this.goals = new ArrayList<>();
    }

    public void loadGoals(String input){
        int counter = 10;

        String[] split = input.split("\n");
        for (String s: split) {
            ArrayList<Tasks> ts = new ArrayList<>();
            s = s.replaceAll("[^\\x00-\\x7F]", "");
            s = s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            s = s.replaceAll("\\p{C}", "");
            String[] splitGoals = s.split("~");
            String[] Goal = splitGoals[0].split(",");
            int id = Integer.parseInt(Goal[0]);
            long l = Long.parseLong(Goal[3]);
            Date d = new Date(l);

            String[] tasks = splitGoals[1].split(",");
            for (String task: tasks){
                String[] t = task.split("\\|");
                int state = Integer.parseInt(t[1]);
                ts.add(new Tasks(counter, t[0], state));
                counter++;
            }

            goals.add(new Goal(id, Goal[1], Goal[2], d, ts, null, false));
        }
    }

    public void addGoal(String title, String description, Timestamp t){
        int size = goals.size();
        Date d = new Date(t.getTime());
        ArrayList<Tasks> tsks = new ArrayList<>();
        goals.add(new Goal(size+1, title, description, d, tsks, null, false));
    }

    public void removeGoal(int index){
        goals.remove(index);
    }

    @Override
    public String toString() {
        String retString = "";
        for (int i = 0; i < goals.size(); i++){
            int id = i+1;
            Goal g = goals.get(i);
            String tasks = "";
            for (Tasks t: g.getGoalTasks()){
                tasks = tasks + t.toString() + ",";
            }
            if (tasks != ""){
                tasks = tasks.substring(0, tasks.length()-1);
            }
            String strGoal = id + "," + g.toString() + "~" + tasks + "\n";
            retString = retString + strGoal;
        }
        return retString;
    }

    //====================================Getters====================================//
    public ArrayList<Goal> getGoals() {return goals;}
    public Goal getGoal(int index) {return goals.get(index);}

    //====================================Setters====================================//
    public void setGoals(ArrayList<Goal> goals) {this.goals = goals;}
    public void setGoal(Goal g, int index) {this.goals.set(index, g);}
}
