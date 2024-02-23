package org.example;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CosmosCalculator {


    final static char SPACE = '.';
    final static Character GALAXY = '#';


    public static int sumShotestPaths(String data) {
        try (BufferedReader reader = new BufferedReader(new StringReader(data))) {
            AtomicInteger yInflated = new AtomicInteger(0);
            List<Pair<Integer, Integer>> galaxiesOriginalObservation =  new ArrayList<>();
            String line = reader.readLine();
            Set<Integer> columnWithNoGalaxy = Utils.generateIndiciesTo(line.length());

            final int lineLength = line.length();
            final String EMPTY_SPACE_LINE = Character.toString(SPACE).repeat(lineLength);

            for ( AtomicInteger lineCounter = new AtomicInteger(0); line != null; line = reader.readLine(), lineCounter.incrementAndGet()) {
                //zadna galaxie na ose Y, pro dalsi radky v poradi se nafouknuti versmiru inkrementuje
                if (EMPTY_SPACE_LINE.equals(line)) {
                    yInflated.incrementAndGet();
                    continue;
                }

                validateLine(line, lineLength);

                List<Pair<Integer, Integer>> galaxyInLineIndexes = Utils.getIndexes(line, GALAXY).stream()
                        .peek(columnWithNoGalaxy::remove)
                        //model vesmiru, osa Y se roztahuje pri vytvareni, Y se roztahne pozdeji
                        .map(ind -> new ImmutablePair<Integer, Integer>(lineCounter.get() + yInflated.get(), ind))
                        .collect(Collectors.toList());

                galaxiesOriginalObservation.addAll(galaxyInLineIndexes);
            }

            List<Pair<Integer, Integer>> inflatedCosmos = inflateCosmosToWidth(galaxiesOriginalObservation, columnWithNoGalaxy.toArray(Integer[]::new));
            return sumShortestPaths(inflatedCosmos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Secte vzdalenosti kombinaci vsech bodu (bez poradi)
     *
     * @param inflatedCosmos - souradnice galaxii ve vesmiru
     * @return
     */
    private static int sumShortestPaths(List<Pair<Integer, Integer>> inflatedCosmos) {
        int shortestPathSum = 0;
        for (int i = 0; i < inflatedCosmos.size(); i++) {
            for (int j = i + 1; j < inflatedCosmos.size(); j++) {
                shortestPathSum += Math.abs(inflatedCosmos.get(i).getKey() - inflatedCosmos.get(j).getKey())
                        + Math.abs(inflatedCosmos.get(i).getValue() - inflatedCosmos.get(j).getValue());
            }
        }
        return shortestPathSum;
    }


    private static List<Pair<Integer, Integer>> inflateCosmosToWidth(List<Pair<Integer, Integer>> galaxiesOriginalObservation, Integer[] columnWithNoGalaxy) {
        return galaxiesOriginalObservation.stream()
                .sorted(Comparator.comparing(Pair::getRight))
                /* Arrays.binarySearch vraci jak moc se prifoukne dany sloupec, protoze Arrays.binarySearch vraci
                    souradnici vlozeni chybejici hodnoty do pole, ktere zde predstavuji sloupce bez galaxie vlozena
                    a v pripade chybejici hodnoty je vracena hodnota "(-(insertion point) - 1)"
                 */
                .map(coordinates -> new ImmutablePair<>(coordinates.getKey(),
                        coordinates.getValue() + Math.abs(Arrays.binarySearch(columnWithNoGalaxy, coordinates.getRight())) - 1))
                .collect(Collectors.toUnmodifiableList());
    }


    private static void validateLine(String line, int lineLength) {
        if (line.length() != lineLength) {
            throw new IllegalArgumentException(
                    String.format("line lengths differs, first line legnth %d, current line %s", lineLength, line));
        }

        if (StringUtils.countMatches(line, GALAXY) + StringUtils.countMatches(line, SPACE) != lineLength) {
            throw new IllegalArgumentException(
                    String.format("Invalid characters present, allowed [%ch, %s], line %s", GALAXY, SPACE, line));
        }
    }

}
