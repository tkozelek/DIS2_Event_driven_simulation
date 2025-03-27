package kozelek;


import kozelek.entity.order.OrderType;
import kozelek.generator.EnumGenerator;
import kozelek.generator.SeedGenerator;
import kozelek.generator.continuos.ContinuosExponentialGenerator;
import kozelek.generator.continuos.ContinuosTriangularGenerator;
import kozelek.generator.continuos.ContinuosUniformGenerator;
import kozelek.gui.controller.MainController;
import kozelek.gui.view.MainWindow;
import kozelek.simulation.Simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        Simulation sim = new Simulation(10,5L, new int[]{2, 2, 20});
//        sim.setSpeed(10);
//        sim.simuluj();

        MainWindow win = new MainWindow();
        MainController controller = new MainController(win);


//        Map<OrderType, Double> probabilities = new HashMap<>();
//        probabilities.put(OrderType.TABLE, 0.5);
//        probabilities.put(OrderType.CHAIR, 0.15);
//        probabilities.put(OrderType.CUPBOARD, 0.35);
//
//        EnumGenerator orderTypeGenerator = new EnumGenerator(probabilities, new SeedGenerator());
//        int chair = 0, table = 0, cupboard = 0;
//        int num = 10_000_000;
//        for (int i = 0; i < num; i++) {
//            switch ((OrderType) orderTypeGenerator.sample()) {
//                case OrderType.CHAIR -> ++chair;
//                case OrderType.TABLE -> ++table;
//                case OrderType.CUPBOARD -> ++cupboard;
//            }
//        }
//        System.out.printf("%.4f %.4f %.4f", (double)table / num, (double)chair / num, (double)cupboard / num);


//        ContinuosUniformGenerator morenie = new ContinuosUniformGenerator(600 * 60, 700 * 60, new SeedGenerator());
//        try {
//            PrintWriter pwStorage = new PrintWriter(new File("enum.txt"));
//            for (int i = 0; i < 1000000; i++) {
//                pwStorage.write(orderTypeGenerator.sample() + "\n");
//            }
//            pwStorage.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}