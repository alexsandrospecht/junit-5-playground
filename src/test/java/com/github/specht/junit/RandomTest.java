package com.github.specht.junit;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@DisplayName("Random tests")
public class RandomTest {

    @Test
    @DisplayName("Random number between 0 and 10")
    void random10() {
        final int random = RandomUtils.nextInt(0, 10);

        assertAll(
                () -> assertTrue(random < 10),
                () -> assertFalse(random < 0)
        );
    }

    @RepeatedTest(10)
    @DisplayName("This will repeat 10 times")
    void repeatedTest(RepetitionInfo repetitionInfo) {
        final int currentRepetition = repetitionInfo.getCurrentRepetition();

        System.out.printf("Running %d", currentRepetition);
        assertTrue(currentRepetition <= 10);
    }

    @ParameterizedTest(name = "Number {arguments} is even")
    @ValueSource(ints = {2, 4, 6, 8, 10})
    void evenNumbers(int value) {
        assertEquals(0, value % 2);
    }

    @ParameterizedTest(name = "Number {arguments} is odd")
    @ValueSource(ints = {1, 3, 5, 7, 9})
    void oddNumbers(int value) {
        assertEquals(1, value % 2);
    }

    @ParameterizedTest(name = "List must contain {arguments} value")
    @MethodSource("listWords")
    @DisplayName("This will use listWords() values")
    void listMethodSourceProvider(String string) {
        assertTrue(Arrays.asList("hello", "world").contains(string));
    }

    static List<String> listWords() {
        return Arrays.asList("hello", "world");
    }

    @Test
    @DisplayName("Exception message assertion")
    void exceptionTesting() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Some message");
        });
        assertEquals("Some message", exception.getMessage());
    }

    @Test
    @DisplayName("Assuming that false condition")
    void assuming() {
        assumingThat("MyName".equals("YourName"),
                () -> {
                    assertEquals(2, 3); // error
                });

        assertTrue(true);
    }

    @Test
    @DisplayName("Assuming that true condition")
    void assumingTrue() {
        assumingThat("Name".equals("Name"),
                () -> {
                    assertEquals(2, 2);
                });

        assertTrue(true);
    }

    @DisplayName("Disabled on OS")
    @DisabledOnOs({ OS.LINUX, OS.MAC })
    void windowsSucks() {
        Assertions.fail();
    }

    @DisplayName("Disabled on JRE 8")
    @DisabledOnJre(JRE.JAVA_8)
    void java6Sucks() {
        Assertions.fail();
    }

    @Test
    @EnabledIf("4 * 20 == 80")
    void creezTime() {
        final String message = "Creez Time!";
        assertTrue(message.length() > 5);
    }

}
