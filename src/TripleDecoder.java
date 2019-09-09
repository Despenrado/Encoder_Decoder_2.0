import java.util.ArrayList;
import java.util.Iterator;

public class TripleDecoder extends Signal {

    TripleDecoder(ArrayList<Boolean> tmp){
        super(tmp);
    }

    public void decode(){
        calculationOfControlBits();
        setFinalSignal(getIntermediateSignal());
    }

    @Override
    void calculationOfControlBits(){
        setIntermediateSignal(new ArrayList<Boolean>());
        Iterator<Boolean> iter = this.getInitialSignal().iterator();
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
            getIntermediateSignal().add(tmp);
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
