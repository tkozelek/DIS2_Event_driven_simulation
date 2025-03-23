package kozelek;


import kozelek.simulation.Simulation;

public class Main {
    public static void main(String[] args) {
        Simulation sim = new Simulation(100,5L, new int[]{2, 2, 2});
        sim.setSpeed(1000);
        sim.simuluj();
    }
}