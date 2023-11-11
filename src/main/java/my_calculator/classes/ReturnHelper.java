package my_calculator.classes;

public class ReturnHelper {
    private final String value;
    private final int index;

    public ReturnHelper(String val, int i) {
        this.value = val;
        this.index = i;
    }

    public String getVal() { return value; }
    public int getIndex() { return index; }
}
