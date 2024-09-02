package org.example;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AppTest {
    @Test
    public void appHasAGreeting() {
        App classUnderTest = new App();
        assertThat("app should have a greeting", classUnderTest.getGreeting(), is(notNullValue()));
    }
}
