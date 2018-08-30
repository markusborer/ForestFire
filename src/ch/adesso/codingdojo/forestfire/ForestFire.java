package ch.adesso.codingdojo.forestfire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.WindowConstants;

public class ForestFire {

	private static final int SLEEP_TIME = 50;
	private static final int F = (int) (0.0001F * Integer.MAX_VALUE);
	private static final int P = (int) (0.03F * Integer.MAX_VALUE);
	private static final float TREE_PROB = 0.5F;

	static final byte EMPTY = 0;
	static final byte TREE = 1;
	static final byte FIRE = 10;
	private static final int[] EMPTY_COLOR = new int[] { 0, 0, 0 };
	private static final int[] TREE_COLOR = new int[] { 0, 255, 0 };
	private static final int[] FIRE_COLOR = new int[] { 255, 0, 0 };

	static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

	private Window window;
	private final int width;
	private final int height;
	byte[][] forest;
	byte[][] newForest;
	private List<Runnable> threads;
	private final Random random = new Random();
	AtomicInteger nextX = new AtomicInteger();

	public ForestFire(int width, int height) {
		this.width = width + 2;
		this.height = height + 2;
		initForest();
		initThreads();
		initAndShowWindow();
		doRandomPopulation();
	}

	private void initForest() {
		// Add one pixel around the forest to avoid boundary-check
		forest = new byte[width][height];
		newForest = new byte[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				forest[x][y] = EMPTY;
				newForest[x][y] = EMPTY;
			}
		}
	}

	private void doRandomPopulation() {
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				if (random.nextFloat() < TREE_PROB) {
					forest[x][y] =  TREE;
					window.setColor(x - 1, y - 1, TREE_COLOR);
				} else {
					forest[x][y] =  EMPTY;
					window.setColor(x - 1, y - 1, EMPTY_COLOR);
				}
			}
		}
		window.repaint();
	}

	private void initThreads() {
		threads = new ArrayList<>();
		for (int i = 0; i < THREAD_COUNT; i++) {
			final int startX = i + 1;
			final int endX = width - 1;
			final PositiveIntRandom threadRandom = new PositiveIntRandom();
			threads.add(new Runnable() {
				@Override
				public void run() {
					process(startX, endX, threadRandom);
				}
			});
		}
	}

	private void initAndShowWindow() {
		this.window = new Window(width - 2, height - 2);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public void processEndless() {
		try {
			ExecutorService exec = Executors.newWorkStealingPool();
			Collection<Future<?>> tasks = new LinkedList<Future<?>>();
			while (true) {
				nextX.set(THREAD_COUNT + 1);
				tasks.clear();
				for (int i = 0; i < THREAD_COUNT; i++) {
					tasks.add(exec.submit(threads.get(i)));
				}
				// wait for tasks completion
				for (Future<?> task : tasks) {
					task.get();
				}
				byte[][] temp = forest;
				forest = newForest;
				newForest = temp;
				window.repaint();
				Thread.sleep(SLEEP_TIME);
			}
		} catch (Exception e) {
		}
	}

	void process(int startX, int endX, PositiveIntRandom randomNumberGenerator) {
		byte oldValue;
		byte newValue;
		int[] color;
		for (int x = startX; x < endX; x = nextX.getAndIncrement()) {
			for (int y = 1; y < height - 1; y++) {
				oldValue = forest[x][y];
				newValue = oldValue;
				color = null;
				if (oldValue == EMPTY && randomNumberGenerator.nextPositiveInt() < P) {
					newValue = TREE;
					color = TREE_COLOR;
				} else if (oldValue == TREE &&
						(
							forest[x - 1][y - 1] == FIRE ||
					        forest[x][y - 1] == FIRE ||
					        forest[x + 1][y - 1] == FIRE ||
					        forest[x - 1][y] == FIRE ||
					        forest[x + 1][y] == FIRE ||
					        forest[x - 1][y + 1] == FIRE ||
					        forest[x][y + 1] == FIRE ||
					        forest[x + 1][y + 1] == FIRE ||
					        randomNumberGenerator.nextPositiveInt() < F
				        )) {
					newValue = FIRE;
					color = FIRE_COLOR;
				} else if (oldValue == FIRE) {
					newValue = EMPTY;
					color = EMPTY_COLOR;
				}
				newForest[x][y] = newValue;
				if (color != null) {
					window.setColor(x - 1, y - 1, color);
				}
			}
		}
	}

	class PositiveIntRandom {
		public int nextPositiveInt() {
			return ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
		}
	}
}