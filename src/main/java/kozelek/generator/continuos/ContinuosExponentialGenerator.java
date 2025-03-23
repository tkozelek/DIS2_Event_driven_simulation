package kozelek.generator.continuos;

import kozelek.generator.IGenerator;
import kozelek.generator.SeedGenerator;

import java.util.Random;

public class ContinuosExponentialGenerator implements IGenerator<Double> {
    private final double lambda;
    private final Random rand;


    public ContinuosExponentialGenerator(double lambda, SeedGenerator seedGenerator) {
        this.lambda = lambda;
        this.rand = new Random(seedGenerator.sample());
    }

    @Override
    public Double sample() {
        return -Math.log(1.0 - rand.nextDouble()) / lambda;
    }
}
