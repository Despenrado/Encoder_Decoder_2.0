import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

public class Test implements Runnable{

    private ArrayList<Boolean> arr;
    private ArrayList<Boolean> noise;
    private HummingDecoder hummingDecoder;
    private  HummingEncoder hummingEncoder;
    private int pollute;
    private UI ui;

    Test(ArrayList<Boolean> array, int pollute, UI ui){
        this.arr = new ArrayList<Boolean>(array);
        this.pollute = pollute;
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
    }

    private int check(){

        //111 - 1 - nie ma zakłocen;+
        //101 - 2 - zakłocenia wykryte;+
        //100 - 3 - zaklocenia w sygnalie i w bitach kontrolnych => ale wykryte zaklocenia => blend w dekoderze;
        //110 - 4 - 1 zaklocenie w bitach kontrolnych lub w bitach nieznaczących lub blend dekodera => ale wykryte zaklocenia;+
        //010 - 5 - zaklocenia w bitach kontrolnych => nie wykryte zaklocenia => dekoder dziala;+
        //011 - 6 - bez zaklocen, ale problem z dekoderem => nie wykryte zaklocenia;
        //001 - 7 - wiele zaklocen w sygnalie lub problem dekodera => nie wykryte zaklocenia+
        //000 - 8 - wszedzie blednde+

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
            if(random.nextInt(100) < this.pollute){
                boolean tmp = iter.next();
                this.noise.set(i, !tmp);
            }
            else{
                iter.next();
            }
            i++;
        }
    }


    public static synchronized void print(int n, Test test){
        Test.printToFile("Results_Humming.txt", n);
        Test.printToFile("Log_Humming.txt", test, n);
           // UI.getProgress(test.times);
    }

    public synchronized static void printToFile(String fileName, int n){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8")))){
            bufferedWriter.write(Integer.toString(n));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){

        }
    }

    public static synchronized void printToFile(String fileName, Test test, int n){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8")))){
            bufferedWriter.write(Integer.toString(n));
            bufferedWriter.newLine();

            Iterator<Boolean> iter = test.hummingEncoder.getInitialSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingEncoder.getControlBit().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingEncoder.getIntermediateSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = test.noise.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = test.hummingDecoder.getFinalSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingDecoder.getNoCorrectFinalSignal().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingDecoder.getControlBit().iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingDecoder.getCalculatedControlBits().iterator();
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
        Test.delFile();
        Test.propertiesOfTest(text);
    }

    public static void propertiesOfTest(String text){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("Log_Humming.txt", true), "UTF-8")))){
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){

        }
    }

    public static void delFile(){
        try{
            File file = new File("Results_Humming.txt");
            file.delete();
        }catch (Exception e){

        }
    }

}
