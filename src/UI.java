import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class UI {

    public CountDownLatch latch;
    private Data parametrs;
    private int progressLine;
    private boolean inProcess;
    private int signLenght;
    private String commandLineText;
    private int repeatTest;

    UI() {
        this.progressLine = 0;
        this.inProcess = false;
        this.signLenght = 1;
        this.repeatTest = 1;
    }

    private void commandMenu() {
        System.out.print("$ ");
        Scanner scanner = new Scanner(System.in);
        commandLineText = scanner.nextLine();
        String[] retval = commandLineText.split(" ");

        switch (retval[0]) {
            case "h":
            case "help": {
                help();
                break;
            }
            case "exit": {
                System.exit(0);
                break;
            }
            case "s": {
                ScanResults.print(ScanResults.getStatistic(retval[1]));
                break;
            }
            case "t":
            case "test": {
                testOptins(retval);
                break;
            }
            default: {
                System.out.println("    h or help for help");
                break;
            }
        }
        commandMenu();
    }


    public void help() {
        System.out.println("HELP:\n" +
                "[command] [quantity] [capacity] [option1] [option1_capacity] [interference] [interference_%] [option2] [option2_number]\n" +
                "\n" +
                "command:\n" +
                "t or test -- test\n" +
                "s -- get statistic and after space enter name of file\n" +
                "h or help -- help\n" +
                "exit -- exit from application\n" +
                "\n" +
                "quantity: - how many times test 1 signal (integer)\n" +
                "\n" +
                "capacity - split signal by bits\n" +
                "\n" +
                "option1:\n" +
                "-r - generate random signal whit [option1_capacity]\n" +
                "-f - read from file. Name of file = [option1_capacity]\n" +
                "-s - use bites from [option1_capacity] to generating signal\n" +
                "\n" +
                "option1_capacity:" +
                "if you use -r, enter how many bites will be in signal\n" +
                "if you use -f, enter name of file which have signal\n" +
                "if you use -s, enter your signal(example: 1010)\n" +
                "\n" +
                "interference:\n" +
                "-p - if you want to change interference of signal. default=1%" +
                "\n" +
                "interference% - set per cent of interference (default=1)\n" +
                "\n" +
                "option2:\n" +
                "-n - if you want repeat 'option2_number' times\n" +
                "\n" +
                "option2_number - how many times repeat test with current settings\n" +
                "\n");
    }

    private void testOptins(String[] retval) {
        parametrs = new Data();
        try {
            parametrs.setNumberOfTessts(Integer.parseInt(retval[1]));
            parametrs.setCapacity(Integer.parseInt(retval[2]));
        } catch (Exception e) {
            return;
        }

        try {
            if (retval[5] != null && retval[6] != null) {
                if (retval[5].equals("-p")) {
                    parametrs.setInterference(Integer.parseInt(retval[6]));
                    if (retval[7].equals("-n")) {
                        this.repeatTest = Integer.parseInt(retval[8]);
                    }
                } else {
                    if (retval[5].equals("-n")) {
                        this.repeatTest = Integer.parseInt(retval[6]);
                    }
                }
            }
        } catch (Exception e) {
        }

        switch (retval[3]) {
            case "-r": {
                System.out.print("generating random signal... ");
                if (retval[4] == null) {
                    return;
                }
                genRandomSignal(Integer.parseInt(retval[4]));
                UI.printToFile("results/Random_Signal.txt", parametrs.getSignal());
                signLenght = parametrs.getSignal().size();
                System.out.println("OK");
                startLoop();
                break;
            }
            case "-f": {
                System.out.print("read from file... ");
                if (retval[4] == null) {
                    return;
                }
                parametrs.setSignal(readFromFile(retval[4], parametrs.getCapacity()));
                signLenght = parametrs.getSignal().size();
                startLoop();
                break;
            }
            case "-s": {
                System.out.print("read from console... ");
                Data.ToBoolean(retval[4], parametrs.getCapacity());
                signLenght = parametrs.getSignal().size();
                startLoop();
                break;
            }
            default: {
                return;
            }
        }

    }

    private void startLoop() {
        ScanResults.delFile();
        for (int i = 0; i < this.repeatTest; i++) {
            startTest(i);
            ScanResults.print(ScanResults.getStatistic("results/Results_Humming.txt"));
            ScanResults.print(ScanResults.getStatistic("results/Results_Triple.txt"));
        }
    }

    private void startTest(int testNumber) {
        HummingTest.fileNewTest(commandLineText);
        TestTriple.fileNewTest(commandLineText);
        System.out.println("Test Number: " + testNumber);
        System.out.println("|                                               Testing:                                           |");
        inProcess = true;
        this.latch = new CountDownLatch(parametrs.getNumberOfTessts() * 2);
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
        List<Runnable> workers = new LinkedList<Runnable>();
        for (int i = 0; i < parametrs.getNumberOfTessts(); i++) {
            Iterator<ArrayList<Boolean>> iter = parametrs.getSignal().iterator();
            while (iter.hasNext()) {
                ArrayList<Boolean> tmp = iter.next();
                ArrayList<Boolean> tmp2 = (ArrayList)tmp.clone();
                new TestTriple(tmp, parametrs.getInterference(), this).run();
                new HummingTest(tmp2, parametrs.getInterference(), this).run();
            }
        }
//        workers.forEach(Runnable::run);
//        while (inProcess) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            System.out.println("bla");
            e.printStackTrace();
        }
//        executorService.shutdown();
    }


    private void genRandomSignal(int n) {
        SecureRandom secureRandom = new SecureRandom();
        ArrayList<Boolean> tmp = new ArrayList<Boolean>();
        for (int i = 0; i < n; i++) {
            tmp.add(secureRandom.nextBoolean());
            if (tmp.size() == parametrs.getCapacity()) {
                LinkedList<ArrayList<Boolean>> temp = parametrs.getSignal();
                temp.add(new ArrayList<Boolean>(tmp));
                parametrs.setSignal(temp);
                tmp = new ArrayList<Boolean>();
            }
        }
        if (tmp.size() != 0) {
            LinkedList<ArrayList<Boolean>> temp = parametrs.getSignal();
            temp.add(new ArrayList<Boolean>(tmp));
            parametrs.setSignal(temp);
            tmp = new ArrayList<Boolean>();
        }
    }

    public static LinkedList<ArrayList<Boolean>> readFromFile(String fileName, int cap) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String string = bufferedReader.readLine();
            String[] retval = string.split(" ");
            LinkedList<ArrayList<Boolean>> sign = new LinkedList<ArrayList<Boolean>>();
            ArrayList<Boolean> bool = new ArrayList<Boolean>();
            for (int i = 0; i < retval.length; i++) {
                bool.add(Boolean.parseBoolean(retval[i]));
                if (bool.size() == cap) {
                    sign.add(new ArrayList<Boolean>(bool));
                    bool = new ArrayList<Boolean>();
                }
            }
            if (bool.size() != 0) {
                sign.add(new ArrayList<Boolean>(bool));
                bool = new ArrayList<Boolean>();
            }
            System.out.println("OK");
            return sign;

        } catch (IOException e) {
        }
        System.out.println("error");
        return null;
    }

    public static void printToFile(String fileName, LinkedList<ArrayList<Boolean>> tmp) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), "UTF-8")))) {
            Iterator<ArrayList<Boolean>> iter1 = tmp.iterator();
            while (iter1.hasNext()) {
                Iterator<Boolean> iter2 = iter1.next().iterator();
                while (iter2.hasNext()) {
                    bufferedWriter.write(Boolean.toString(iter2.next()) + " ");
                }
            }
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("fatal error: print to file is failed");
        }
    }


    public synchronized void progress() {
        try {
            progressLine++;
            if (parametrs.getNumberOfTessts() >= 100) {
                if (progressLine % (parametrs.getNumberOfTessts() * signLenght / 50) == 0) {
                    System.out.print("#");
                }
            } else {
                System.out.print("#");
            }
            if ((progressLine / 2) == parametrs.getNumberOfTessts() * signLenght) {
                System.out.println();
                System.out.println("completed");
                progressLine = 0;
                inProcess = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        UI ui = new UI();
        System.out.println("    h or help for help");
        ui.commandMenu();
        System.exit(0);
    }

}

