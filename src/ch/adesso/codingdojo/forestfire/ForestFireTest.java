package ch.adesso.codingdojo.forestfire;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.adesso.codingdojo.forestfire.ForestFire.PositiveIntRandom;
import static ch.adesso.codingdojo.forestfire.ForestFire.*;

public class ForestFireTest {

	private static final byte[][] ALL_EMPTY = new byte[][] {
        { EMPTY, EMPTY, EMPTY },
        { EMPTY, EMPTY, EMPTY },
        { EMPTY, EMPTY, EMPTY }
    };
	private static final byte[][] ALL_TREE = new byte[][] {
        { TREE, TREE, TREE },
        { TREE, TREE, TREE },
        { TREE, TREE, TREE }
    };
	private static final byte[][] ALL_FIRE = new byte[][] {
        { FIRE, FIRE, FIRE },
        { FIRE, FIRE, FIRE },
        { FIRE, FIRE, FIRE }
    };
	private static final byte[][] ONLY_CENTER_FIRE = new byte[][] {
        { EMPTY, EMPTY, EMPTY },
        { EMPTY, FIRE, EMPTY },
        { EMPTY, EMPTY, EMPTY }
    };

	@Test
	public void process_FromEmptyToEmpty() {
		verify(ALL_EMPTY, ALL_EMPTY, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromEmptyToTree() {
		verify(ALL_EMPTY, ALL_TREE, 0);
	}

	@Test
	public void process_FromTreeToTree() {
		verify(ALL_TREE, ALL_TREE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire() {
		verify(ALL_TREE, ALL_FIRE, 0);
	}

	@Test
	public void process_FromFireToEmpty() {
		verify(ALL_FIRE, ALL_EMPTY, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_TopLeft() {
		byte[][] start = new byte[][] {
		        { FIRE, EMPTY, EMPTY },
		        { EMPTY, TREE, EMPTY },
		        { EMPTY, EMPTY, EMPTY }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_Top() {
		byte[][] start = new byte[][] {
		        { EMPTY, FIRE, EMPTY },
		        { EMPTY, TREE, EMPTY },
		        { EMPTY, EMPTY, EMPTY }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_TopRight() {
		byte[][] start = new byte[][] {
		        { EMPTY, EMPTY, FIRE },
		        { EMPTY, TREE, EMPTY },
		        { EMPTY, EMPTY, EMPTY }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_Left() {
		byte[][] start = new byte[][] {
		        { EMPTY, EMPTY, EMPTY },
		        { FIRE, TREE, EMPTY },
		        { EMPTY, EMPTY, EMPTY }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_Right() {
		byte[][] start = new byte[][] {
		        { EMPTY, EMPTY, EMPTY },
		        { EMPTY, TREE, FIRE },
		        { EMPTY, EMPTY, EMPTY }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_BottomLeft() {
		byte[][] start = new byte[][] {
		        { EMPTY, EMPTY, EMPTY },
		        { EMPTY, TREE, EMPTY },
		        { FIRE, EMPTY, EMPTY }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_Bottom() {
		byte[][] start = new byte[][] {
		        { EMPTY, EMPTY, EMPTY },
		        { EMPTY, TREE, EMPTY },
		        { EMPTY, FIRE, EMPTY }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_FromTreeToFire_BottomRight() {
		byte[][] start = new byte[][] {
		        { EMPTY, EMPTY, EMPTY },
		        { EMPTY, TREE, EMPTY },
		        { EMPTY, EMPTY, FIRE }
	        };
		verify(start, ONLY_CENTER_FIRE, Integer.MAX_VALUE);
	}

	@Test
	public void process_Complex() {
		byte[][] start = new byte[][] {
		        { TREE, TREE, TREE, TREE, TREE },
		        { TREE, TREE, TREE, TREE, TREE },
		        { TREE, TREE, TREE, TREE, TREE },
		        { TREE, FIRE, TREE, TREE, TREE },
		        { TREE, TREE, TREE, TREE, TREE }
	        };
		byte[][] step1 = new byte[][] {
		        { TREE, TREE, TREE, TREE, TREE },
		        { TREE, TREE, TREE, TREE, TREE },
		        { FIRE, FIRE, FIRE, TREE, TREE },
		        { FIRE, EMPTY, FIRE, TREE, TREE },
		        { FIRE, FIRE, FIRE, TREE, TREE }
	        };
		verify(start, step1, Integer.MAX_VALUE);
		byte[][] step2 = new byte[][] {
		        { TREE, TREE, TREE, TREE, TREE },
		        { FIRE, FIRE, FIRE, FIRE, TREE },
		        { EMPTY, EMPTY, EMPTY, FIRE, TREE },
		        { EMPTY, EMPTY, EMPTY, FIRE, TREE },
		        { EMPTY, EMPTY, EMPTY, FIRE, TREE }
	        };
		verify(step1, step2, Integer.MAX_VALUE);
		byte[][] step3 = new byte[][] {
		        { FIRE, FIRE, FIRE, FIRE, FIRE },
		        { EMPTY, EMPTY, EMPTY, EMPTY, FIRE },
		        { EMPTY, EMPTY, EMPTY, EMPTY, FIRE },
		        { EMPTY, EMPTY, EMPTY, EMPTY, FIRE },
		        { EMPTY, EMPTY, EMPTY, EMPTY, FIRE }
	        };
		verify(step2, step3, Integer.MAX_VALUE);
		byte[][] step4 = new byte[][] {
		        { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
		        { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
		        { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
		        { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY },
		        { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY }
	        };
		verify(step3, step4, Integer.MAX_VALUE);
	}

	private void verify(byte[][] start, byte[][] expectedResult, final int randomResult) {
	    ForestFire forestFire = new ForestFire(start.length, start[0].length);
		for (int x = 0; x < start.length; x++) {
			for (int y = 0; y < start[0].length; y++) {
				forestFire.forest[x + 1][y + 1] = start[x][y];
			}
		}
		PositiveIntRandom random = forestFire.new PositiveIntRandom() {
			public int nextPositiveInt() {
				return randomResult;
			}
		};
		forestFire.nextX.set(2);
		forestFire.process(1, start.length + 1, random);
		for (int x = 0; x < expectedResult.length; x++) {
			for (int y = 0; y < expectedResult[0].length; y++) {
				assertEquals("x/y: " + x + "/" + y, expectedResult[x][y], forestFire.newForest[x + 1][y + 1]);
			}
		}
    }

}
