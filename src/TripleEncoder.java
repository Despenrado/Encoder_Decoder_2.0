import java.util.ArrayList;
import java.util.Iterator;

public class TripleEncoder extends Signal{

    TripleEncoder(ArrayList<Boolean> tmp){
        super(tmp);
    }

    public void encode(){
        calculationOfControlBits();
        setFinalSignal(getIntermediateSignal());
    }

    @Override
    void calculationOfControlBits(){
        ArrayList<Boolean> tmp = new ArrayList<Boolean>();
        Iterator<Boolean> iter = this.getIntermediateSignal().iterator();
        while(iter.hasNext()){
            Boolean tmpBool = new Boolean(iter.next());
            for (int i = 0; i < 3; i++) {
                tmp.add(tmpBool);
            }
        }
        setIntermediateSignal(tmp);
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
