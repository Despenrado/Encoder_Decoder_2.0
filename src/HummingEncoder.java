import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class HummingEncoder extends Signal{

    HummingEncoder(ArrayList<Boolean> arrayList){
        super(arrayList);
    }

    private void addEmptyControlBits(){

            ArrayList<Boolean> newArray = new ArrayList<Boolean>();
            Iterator<Boolean> iterBool = getIntermediateSignal().iterator();
            int degree = 0;
            for (int i = 0; iterBool.hasNext(); i++) {
                if(i != Math.pow(2, degree) - 1){
                    newArray.add(iterBool.next());
                }
                else{
                    newArray.add(false);
                    degree++;
                }
            }
            setIntermediateSignal(new ArrayList<Boolean>(newArray));

    }

    public void printList(ArrayList<Boolean> tmp){
        int i = 0;

        Iterator<Boolean> iterBool = tmp.iterator();
        while (iterBool.hasNext()) {
            System.out.print(iterBool.next() + " ");
            i++;
        }
            System.out.println();
        System.out.println(i);
    }
    @Override
    public void calculationOfControlBits(){

        int[] c = new int[log2(getCapacity())+1];
        int[] check = new int[log2(getCapacity())];
        int[] check2 = new int[log2(getCapacity())];
        for (int i = 0; i < c.length; i++) {
            c[i] = 0;
        }
        for (int i = 0; i < check.length; i++) {
            check[i] = (int) Math.pow(2, i+1);
            check2[i] = (int) Math.pow(2, i+1)-1;
        }

        int i = 0;
        Iterator<Boolean> iter = getIntermediateSignal().iterator();
        while (iter.hasNext()){
            Boolean tmpBool = iter.next();
            if(i % 2 == 0 && tmpBool == true && i != 0){   // c0
                c[0]++;
            }
            for (int j = 1; j < log2(getCapacity())+1; j++) {
                if(i == check[j-1]){   // c1
                    check2[j-1]--;
                    check[j-1]++;
                    if(tmpBool == true){
                        c[j]++;
                    }
                }
                if(check2[j-1] == 0){
                    check[j-1]+= Math.pow(2, j);
                    check2[j-1] = (int) Math.pow(2, j);
                }
            }
            i++;
        }

        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        //printList(bool);

        for (int i2 = 0; i2 < log2(getCapacity())+1; i2++) {
            Double d = Math.pow(2, i2);
            int a = d.intValue();
            if(c[i2] % 2 == 1){
                a--;
                getIntermediateSignal().set(a, true);
                bool.add(true);
            }
            else{
                bool.add(false);
            }

            //System.out.println(c[i2]);
        }
        //printList(bool);
        setControlBit(new ArrayList<Boolean>(bool));
    }

    ArrayList<Boolean> encodeSignal() {
        //System.out.println("единицы"true true true true false false false false  / true true true false false false false  / true true true true true true true false false false false false );
        //this.printList(this.list);

        this.addEmptyControlBits();
        //System.out.println("с добавлением");
       // this.printList(this.getIntermediateSignal());

        this.calculationOfControlBits();
        //System.out.println("сохран");
        //this.printList(this.savedList);
        //System.out.println("выход");
        //this.printList(this.list);
        //System.out.println("контрольки");
        //this.printList(this.checkBytes);
        this.setFinalSignal(getIntermediateSignal());
        //printList(getFinalSignal());
        return getIntermediateSignal();
    }
}
