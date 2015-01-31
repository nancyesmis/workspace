import junit.framework.TestCase;


public class PaperTest extends TestCase {
	private Paper p = null;
	protected void setUp() throws Exception {
		super.setUp();
	    p = new Paper();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTitle() {
		assertEquals(null, p.getTitle());
	}

}
