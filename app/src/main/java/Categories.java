public enum Categories {
    UNCATEGORIZED(0),
    RENT(1),
    UTILITIES(2),
    TRANSPORTATION(3),
    FOOD(4),
    CLOTHING(5),
    MEDICAL(6),
    SUPPLIES(7),
    PERSONAL(8),
    DEBT(9),
    SAVINGS(10),
    OTHER(11),
    DISCRETIONARY(12);


    private int value;

    private Categories(int value){
        this.value = value;
    }

    public int getVal() {
        return this.value;
    }
}
