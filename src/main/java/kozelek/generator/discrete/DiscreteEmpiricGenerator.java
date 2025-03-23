package kozelek.generator.discrete;

import kozelek.generator.Distribution;
import kozelek.generator.IGenerator;
import kozelek.generator.SeedGenerator;

import java.util.Random;

public class DiscreteEmpiricGenerator implements IGenerator<Integer> {
    private final DiscreteUniformGenerator[] generators;
    private final Random probRand;
    private final Distribution[] dists;

    public DiscreteEmpiricGenerator(Distribution[] dists, SeedGenerator seedGenerator) {
        this.dists = dists;
        this.generators = new DiscreteUniformGenerator[dists.length];

        for (int i = 0; i < this.generators.length; i++) {
            this.generators[i] = new DiscreteUniformGenerator((int) this.dists[i].getMin(), (int) this.dists[i].getMax(), seedGenerator);
        }

        this.probRand = new Random(seedGenerator.sample());
    }

    @Override
    public Integer sample() {
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
