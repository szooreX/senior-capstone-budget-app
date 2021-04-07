package com.example.senior_capstone_budget_app.goals;

public class Goals {
    private Goal[] goals;

    public Goals(Goal[] goals) {
        this.goals = goals;
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
