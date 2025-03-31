package kozelek;

import kozelek.config.Constants;
import kozelek.gui.controller.MainController;
import kozelek.gui.view.MainWindow;
import kozelek.simulation.Simulation;

public class Main {
    public static void main(String[] args) {
//        Simulation sim = new Simulation(10000, null, new int[]{2, 2, 18});
//        sim.setSpeed(Constants.MAX_SPEED);
//        double start = System.currentTimeMillis();
//        sim.simuluj();
//        double end = System.currentTimeMillis();
//        System.out.printf("Time: %.2fms\n", end - start);

        MainWindow win = new MainWindow();
        MainController controller = new MainController(win);


//        HashMap<OrderType, Double> probabilities = new HashMap<>();
//        probabilities.put(OrderType.TABLE, 0.5);
//        probabilities.put(OrderType.CHAIR, 0.15);
//        probabilities.put(OrderType.CUPBOARD, 0.35);
//
//        EnumGenerator orderTypeGenerator = new EnumGenerator(probabilities, new SeedGenerator());
//        long chair = 0, table = 0, cupboard = 0;
//        int num = 1_000_000_000;
//        for (int i = 0; i < num; i++) {
//            switch ((OrderType) orderTypeGenerator.sample()) {
//                case OrderType.CHAIR -> ++chair;
//                case OrderType.TABLE -> ++table;
//                case OrderType.CUPBOARD -> ++cupboard;
//            }
//        }
//        System.out.printf("%f %f %f", (double)table / num, (double)chair / num, (double)cupboard / num);


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