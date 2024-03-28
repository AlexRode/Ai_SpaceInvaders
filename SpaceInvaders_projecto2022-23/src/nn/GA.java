package nn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.swing.JPanel;

import space.Board;
import space.Commons;
import space.SpaceInvaders;

import java.util.List;

public class GA implements Commons {

	private static final int POPULATION_SIZE = 100;
	private static final int NUM_GENERATIONS = 100;
	private static final double MUTATION_RATE = 0.7;
	private static final int MAX_AMMO = 1000;
	private static final int MAX_SimpleFeedforwardNeuralNetworkS = 100;
	private static Random random = new Random();
	private static final int BOARD_WIDTH = 358;

	public GA() {

		// Initialize the population
		SimpleFeedforwardNeuralNetwork[] population = new SimpleFeedforwardNeuralNetwork[POPULATION_SIZE];
		SimpleFeedforwardNeuralNetwork chosen_one = new SimpleFeedforwardNeuralNetwork(STATE_SIZE, 6, NUM_ACTIONS);
		long seeds[] = new long [NUM_GENERATIONS] ;
		int seed_index = 0;
		long seed = 0;
		
//		chosen_one.initializeWeights();
//chosen_one.getRun();

		for (int i = 0; i < POPULATION_SIZE; i++) {

			SimpleFeedforwardNeuralNetwork guy = new SimpleFeedforwardNeuralNetwork(STATE_SIZE, 6, NUM_ACTIONS);
			guy.initializeWeights();
			population[i] = guy;

		}
		// Evolve the population for a fixed number of generations
		for (int i = 0; i < NUM_GENERATIONS; i++) {
			// Sort the population by fitness
			
			// Define a seed 
			int min = 100;
			int max = 1000;
			seed = (long) Math.floor(Math.random() * (max - min + 1) + min);
			System.out.printf("Seed Value: %d - ", (int) seed);
			seeds[i] = seed;
			
			
			// Create the next generation
			SimpleFeedforwardNeuralNetwork[] newPopulation = new SimpleFeedforwardNeuralNetwork[POPULATION_SIZE];
			for (int j = 0; j < POPULATION_SIZE; j++) {
				// Select two parents from the population
				SimpleFeedforwardNeuralNetwork parent1 = selectParent(population);
				SimpleFeedforwardNeuralNetwork parent2 = selectParent(population);
				// Crossover the parents to create a new child
				SimpleFeedforwardNeuralNetwork child = crossover(parent1, parent2);
				// Mutate the child
				mutate(child);
				
				
				child.getRun(seeds[i]);
				newPopulation[j] = child;
				if (newPopulation[j].getFitness() > chosen_one.getFitness()) {
					chosen_one = newPopulation[j];
					seed_index = i;
				}
			
			}

			// Replace the old population with the new population
			population = newPopulation;

			Arrays.sort(population);

			// Print the best solution of this generation
			System.out.println("Generation " + i + ": " + population[0].getFitness());
			// Check if we have found a solution
			if (population[0].getFitness() == 1000000.0) {
				break;
			}

			
		}
		// Print the best solution we found

		Arrays.sort(population);


		System.out.println("Best solution found: " + chosen_one.getFitness());
		System.out.printf("Seed Index: %d | Seed Value %d \n", (int) seed_index, (int) seeds[seed_index]);		
		SpaceInvaders.showControllerPlaying(chosen_one, seeds[seed_index]);

	}

	public static void main(String[] args) {

		new GA();
	}

	// Select a parent from the population using tournament selection
	private SimpleFeedforwardNeuralNetwork selectParent(SimpleFeedforwardNeuralNetwork[] population) {

		ArrayList<SimpleFeedforwardNeuralNetwork> tournament = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			tournament.add(population[(int) Math.random() * (POPULATION_SIZE)]);

		}
		Collections.sort(tournament);
		return tournament.get(0);
	}

	// Crossover two parents to create a new child
	private SimpleFeedforwardNeuralNetwork crossover(SimpleFeedforwardNeuralNetwork parent1,
			SimpleFeedforwardNeuralNetwork parent2) {

		double[] child = new double[parent1.getChromossomeSize()];

		for (int i = 0; i < parent1.getChromossomeSize(); i++) {
			if (random.nextDouble() < 0.5) {
				child[i] = parent1.getChromossome()[i];

			} else {
				child[i] = parent2.getChromossome()[i];
			}
		}
		SimpleFeedforwardNeuralNetwork childfinal = new SimpleFeedforwardNeuralNetwork(STATE_SIZE, 6, NUM_ACTIONS,
				child);
		return childfinal;
	}

	private void mutate(SimpleFeedforwardNeuralNetwork child) {

		if (random.nextDouble() < MUTATION_RATE) {
			int chr1 = (int) Math.random() * child.getChromossomeSize();
			int chr2 = (int) Math.random() * child.getChromossomeSize();
			double x = child.getChromossome()[chr1];
			child.getChromossome()[chr1] = child.getChromossome()[chr2];
			child.getChromossome()[chr2] = x;

		}
	}

}
