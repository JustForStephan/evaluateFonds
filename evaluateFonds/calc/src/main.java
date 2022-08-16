import java.io.*;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class main {

    static String fileName = "/home/felix/Dokumente/FelixStephan/SoftwareDevelopment/projects/evaluateFonds/evaluateFonds/parameters.txt";
    static float relative_costs;
    static float relative_costs_additional;
    static int risk;
    static float sustainability_rate;
    static boolean sustainable;
    static boolean global;
    static float[] price_per_year = new float[10];

    public static void main(String[] args) throws IOException {

        String file_content = "";
        try {
            File file = new File(fileName);
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine()) {
                file_content += scan.nextLine() + "\n";
            }
            scan.close();
        } catch (IOException e) {
            System.out.println("ERROR: File not found!");
        } finally {
            extractParams(file_content);

        }
        float a = average(price_per_year);
        float eval = getBest(relative_costs,relative_costs_additional,a, risk, sustainable);
        String output = file_content+"\n------\nAverage:" + a + "\nevaluation_of_parameters:"+eval;
        Files.writeString(Paths.get(fileName), output);
        System.out.println("Average:"+a);
        System.out.println("respective_evaluation:"+eval);

    }

    public static float average(float[] data) {
        float all = 0;
        for (int i = 0; i < data.length; i++) {
            all += data[i];
        }
        return all / data.length;
    }

    private static void extractParams(String input) {
        String[] lines = input.split("\n");
        relative_costs = Float.valueOf(lines[0].split(":")[1]).floatValue();
        relative_costs_additional = Float.valueOf(lines[1].split(":")[1]).floatValue();
        risk = Integer.parseInt(lines[2].split(":")[1]);
        sustainability_rate = Float.valueOf(lines[3].split(":")[1]).floatValue();
        sustainable = Boolean.parseBoolean(lines[4].split(":")[1]);
        global = Boolean.parseBoolean(lines[5].split(":")[1]);
        String prices = input.split("----")[1];
        String[] price = prices.split("\n");
        for (int i = 1; i < price.length; i++) {
            price_per_year[i-1] = Float.valueOf(price[i].split(":")[1]).floatValue();
        }
    }
    // with the risk factor
    public static float getBest(float relCost, float relCostAdd, float average, boolean sus){
        float susRate = 1;
        if(sus==true) {susRate = sustainability_rate;}
        return average/(relCost+relCostAdd)*susRate;
    }
    //without risk factor
    public static float getBest(float relCost, float relCostAdd, float average, int risk, boolean sus){
        float susRate = 1;
        if(sus==true) {susRate = sustainability_rate;}
        return average/(relCost+relCostAdd)*susRate/Float.valueOf(risk).floatValue();
    }
}
