package com.example.senior_capstone_budget_app.goals;

import com.example.senior_capstone_budget_app.trackers.Tracker;

import java.util.Date;

public class Goals {
    private Goal[] goals;

    public Goals() {
        this.goals = new Goal[3];
    }

    public void loadGoals(String input){
        int index = 0;
        int counter = 10;

        String[] split = input.split("\n");
        for (String s: split) {
            s = s.replaceAll("[^\\x00-\\x7F]", "");
            s = s.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            s = s.replaceAll("\\p{C}", "");

            String[] t = s.split(",");
            int id = Integer.parseInt(t[0]);
            long l = Long.parseLong(t[3]);
            Date d = new Date(l);
            String[] t1 = t[4].split("\\|");
            int state1 = Integer.parseInt(t1[1]);
            Tasks track1 = new Tasks(counter, t1[0], state1);
            counter++;
            String[] t2 = t[5].split("\\|");
            int state2 = Integer.parseInt(t2[1]);
            Tasks track2 = new Tasks(counter, t2[0], state2);
            counter++;
            String[] t3 = t[6].split("\\|");
            int state3 = Integer.parseInt(t3[1]);
            Tasks track3 = new Tasks(counter, t3[0], state3);
            counter++;

            Tasks[] tsks = {track1, track2, track3};

            goals[index] = new Goal(id, t[1], t[2], d, tsks, null, false);
            index++;
        }
    }

    public int addGoal(Goal g){
        boolean set =false;
        for(int i = 0; i < 3; i++){
            if (goals[i] == null){
                goals[i] = g;
                set = true;
            }
            if (!set){
                return -1;
            }
        }
        return 1;
    }

    public void removeGoal(int index){
        goals[index] = null;
        for(int i = index;  i < 2; i++){
            if(goals[i] == null && goals[i+1] != null){
                goals[i] = goals[i+1];
            }
        }
    }

    //====================================Getters====================================//
    public Goal[] getGoals() {return goals;}
    public Goal getGoal(int index) {return goals[index];}

    //====================================Setters====================================//
    public void setGoals(Goal[] goals) {this.goals = goals;}
    public void setGoal(Goal g, int index) {this.goals[index] = g;}
}
