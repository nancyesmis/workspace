import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestTopcoderTest {
	private TestTopcoder top;
	@Before
	public void setUp() throws Exception {		
		top = new TestTopcoder();
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test
	public void testAdd() {
		assertEquals(top.add(1, 2), 3);
		assertEquals(top.add(3, 4), 7);
	}

}
