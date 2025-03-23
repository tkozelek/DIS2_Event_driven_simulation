package kozelek.generator.continuos;

import kozelek.generator.IGenerator;
import kozelek.generator.SeedGenerator;

import java.util.Random;

public class ContinuosTriangularGenerator implements IGenerator<Double> {
    private final Random rand;
    private final double min, max, modus;

    public ContinuosTriangularGenerator(double min, double max, double modus, SeedGenerator seedGenerator) {
        this.rand = new Random(seedGenerator.sample());
        this.min = min;
        this.max = max;
        this.modus = modus;
    }

    @Override
    public Double sample() {
        double u = rand.nextDouble();
        if (u < (modus - min) / (max - min)) {
            return min + Math.sqrt(u * (max - min) * (modus - min));
        } else {
            return max - Math.sqrt((1 - u) * (max - min) * (max - modus));
        }
    }
}
