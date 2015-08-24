package br.com.esdras.sunshine.app;

import android.test.AndroidTestCase;

/**
 * Created by esdras on 23/08/15.
 */
public class TestPractice extends AndroidTestCase {

    //    Roda antes de cada teste.
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testThatDemonstratesAssertions() throws Throwable {
        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("X should be equal", a, c);
        assertTrue("Y should be true", d > a);
        assertFalse("Z should be false", a == b);

        if (b > d) {
            fail("XX should never happen");
        }
    }
    //    Roda depois de cada teste.
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
