package kozelek.generator.continuos;

import kozelek.generator.IGenerator;
import kozelek.generator.SeedGenerator;

import java.util.Random;

public class ContinuosUniformGenerator implements IGenerator<Double> {
    private final double min;
    private final double max;
    private final Random random;

    public ContinuosUniformGenerator(double min, double max, SeedGenerator seedGenerator) {
        this.min = min;
        this.max = max;
        this.random = new Random(seedGenerator.sample());
    }

    @Override
    public Double sample() {
        return this.random.nextDouble(min, max);
    }
}
