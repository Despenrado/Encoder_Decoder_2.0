import java.io.*;

public class ScanResults {
    private int[] results;
    private String encoderName;
    private int number;

    public ScanResults(int[] results, String testName, int number) {
        this.results = results;
        this.encoderName = testName;
        this.number = number;
    }

    public void setResults(int[] results) {
        this.results = results;
    }

    public void setencoderName(String testName) {
        this.encoderName = testName;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int[] getResults() {
        return results;
    }

    public String getencoderName() {
        return encoderName;
    }

    public int getNumber() {
        return number;
    }

    public static ScanResults getStatistic(String fileName){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            int []result = new int[8];
            int much = 0;
            int res;
            for (int i = 0; i < 8; i++) {
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
                    case 5:{
                        result[4]++;
                        break;
                    }
                    case 6:{
                        result[5]++;
                        break;
                    }
                    case 7:{
                        result[6]++;
                        break;
                    }
                    case 8:{
                        result[7]++;
                        break;
                    }
                }

            }while(res != -1);
            String encoderName;
            if(fileName.equals("Results_Humming.txt")){
                return new ScanResults(result, "Humming", much);
            }
            else {
                return new ScanResults(result, "Triple", much);
            }

        }
        catch (IOException e){
            System.out.println("Error: file read error");
        }
        return null;
    }

    public static void print(ScanResults scanResults){
        //printToConsole(scanResults);
        printToFile(scanResults);
    }

    public static void printToFile(ScanResults scanResults){
        if(scanResults.getencoderName().equals("Humming")){
            printToFileHumming(scanResults);
            printToFileHumming_Correct_nonCorrect(scanResults);
        }
        else {
            printToFileTriple(scanResults);
            printToFileTriple_Correct_nonCorrect(scanResults);
        }
    }

    public static void printToFileHumming(ScanResults scanResults){
        for (int i = 0; i < 8; i++) {
            ScanResults.printToFile("Humming_statistic.txt", scanResults.getResults()[i]);
        }
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("Humming_statistic.txt", true), "UTF-8")))){
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("error");
        }
    }

    public static void printToFileTriple(ScanResults scanResults){
        for (int i = 0; i < 4; i++) {
            ScanResults.printToFile("Triple_statistic.txt", scanResults.getResults()[i]);
        }
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("Triple_statistic.txt", true), "UTF-8")))){
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("error");
        }
    }

    public static void printToFile(String fileName, int n){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8")))){
            bufferedWriter.write(Integer.toString(n) + "    ");
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("error");
        }
    }

    public static void printToConsole(ScanResults scanResults){
        if(scanResults.getencoderName().equals("Humming")){
            printToConsoleHumming(scanResults);
        }
        else {
            printToConsoleTriple(scanResults);
        }
    }

    public static void printToConsoleHumming(ScanResults scanResults){
        System.out.println("Humming statistic\n" +
                "number of correction - quantity - percent");
        for (int i = 0; i < 8; i++) {

            System.out.println((i + 1) + ") - " + scanResults.results[i] + " - " + scanResults.results[i] * 100 / scanResults.number + "%");
        }
//        System.out.println("2) - " + scanResults.results[1] + " - " + scanResults.results[1] * 100 / scanResults.number + "%");
//        System.out.println("3) - " + scanResults.results[2] + " - " + scanResults.results[2] * 100 / scanResults.number + "%");
//        System.out.println("4) - " + scanResults.results[3] + " - " + scanResults.results[3] * 100 / scanResults.number + "%");
//        System.out.println("5) - " + scanResults.results[4] + " - " + scanResults.results[4] * 100 / scanResults.number + "%");
//        System.out.println("6) - " + scanResults.results[5] + " - " + scanResults.results[5] * 100 / scanResults.number + "%");
//        System.out.println("7) - " + scanResults.results[6] + " - " + scanResults.results[6] * 100 / scanResults.number + "%");
//        System.out.println("8) - " + scanResults.results[7] + " - " + scanResults.results[7] * 100 / scanResults.number + "%");
    }

    public static void printToConsoleTriple(ScanResults scanResults){
        System.out.println("\nTriple statistic\n" +
                "number of correction - quantity - percent");
        for (int i = 0; i < 4; i++) {
            System.out.println((i + 1) + ") - " + scanResults.results[i] + " - " + scanResults.results[i] * 100 / scanResults.number + "%");
        }
    }

    public static void printToFileHumming_Correct_nonCorrect(ScanResults scanResults){
        int []sum = new int[2];
        sum[0] = 0;
        sum[1] = 0;
        for (int i = 0; i < 4; i++) {
            sum[0] = sum[0] + scanResults.getResults()[i];
        }
        for (int i = 4; i < 8; i++) {
            sum[1] = sum[1] + scanResults.getResults()[i];
        }
            ScanResults.printToFile("Humming_Correct.txt", sum[0]);
            ScanResults.printToFile("Humming_nonCorrect.txt", sum[1]);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("Humming_Correct.txt", true), "UTF-8")))){
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("error");
        }
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("Humming_nonCorrect.txt", true), "UTF-8")))){
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("error");
        }
    }

    public static void printToFileTriple_Correct_nonCorrect(ScanResults scanResults){
        int []sum = new int[2];
        sum[0] = 0;
        sum[1] = 0;
        for (int i = 0; i < 2; i++) {
            sum[0] = sum[0] + scanResults.getResults()[i];
        }
        for (int i = 2; i < 4; i++) {
            sum[1] = sum[1] + scanResults.getResults()[i];
        }
        ScanResults.printToFile("Triple_Correct.txt", sum[0]);
        ScanResults.printToFile("Triple_nonCorrect.txt", sum[1]);
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("Triple_nonCorrect.txt", true), "UTF-8")))){
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("error");
        }
        try(BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream("Triple_Correct.txt", true), "UTF-8")))){
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            System.out.println("error");
        }
    }

    public static void delFile(){
        try{
            File file = new File("Triple_nonCorrect.txt");
            file.delete();
            file = new File("Triple_Correct.txt");
            file.delete();
            file = new File("Humming_Correct.txt");
            file.delete();
            file = new File("Humming_nonCorrect.txt");
            file.delete();
        }catch (Exception e){

        }
    }

}
