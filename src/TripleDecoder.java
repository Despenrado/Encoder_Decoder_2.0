import java.util.ArrayList;
import java.util.Iterator;

public class TripleDecoder {

    ArrayList<Boolean> noCorrectList;
    ArrayList<Boolean> finalList;
    int capacity;

    TripleDecoder(ArrayList<Boolean> tmp, int tmp_cap){
        this.capacity = tmp_cap;
        this.noCorrectList = new ArrayList<Boolean>(tmp);

    }

    public void decode(){
        this.finalList = new ArrayList<Boolean>();
        Iterator<Boolean> iter = this.noCorrectList.iterator();
        while (iter.hasNext()){
            Boolean tmp1 = iter.next();
            Boolean tmp2 = iter.next();
            Boolean tmp3 = iter.next();
            Boolean tmp = false;
            if(tmp1 == tmp2){
                tmp = tmp1;
            }
            if(tmp1 == tmp3){
                tmp = tmp1;
            }
            if(tmp2 == tmp3){
                tmp = tmp2;
            }
            finalList.add(tmp);
        }
    }

    public static void ptintToConsole(ArrayList<Boolean> tmp){
        int i = 0;
        Iterator<Boolean> iter = tmp.iterator();
        while (iter.hasNext()){
            System.out.print(iter.next() + " ");
            i++;
        }
        System.out.println(i + "\n");
    }

}
