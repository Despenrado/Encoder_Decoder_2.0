import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;

public class TestTriple implements Runnable{

    TripleDecoder tripleDecoder;
    TripleEncoder tripleEncoder;
    int capacity;
    ArrayList<Boolean> arr;
    ArrayList<Boolean> noise;


    TestTriple(ArrayList<Boolean> array, int cap){
        this.arr = new ArrayList<Boolean>(array);
        this.capacity = cap;
    }

    @Override
    public void run() {
        tripleEncoder = new TripleEncoder(arr, 8);
        tripleEncoder.encode();
        noise = new ArrayList<Boolean>(tripleEncoder.list);

        this.noise();
        //TripleEncoder.ptintToConsole(noise);

        tripleDecoder = new TripleDecoder(this.noise, 8);
        tripleDecoder.decode();
        //TripleDecoder.ptintToConsole(tripleDecoder.noCorrectList);
        //TripleDecoder.ptintToConsole(tripleDecoder.finalList);

        int n = check();
        TestTriple.print(n, this);
        UI.loading();
    }

    public void noise(){
        SecureRandom random = new SecureRandom();
        int i = 0;
        Iterator<Boolean> iter = this.noise.iterator();
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

    public int check(){

        // 11 - bez zakl, sygnał korektny
        // 10 - z zakl, sygnal odnowiony
        // 00 - sygnal na tyle zalocony, ze nie mozna odnowic
        // 01 - blędna korekcja (z powody bitów korrekcyjnych)

        if(this.arr.equals(tripleDecoder.finalList) && tripleEncoder.list.equals(tripleDecoder.noCorrectList)){return 1;}
        if(this.arr.equals(tripleDecoder.finalList) && !tripleEncoder.list.equals(tripleDecoder.noCorrectList)){return 2;}
        if(!this.arr.equals(tripleDecoder.finalList) && !tripleEncoder.list.equals(tripleDecoder.noCorrectList)){return 3;}
        if(!this.arr.equals(tripleDecoder.finalList) && tripleEncoder.list.equals(tripleDecoder.noCorrectList)){return 4;}


        return 0;
    }


    public static synchronized void print(int n, TestTriple test){
        TestTriple.printToFile("Results_Triple.txt", n);
        TestTriple.printToFile("Log_Triple.txt", test, n);
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

    public static synchronized void printToFile(String fileName, TestTriple test, int n){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8")))){
            bufferedWriter.write(Integer.toString(n));
            bufferedWriter.newLine();

            Iterator<Boolean> iter = test.tripleEncoder.savedList.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.tripleEncoder.list.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = test.noise.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();

            iter = test.tripleDecoder.noCorrectList.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.write(" / ");
            iter = test.tripleDecoder.finalList.iterator();
            while(iter.hasNext()){
                bufferedWriter.write(Boolean.toString(iter.next()) + " ");

            }
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){

        }
    }

    public  static void getStatistic(String fileName){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            int []result = new int[4];
            int much = 0;
            int res;
            for (int i = 0; i < 4; i++) {
                result[i] = 0;
            }
            do {
                try {
                    res = Integer.parseInt(bufferedReader.readLine());
                    much++;
                }
                catch (Exception e){
                    res = -1;
                }
                switch (res){
                    case 1:{
                        result[0]++;
                        break;
                    }
                    case 2:{
                        result[1]++;
                        break;
                    }
                    case 3:{
                        result[2]++;
                        break;
                    }
                    case 4:{
                        result[3]++;
                        break;
                    }
                }

            }while(res != -1);
            System.out.println("1) - " + result[0] + " - " + result[0]*100/much + "%");
            System.out.println("2) - " + result[1] + " - " + result[1]*100/much + "%");
            System.out.println("3) - " + result[2] + " - " + result[2]*100/much + "%");
            System.out.println("4) - " + result[3] + " - " + result[3]*100/much + "%");
        }
        catch (IOException e){
            System.out.println("Error: file read error");
        }
    }

}
