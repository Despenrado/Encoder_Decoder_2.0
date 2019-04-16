import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class HummingEncoder {

     ArrayList<Boolean> list;
     ArrayList<Boolean> savedList;
     ArrayList<Boolean> checkBytes;
     int capacity;

    private String progress = "";
    private JTextField fieldProgress;


    HummingEncoder(ArrayList<Boolean> arrayList, int capacity){
        this.capacity = capacity;
        list = new  ArrayList<Boolean>(arrayList);
        savedList = new ArrayList<Boolean>(arrayList);
    }

    public void addBytes(){

            ArrayList<Boolean> newArray = new ArrayList<Boolean>();
            Iterator<Boolean> iterBool = list.iterator();
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
            list = new ArrayList<Boolean>(newArray);

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

    public void enCoder(){
        int[] c = new int[7];
        int[] check = new int[6];
        int[] check2 = new int[6];
        for (int i = 0; i < 7; i++) {
            c[i] = 0;
        }
        check[0] = 1;
        check2[0] = 2;
        check[1] = 3;
        check2[1] = 4;
        check[2] = 7;
        check2[2] = 8;
        check[3] = 15;
        check2[3] = 16;
        check[4] = 31;
        check2[4] = 32;
        check[5] = 63;
        check2[5] = 64;

        int i = 0;
        Iterator<Boolean> iter = list.iterator();
        while (iter.hasNext()){
            Boolean tmpBool = iter.next();
            if(i % 2 == 0 && tmpBool == true){   // c0
                c[0]++;
            }
            if(i == check[0]){   // c1
                check2[0]--;
                check[0]++;
                if(tmpBool == true){
                    c[1]++;
                }
            }
            if(check2[0] == 0){
                check[0]+= 2;
                check2[0] = 2;
            }
            if(i == check[1]){   // c2
                check[1]++;
                check2[1]--;
                if(tmpBool == true){
                    c[2]++;
                }
            }
            if(check2[1] == 0){
                check[1]+= 4;
                check2[1] = 4;
            }
            if(i == check[2]){   // c3
                check[2]++;
                check2[2]--;
                if(tmpBool == true){
                    c[3]++;
                }
            }
            if(check2[2] == 0){
                check[2]+= 8;
                check2[2] = 8;
            }
            if(i == check[3]){   // c4
                check[3]++;
                check2[3]--;
                if(tmpBool == true){
                    c[4]++;
                }
            }
            if(check2[3] == 0){
                check[3]+= 16;
                check2[3] = 16;
            }
            if(i == check[4]){   // c5
                check[4]++;
                check2[4]--;
                if(tmpBool == true){
                    c[5]++;
                }
            }
            if(check2[4] == 0){
                check[4]+= 32;
                check2[4] = 32;
            }
            if(i == check[5]){   // c6
                check[5]++;
                check2[5]--;
                if(tmpBool == true){
                    c[6]++;
                }
            }
            if(check2[5] == 0){
                check[5]+= 64;
                check2[5] = 64;
            }
            i++;
        }

        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        //printList(bool);

        for (int i2 = 0; i2 < 7; i2++) {
            Double d = Math.pow(2, i2);
            int a = d.intValue();
            if(capacity >= a && c[i2] % 2 == 1){
                a--;
                list.set(a, true);
                bool.add(true);
            }
            else{
                bool.add(false);
            }

            //System.out.println(c[i2]);
        }
        //printList(bool);
        checkBytes = new ArrayList<Boolean>(bool);
    }

    ArrayList<Boolean> encodeSignal() {
        //System.out.println("единицы"true true true true false false false false  / true true true false false false false  / true true true true true true true false false false false false );
        //this.printList(this.list);

        this.addBytes();
        //System.out.println("с добавлением");
        //this.printList(this.list);

        this.enCoder();
        //System.out.println("сохран");
        //this.printList(this.savedList);
        //System.out.println("выход");
        //this.printList(this.list);
        //System.out.println("контрольки");
        //this.printList(this.checkBytes);

        return this.list;
    }

}
