import java.util.ArrayList;
import java.util.Iterator;

public class HummingDecoder extends Signal {

    private ArrayList<Boolean> calculatedControlBits;
    private ArrayList<Boolean> noCorrectFinalSignal;

    HummingDecoder(ArrayList<Boolean> tmp) {
        super(tmp);
    }

    public ArrayList<Boolean> getCalculatedControlBits() {
        return calculatedControlBits;
    }

    public void setCalculatedControlBits(ArrayList<Boolean> calculatedControlBits) {
        this.calculatedControlBits = calculatedControlBits;
    }

    public ArrayList<Boolean> getNoCorrectFinalSignal() {
        return noCorrectFinalSignal;
    }

    public void setNoCorrectFinalSignal(ArrayList<Boolean> noCorrectFinalSignal) {
        this.noCorrectFinalSignal = noCorrectFinalSignal;
    }

    private void correction() {
        Iterator<Boolean> iter1 = getControlBit().iterator();
        Iterator<Boolean> iter2 = calculatedControlBits.iterator();
        int i = 0;
        int number = 0;
        while (iter1.hasNext() && iter2.hasNext()) {
            if (iter1.next() != iter2.next()) {
                number += Math.pow(2, i);
                //System.out.println("################");
                //System.out.println(i);
            }
            i++;
        }
        //System.out.print("/////////////////////////");
        // System.out.println(number);
        number--;
        if (number >= 0 && number < getIntermediateSignal().size()) {
            getIntermediateSignal().set(number, !getIntermediateSignal().get(number));
        }

        this.delControlBytes();
    }

    private void delControlBytes() {
        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        int degree = 0;
        int i = 0;
        Iterator<Boolean> iter2 = getIntermediateSignal().iterator();
        while (iter2.hasNext()) {
            //System.out.print(i);
            //System.out.print("   ");
            //System.out.println(Math.pow(2, degree) - 1);
            if (i != Math.pow(2, degree) - 1) {
                bool.add(iter2.next());
            } else {
                iter2.next();
                degree++;
            }
            i++;
        }
        setFinalSignal(new ArrayList<Boolean>(bool));
    }

    private void pullControlBytes() {
        int degree = 0;
        int i = 0;
        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        ArrayList<Boolean> tmp = new ArrayList<Boolean>();
        Iterator<Boolean> iter2 = getIntermediateSignal().iterator();
        while (iter2.hasNext()) {
            if (i == Math.pow(2, degree) - 1) {
                tmp.add(iter2.next());
                degree++;
            } else {
                bool.add(iter2.next());
            }

            i++;
        }

       // printList(bool);
       // printList(tmp);
        noCorrectFinalSignal = bool;
        setControlBit(tmp);

    }
    @Override
    void calculationOfControlBits() {
        int[] c = new int[log2(getCapacity()) + 1];
        int[] check = new int[log2(getCapacity())];
        int[] check2 = new int[log2(getCapacity())];
        for (int i = 0; i < c.length; i++) {
            c[i] = 0;
        }
        for (int i = 0; i < check.length; i++) {
            check[i] = (int) Math.pow(2, i + 1);
            check2[i] = (int) Math.pow(2, i + 1) - 1;
        }

        int i = 0;
        Iterator<Boolean> iter = getIntermediateSignal().iterator();
        while (iter.hasNext()) {
            Boolean tmpBool = iter.next();
            if (i % 2 == 0 && tmpBool == true && i != 0) {   // c0
                c[0]++;
            }
            for (int j = 1; j < log2(getCapacity()) + 1; j++) {
                if (i == check[j - 1]) {   // c1
                    check2[j - 1]--;
                    check[j - 1]++;
                    if (tmpBool == true) {
                        c[j]++;
                    }
                }
                if (check2[j - 1] == 0) {
                    check[j - 1] += Math.pow(2, j);
                    check2[j - 1] = (int) Math.pow(2, j);
                }
            }
            i++;
        }

        ArrayList<Boolean> bool = new ArrayList<Boolean>();

        for (int i2 = 0; i2 < log2(getCapacity()) + 1; i2++) {
            if (c[i2] % 2 == 1) {
                bool.add(true);
            } else {
                bool.add(false);
            }

            //System.out.println(c[i2]);
        }
        calculatedControlBits = new ArrayList<Boolean>(bool);
        //list.set(numberOfNextArr, tmpArr);
    }


    ArrayList<Boolean> decodeSignal() {

        //System.out.println("вход");
        //this.printList(this.list);

        this.calculationOfControlBits();
        //System.out.println("декодер");
        //this.printList(this.checkBytes);


        this.pullControlBytes();
        //System.out.println("контрольки");
        //this.printList(this.noCorrectFinalSignal);
        //this.printList(this.controlBytes);

        this.correction();
        //System.out.println("коррекция");
        //this.printList(this.finalList);


        //this.printList(this.controlBytes);
        //this.printList(this.checkBytes);


        return getFinalSignal();
    }

    public void printList(ArrayList<Boolean> tmp) {
        int i = 0;
        Iterator<Boolean> iterBool = tmp.iterator();
        while (iterBool.hasNext()) {
            System.out.print(iterBool.next() + " ");
            i++;
        }
        System.out.println();
        System.out.println(i);
    }
}
