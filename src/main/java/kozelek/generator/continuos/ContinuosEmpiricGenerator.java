package kozelek.generator.continuos;

import kozelek.generator.Distribution;
import kozelek.generator.IGenerator;
import kozelek.generator.SeedGenerator;

import java.util.Random;

public class ContinuosEmpiricGenerator implements IGenerator<Double> {
    private final ContinuosUniformGenerator[] generators;
    private final Random probRand;
    private final Distribution[] dists;

    public ContinuosEmpiricGenerator(Distribution[] dists, SeedGenerator seedGenerator) {
        this.generators = new ContinuosUniformGenerator[dists.length];
        this.dists = dists;

        for (int i = 0; i < dists.length; i++) {
            this.generators[i] = new ContinuosUniformGenerator(this.dists[i].getMin(), this.dists[i].getMax(), seedGenerator);
        }

        this.probRand = new Random(seedGenerator.sample());
    }

    @Override
    public Double sample() {
        double prob = this.probRand.nextDouble(); // vygenerujeme prob. sancu
        double sum = 0;
        for (int i = 0; i < this.dists.length; i++) {
            sum += this.dists[i].getProbability();

            if (sum > prob) {
                return this.generators[i].sample();
            }
        }
        throw new RuntimeException();
    }
}
