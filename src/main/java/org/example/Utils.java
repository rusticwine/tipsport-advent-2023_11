package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {


    public static List<Integer> getIndexes(String str, char searchedChar) {
        return IntStream
                .iterate(str.indexOf(searchedChar), index -> index >= 0, index -> str.indexOf(searchedChar, index + 1))
                .boxed()
                .collect(Collectors.toList());
    }


    public static Set<Integer> generateIndiciesTo(int size) {
        return new HashSet<>(IntStream.range(0, --size).boxed().collect(Collectors.toList()));
    }
}
