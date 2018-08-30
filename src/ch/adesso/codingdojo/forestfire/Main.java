package ch.adesso.codingdojo.forestfire;

public class Main {

	private static final int WIDTH = 1000;
	private static final int HEIGHT = 1000;

	public static void main(String[] args) {
		ForestFire fire = new ForestFire(WIDTH, HEIGHT);
		fire.processEndless();
	}

}
