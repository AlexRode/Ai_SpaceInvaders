package randomControler;

import java.util.Random;

import controllers.GameController;
import nn.Commons;
import nn.SimpleFeedforwardNeuralNetwork;
import space.SpaceInvaders;

public class PlayRandomController implements Commons{
	public static void main(String[] args) {
	


		GameController c = new RandomController(new Random());
		//GameController c = new SimpleFeedforwardNeuralNetwork(STATE_SIZE, 6, NUM_ACTIONS);
		
		SpaceInvaders.showControllerPlaying(c,10);
	}
}
