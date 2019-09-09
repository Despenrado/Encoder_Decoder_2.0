import java.util.ArrayList;
import java.util.LinkedList;

public class Data {
    private LinkedList<ArrayList<Boolean>> signal;
    private int pollution;
    private int numberOfTests;
    private int capacity;

    public Data(LinkedList<ArrayList<Boolean>> signal, int pollution, int numberOfTests, int capacity){
        this.signal = signal;
        this.pollution = pollution;
        this.capacity = capacity;
        this.numberOfTests = numberOfTests;
    }

    public Data(){
        this.signal = new LinkedList<ArrayList<Boolean>>();
    };

    public LinkedList<ArrayList<Boolean>> getSignal() {
        return signal;
    }

    public void setSignal(LinkedList<ArrayList<Boolean>> signal) {
        this.signal = signal;
    }

    public int getPollution() {
        return pollution;
    }

    public void setPollution(int pollution) {
        this.pollution = pollution;
    }

    public int getNumberOfTessts() {
        return numberOfTests;
    }

    public void setNumberOfTessts(int numberOfTessts) {
        this.numberOfTests = numberOfTessts;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public static LinkedList<ArrayList<Boolean>> ToBoolean(String s, int cap){
        try{
            LinkedList<ArrayList<Boolean>> linkedList = new LinkedList<ArrayList<Boolean>>();
            ArrayList<Boolean> tmp = new ArrayList<Boolean>();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '1') {
                    tmp.add(true);
                }
                if (s.charAt(i) == '0') {
                    tmp.add(false);
                }
                if (tmp.size() == cap) {
                    linkedList.add(new ArrayList<Boolean>(tmp));
                    tmp = new ArrayList<Boolean>();
                }
            }
            System.out.println("OK");
            return linkedList;
        }catch (Exception e){return null;}

    }
}
