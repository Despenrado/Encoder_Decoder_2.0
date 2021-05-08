import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

public class HummingTest implements Runnable{

    private ArrayList<Boolean> arr;
    private ArrayList<Boolean> noise;
    private HummingDecoder hummingDecoder;
    private  HummingEncoder hummingEncoder;
    private int interference;
    private UI ui;

    HummingTest(ArrayList<Boolean> array, int interference, UI ui){
        this.arr = new ArrayList<Boolean>(array);
        this.interference = interference;
        this.ui = ui;
    }


    @Override
    public void run() {
        hummingEncoder = new HummingEncoder(arr);
        hummingEncoder.encodeSignal();

        noise = new ArrayList<Boolean>(hummingEncoder.getFinalSignal());
        this.noise();
        //TripleEncoder.ptintToConsole(noise);

        hummingDecoder = new HummingDecoder(this.noise);
        hummingDecoder.decodeSignal();
        print(check(), this);

        ui.progress();
        ui.latch.countDown();
    }

    private int check(){

        //111 - 1 - nie ma zakłocen;+
        //101 - 2 - zakłocenia wykryte;+
        //100 - 3 - zaklocenia w sygnalie i w bitach kontrolnych => ale wykryte zaklocenia => blend w dekoderze;
        //110 - 4 - 1 zaklocenie w bitach kontrolnych lub w bitach nieznaczących lub blend dekodera => ale wykryte zaklocenia;+
        //010 - 5 - zaklocenia w bitach kontrolnych => nie wykryte zaklocenia => dekoder dziala;+
        //011 - 6 - bez zaklocen, ale problem z dekoderem => nie wykryte zaklocenia;
        //001 - 7 - wiele zaklocen w sygnalie lub problem dekodera => nie wykryte zaklocenia+
        //000 - 8 - wszedzie bledndy (w bitach kontrolnych i w sygnale)+
        ////
        //111 - 1 - no interference;+
        //101 - 2 - interference detected;+
        //100 - 3 - interference in the signal and in the control bits = > but detected interference = > blend in the decoder;
        //110 - 4 - 1 interference in the control bits or in the minor bits or the decoder blend = > but detected interference;+
        //010 - 5 - interference in the control bits = > no interference detected = > the decoder is working;+
        //011 - 6 - no interference, but a problem with the decoder = > no interference detected;
        //001 - 7 - multiple signal interference or decoder problem = > undetected interference+
        //000 - 8 - everywhere bledndy (in the control bits and in the signal)+

        if(hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 1;}
        if(hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                !hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 2;}
        if(hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                !hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                !hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 3;}
        if(hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                !hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 4;}
        if(!hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                !hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 5;}
        if(!hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 6;}
        if(!hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                !hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 7;}
        if(!hummingEncoder.getInitialSignal().equals(hummingDecoder.getFinalSignal()) &&
                !hummingEncoder.getInitialSignal().equals(hummingDecoder.getNoCorrectFinalSignal()) &&
                !hummingEncoder.getControlBit().equals(hummingDecoder.getControlBit())){
            return 8;}



        return 0;
    }


    private void noise(){
        SecureRandom random = new SecureRandom();
        int i = 0;
        Iterator<Boolean> iter = noise.iterator();
        while (iter.hasNext()){
            if(random.nextInt(100) < this.interference){
                boolean tmp = iter.next();
                this.noise.set(i, !tmp);
            }
            else{
                iter.next();
            }
            i++;
        }
    }


    public static synchronized void print(int n, HummingTest hummingTest){
        HummingTest.printToFile("Results_Humming.txt", n);
        HummingTest.printToFile("Log_Humming.txt", hummingTest, n);
           // UI.getProgress(test.times);
    }

    public synchronized static void printToFile(String fileName, int n){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("results/"+fileName, true), "UTF-8")))){
            bufferedWriter.write(Integer.toString(n));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){

        }
    }

    public static synchronized void printToFile(String fileName, HummingTest hummingTest, int n){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("results/"+fileName, true), "UTF-8")))){
            bufferedWriter.write(Integer.toString(n));
            bufferedWriter.newLine();

            Iterator<Boolean> iter = hummingTest.hummingEncoder.getInitialSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = hummingTest.hummingEncoder.getControlBit().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = hummingTest.hummingEncoder.getIntermediateSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = hummingTest.noise.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = hummingTest.hummingDecoder.getFinalSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = hummingTest.hummingDecoder.getNoCorrectFinalSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = hummingTest.hummingDecoder.getControlBit().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = hummingTest.hummingDecoder.getCalculatedControlBits().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){

        }
    }

    public static void fileNewTest(String text){
        HummingTest.delFile();
        HummingTest.propertiesOfTest(text);
    }

    public static void propertiesOfTest(String text){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("results/"+"Log_Humming.txt", true), "UTF-8")))){
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){

        }
    }

    public static void delFile(){
        try{
            File file = new File("results/"+"Results_Humming.txt");
            file.delete();
        }catch (Exception e){

        }
    }

}
