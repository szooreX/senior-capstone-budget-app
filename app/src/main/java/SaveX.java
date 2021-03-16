import java.util.Date;

public class SaveX implements Tracker{
    trackerEnum type = trackerEnum.AMOUNT;
    int limit = 0;
    Categories cat = Categories.UNCATEGORIZED;
    trackerEnum direction = trackerEnum.GREATER;
    Date reminder = null;

    public SaveX(int limit, Categories cat) {
        this.limit = limit;
        this.cat = cat;
    }

    public SaveX(int limit, Categories cat, Date reminder) {
        this.limit = limit;
        this.cat = cat;
        this.reminder = reminder;
    }

    @Override
    public double percentSuccessful() {
        return 0;
    }
}

