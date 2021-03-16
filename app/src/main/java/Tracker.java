public interface Tracker {
    enum trackerEnum {
        DEFAULT(0),
        PERCENT(1),
        AMOUNT(2),
        GREATER(3),
        LESS(4);
        private int value;

        private trackerEnum(int value){
            this.value = value;
        }

        public int getVal() {
            return this.value;
        }
    }

    public double percentSuccessful();
}


