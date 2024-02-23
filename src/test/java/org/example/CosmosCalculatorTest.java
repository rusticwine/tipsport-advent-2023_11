package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class CosmosCalculatorTest {


    private static final String TEST_DATA_VALID =
            "...#......\n" +
            ".......#..\n" +
            "#.........\n" +
            "..........\n" +
            "......#...\n" +
            ".#........\n" +
            ".........#\n" +
            "..........\n" +
            ".......#..\n" +
            "#...#.....";


    @Test
    @DisplayName("Pass valid data")
    void testSumShotestPaths() {
        int sumShortestPaths = CosmosCalculator.sumShotestPaths(TEST_DATA_VALID);
        assertEquals(374, sumShortestPaths);
    }


    @ParameterizedTest
    @MethodSource("provideInvalidData")
    @DisplayName("Pass invalid data, throw IllegalArgumentException")
    void testSumShotestPathsTest_throwsIllegalArgumentException(String data) {
        assertThrows(IllegalArgumentException.class, ()-> CosmosCalculator.sumShotestPaths(data));
    }


    private static Stream<Arguments> provideInvalidData() {
        return Stream.of(
                Arguments.of("...#......\n" +
                        ".......#..\n" +
                        "#.........."),
                Arguments.of("...#......\n" +
                        ".......#..\n" +
                        "...#...a..\n" +
                        ".......#..\n")
        );
    }


    @Test
    void testIndicies() {
        var temp = Utils.getIndexes("koma", 'c');
        System.out.println(temp);
    }

}