package practice.java.mockito;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by patrick on 6/25/2015.
 */

public class UnitTestSample {

   @Test
    public void test1(){
       MockitoSample mockitoSample = mock(MockitoSample.class);
       when(mockitoSample.testMethod()).thenReturn(true);
       when(mockitoSample.testInt()).thenReturn(2);
       assertEquals(2,mockitoSample.testInt());
    }

    @Test
    public void test2(){
       String retult = "one"+"two";
       assertEquals("onetwo",retult);
        Assert.assertFalse(false);
        Assert.assertTrue(true);
    }
}