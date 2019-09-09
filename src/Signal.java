import java.util.ArrayList;

public abstract class Signal {
    private int capacity;
    private ArrayList<Boolean> initialSignal;
    private ArrayList<Boolean> intermediateSignal;
    private ArrayList<Boolean> controlBit;
    private ArrayList<Boolean> finalSignal;

    public Signal(ArrayList<Boolean> initialSignal){
        this.capacity = initialSignal.size();
        this.initialSignal = new ArrayList<Boolean>(initialSignal);
        this.intermediateSignal = new ArrayList<Boolean>(initialSignal);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setInitialSignal(ArrayList<Boolean> initialSignal) {
        this.initialSignal = initialSignal;
    }

    public void setControlBit(ArrayList<Boolean> controlBit) {
        this.controlBit = controlBit;
    }

    public void setFinalSignal(ArrayList<Boolean> finalSignal) {
        this.finalSignal = finalSignal;
    }

    public void setIntermediateSignal(ArrayList<Boolean> intermediateSignal) {
        this.intermediateSignal = intermediateSignal;
    }

    public ArrayList<Boolean> getIntermediateSignal() {
        return intermediateSignal;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Boolean> getInitialSignal() {
        return initialSignal;
    }

    public ArrayList<Boolean> getControlBit() {
        return controlBit;
    }

    public ArrayList<Boolean> getFinalSignal() {
        return finalSignal;
    }

    public int log2(int a){
        if(a <= 2)
        {
            return 1;
        }
        if(a <= 4)
        {
            return 2;
        }
        if(a <= 8)
        {
            return 3;
        }
        if(a <= 16)
        {
            return 4;
        }
        if(a <= 32)
        {
            return 5;
        }
        if(a <= 64)
        {
            return 6;
        }
        if(a <= 128)
        {
            return 7;
        }
        if(a <= 256)
        {
            return 8;
        }
        if(a <= 512)
        {
            return 9;
        }
        if(a <= 1024)
        {
            return 10;
        }
        if(a <= 2048)
        {
            return 11;
        }
        if(a <= 4096)
        {
            return 12;
        }
        if(a <= 8192)
        {
            return 13;
        }
        if(a <= 16384)
        {
            return 14;
        }
        if(a <= 32768)
        {
            return 15;
        }
        if(a <= 65536)
        {
            return 16;
        }
        if(a <= 131072)
        {
            return 17;
        }
        if(a <= 26214)
        {
            return 18;
        }
        if(a <= 524288)
        {
            return 19;
        }
        if(a <= 1048576)
        {
            return 20;
        }
        if(a <= 2097152)
        {
            return 21;
        }
        if(a <= 4194304)
        {
            return 22;
        }
        if(a <= 8388608)
        {
            return 23;
        }
        if(a <= 16777216)
        {
            return 24;
        }
        if(a <= 33554432)
        {
            return 25;
        }
        if(a <= 67108864)
        {
            return 26;
        }
        if(a <= 134217728)
        {
            return 27;
        }
        if(a <= 268435456)
        {
            return 28;
        }
        if(a <= 536870912)
        {
            return 29;
        }
        return 30;
    }

    abstract void calculationOfControlBits();

}
