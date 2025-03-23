package kozelek;


import kozelek.generator.SeedGenerator;
import kozelek.generator.continuos.ContinuosExponentialGenerator;
import kozelek.generator.continuos.ContinuosTriangularGenerator;
import kozelek.simulation.Simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        Simulation sim = new Simulation(10,5L, new int[]{2, 2, 2});
        sim.setSpeed(1000);
        sim.simuluj();




//        SeedGenerator sg = new SeedGenerator();
//        ContinuosExponentialGenerator orderArrivalGenerator = new ContinuosExponentialGenerator(1 / 1800.0, sg);
//        ContinuosTriangularGenerator moveToStorageGenerator = new ContinuosTriangularGenerator(60, 480, 120, sg);
//        try {
//            PrintWriter pwOrder = new PrintWriter(new File("order.txt"));
//            for (int i = 0; i < 100000; i++) {
//                pwOrder.write(orderArrivalGenerator.sample() + "\n");
//            }
//            pwOrder.close();
//
//            PrintWriter pwStorage = new PrintWriter(new File("storage.txt"));
//            for (int i = 0; i < 100000; i++) {
//                pwStorage.write(moveToStorageGenerator.sample() + "\n");
//            }
//            pwStorage.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}