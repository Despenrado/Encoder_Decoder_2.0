import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

public class Test implements Runnable{

    ArrayList<Boolean> arr;
    ArrayList<Boolean> noise;
    HummingDecoder hummingDecoder;
    HummingEncoder hummingEncoder;
    int capacity;

    Test(ArrayList<Boolean> array, int capacity){
        this.capacity = capacity;
        this.arr = new ArrayList<Boolean>(array);
    }


    @Override
    public void run() {
        hummingEncoder = new HummingEncoder(arr, capacity);
        hummingEncoder.encodeSignal();

        noise = new ArrayList<Boolean>(hummingEncoder.list);

        this.noise();
        //TripleEncoder.ptintToConsole(noise);

        hummingDecoder = new HummingDecoder(this.noise, capacity);
        hummingDecoder.decodeSignal();
        print(check(), this);

        UI.loading();
    }

    public int check(){

        //111 - 1 - nie ma zakłocen;+
        //101 - 2 - zakłocenia wykryte;+
        //100 - 3 - zaklocenia w sygnalie i w bitach kontrolnych => ale wykryte zaklocenia => blend w dekoderze;
        //110 - 4 - 1 zaklocenie w bitach kontrolnych lub w bitach nieznaczących lub blend dekodera => ale wykryte zaklocenia;+
        //010 - 5 - zaklocenia w bitach kontrolnych => nie wykryte zaklocenia => dekoder dziala;+
        //011 - 6 - bez zaklocen, ale problem z dekoderem => nie wykryte zaklocenia;
        //001 - 7 - wiele zaklocen w sygnalie lub problem dekodera => nie wykryte zaklocenia+
        //000 - 8 - wszedzie blednde+

        if(hummingEncoder.savedList.equals(hummingDecoder.finalList) && hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 1;}
        if(hummingEncoder.savedList.equals(hummingDecoder.finalList) && !hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 2;}
        if(hummingEncoder.savedList.equals(hummingDecoder.finalList) && !hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && !hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 3;}
        if(hummingEncoder.savedList.equals(hummingDecoder.finalList) && hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && !hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 4;}
        if(!hummingEncoder.savedList.equals(hummingDecoder.finalList) && hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && !hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 5;}
        if(!hummingEncoder.savedList.equals(hummingDecoder.finalList) && hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 6;}
        if(!hummingEncoder.savedList.equals(hummingDecoder.finalList) && !hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 7;}
        if(!hummingEncoder.savedList.equals(hummingDecoder.finalList) && !hummingEncoder.savedList.equals(hummingDecoder.noCorrectList) && !hummingEncoder.checkBytes.equals(hummingDecoder.controlBytes)){ return 8;}



        return 0;
    }


    public void noise(){
        SecureRandom random = new SecureRandom();
        int i = 0;
        Iterator<Boolean> iter = noise.iterator();
        while (iter.hasNext()){
            if(random.nextInt(100) < UI.pollute){
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

            Iterator<Boolean> iter = test.hummingEncoder.savedList.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingEncoder.checkBytes.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingEncoder.list.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = test.noise.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = test.hummingDecoder.finalList.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingDecoder.noCorrectList.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingDecoder.controlBytes.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.hummingDecoder.checkBytes.iterator();
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
