import java.util.ArrayList;
import java.util.Iterator;

public class TripleEncoder {

    ArrayList<Boolean> list;
    ArrayList<Boolean> savedList;
    int capacity;

    TripleEncoder(ArrayList<Boolean> tmp, int tmp_cap){
        this.capacity = tmp_cap;
        this.savedList = new ArrayList<Boolean>(tmp);
    }

    public void encode(){
        ArrayList<Boolean> tmp = new ArrayList<Boolean>();
        Iterator<Boolean> iter = this.savedList.iterator();
        while(iter.hasNext()){
            Boolean tmpBool = new Boolean(iter.next());
            for (int i = 0; i < 3; i++) {
                tmp.add(tmpBool);
            }
        }
        this.list = tmp;
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
