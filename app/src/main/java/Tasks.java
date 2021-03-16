import java.security.SecureRandom;
import java.util.Date;

public class Tasks {
    private int TaskId;
    private String title;
    private Tracker track;
    private int category;
    private Date reminder;
    private boolean completed;

    public Tasks(int taskId) {
        TaskId = taskId;
        completed = false;
    }

    public Tasks(int taskId, String title, Tracker track, int category, Date reminder, boolean completed) {
        TaskId = taskId;
        this.title = title;
        this.track = track;
        this.category = category;
        this.reminder = reminder;
        this.completed = completed;
    }

    /**
     * Generates a unique TaskID less than 10000
     * @return A unique int taskID
     */
    public int generateID(){
        SecureRandom random = new SecureRandom();
        int i = random.nextInt(10000);

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

    //====================================Getters====================================//
    public int getTaskId() {return TaskId;}
    public String getTitle() {return title;}
    public Tracker getTrack() {return track;}
    public int getCategory() {return category;}
    public Date getReminder() {return reminder;}
    public boolean isCompleted() {return completed;}

    //====================================Setters====================================//
    public void setTaskId(int taskId) {TaskId = taskId;}
    public void setTitle(String title) {this.title = title;}
    public void setTrack(Tracker track) {this.track = track;}
    public void setCategory(int category) {this.category = category;}
    public void setReminder(Date reminder) {this.reminder = reminder;}
    public void setCompleted(boolean completed) {this.completed = completed;}
}
