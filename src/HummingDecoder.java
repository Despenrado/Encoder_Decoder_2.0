import java.util.ArrayList;
import java.util.Iterator;

public class HummingDecoder {

     ArrayList<Boolean> list;
     ArrayList<Boolean> controlBytes;
     ArrayList<Boolean> checkBytes;
     ArrayList<Boolean> noCorrectList;
     ArrayList<Boolean> finalList;
     int capacity;

    HummingDecoder(ArrayList<Boolean> tmp, int capacity){
        list = new ArrayList<Boolean>(tmp);
        this.capacity = capacity;
    }

    public void correction(){
        finalList = new ArrayList<Boolean>(list);
        Iterator<Boolean> iter1= controlBytes.iterator();
        Iterator<Boolean> iter2= checkBytes.iterator();
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
            if(number >= 0 && number < list.size())
                finalList.set(number, !list.get(number));

        this.delControlBytes();
    }

    public void delControlBytes(){
        ArrayList<Boolean> bool = new ArrayList<Boolean>();
        int degree = 0;
        int i = 0;
        Iterator<Boolean> iter2 = finalList.iterator();
        while (iter2.hasNext()){
            //System.out.print(i);
            //System.out.print("   ");
            //System.out.println(Math.pow(2, degree) - 1);
            if(i != Math.pow(2, degree) - 1){
                bool.add(iter2.next());
            }
            else {
                iter2.next();
                degree++;
            }
            i++;
        }
        this.finalList = new ArrayList<Boolean>(bool);
    }

    public void pullcontrolBytes(){
            int degree = 0;
            int i = 0;
            int i2 = 0;
            ArrayList<Boolean> bool = new ArrayList<Boolean>();
            ArrayList<Boolean> tmp = new ArrayList<Boolean>();
            Iterator<Boolean> iter2 = list.iterator();
            while (iter2.hasNext()){
                if(i == Math.pow(2, degree) - 1){
                    tmp.add(iter2.next());
                    degree++;
                    i2++;
                }
                else {
                    bool.add(iter2.next());
                }

                i++;
            }
            while (tmp.size() < 7){
                tmp.add(false);
            }
            noCorrectList = new ArrayList<Boolean>(bool);
            this.controlBytes = tmp;

    }

    public void deCoder(){
        int[] c = new int[7];
        int[] check = new int[6];
        int[] check2 = new int[6];
        for (int i = 0; i < 7; i++) {
            c[i] = 0;
        }
        check[0] = 2;
        check2[0] = 1;
        check[1] = 4;
        check2[1] = 3;
        check[2] = 8;
        check2[2] = 7;
        check[3] = 16;
        check2[3] = 15;
        check[4] = 32;
        check2[4] = 31;
        check[5] = 64;
        check2[5] = 63;

        int i = 0;
        Iterator<Boolean> iter = list.iterator();
        while (iter.hasNext()){
            Boolean tmpBool = iter.next();
            if(i % 2 == 0 && tmpBool == true && i != 0){   // c0
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

        for (int i2 = 0; i2 < 7; i2++) {
            if(c[i2]%2 == 1) {
                bool.add(true);
            }
            else {
                bool.add(false);
            }

            //System.out.println(c[i2]);
        }
        checkBytes = new ArrayList<Boolean>(bool);
        //list.set(numberOfNextArr, tmpArr);
    }


    ArrayList<Boolean> decodeSignal(){

        //System.out.println("вход");
        //this.printList(this.list);

        this.deCoder();
        //System.out.println("декодер");
        //this.printList(this.checkBytes);


        this.pullcontrolBytes();
        //System.out.println("контрольки");
        //this.printList(this.noCorrectList);
        //this.printList(this.controlBytes);

        this.correction();
        //System.out.println("коррекция");
        //this.printList(this.finalList);


        //this.printList(this.controlBytes);
        //this.printList(this.checkBytes);

        return this.finalList;
    }

    public void printList(ArrayList<Boolean> tmp){
        int i = 0;
        Iterator<Boolean> iterBool = tmp.iterator();
        while(iterBool.hasNext()){
            System.out.print(iterBool.next() + " ");
            i++;
        }
        System.out.println();
        System.out.println(i);
    }

}
