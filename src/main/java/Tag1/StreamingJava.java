package Tag1;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class StreamingJava {
    // Aufgabe 2) a)

    /**
     * @param list
     * @return a stream that contains all elements of the lists in the list
     */
    public static <E> Stream<E> flatStreamOf(List<List<E>> list) {

        return list.stream().flatMap(List::stream);


    }

    // Aufgabe 2) b)

    /**
     * @param stream
     * @return a stream that contains all elements of the streams in the stream
     */

    public static <E> Stream<E> mergeStreamsOf(Stream<Stream<E>> stream) {
        return stream.reduce(Stream::concat).orElseGet(Stream::empty);
    }

    // Aufgabe 2) c)

    /**
     * @param list
     * @return the minimum element of all lists
     */

    public static <E extends Comparable<? super E>> E minOf(List<List<E>> list) {
        // TODO return the minimum element of all lists

        return list.parallelStream().flatMap(List::stream).min(Comparable::compareTo).orElseThrow(NoSuchElementException::new);
    }

    // Aufgabe 2) d)

    /**
     * @param stream
     * @param predicate
     * @return the last element of the stream that matches the predicate
     */
    public static <E> E lastWithOf(Stream<E> stream, Predicate<? super E> predicate) {

        return stream.filter(predicate).reduce((first, second) -> second).orElse(null);
    }

    // Aufgabe 2) e)

    /**
     * @param stream
     * @param count
     * @return a set of the first count elements of the stream
     */
    public static <E> Set<E> findOfCount(Stream<E> stream, int count) {
        Map<E, Long> elementCounts = stream.collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return elementCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    // Aufgabe 2) f)

    /**
     * @param strings
     * @return a stream of the lengths of the strings
     */
    public static IntStream makeStreamOf(String[] strings) {
        // TODO

        return Arrays.stream(strings).flatMapToInt(String::chars);

    }

//-------------------------------------------------------------------------------------------------

    // Aufgabe 3) a)

    /**
     * @param path
     * @return a stream of the lines of the file
     */
    public static Stream<String> fileLines(String path) {
        Path filePath = Paths.get(path);
        try {
            BufferedReader reader = Files.newBufferedReader(filePath);
            reader.readLine();

            Stream<String> lines = reader.lines()
                    .onClose(() -> {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
        // String filePath = "NaturalGasBilling.csv";
        // Stream<String> linesStream = fileLines(filePath);
        // linesStream.forEach(System.out::println);
    }


    // Aufgabe 3) b)

    /**
     * @param lines
     * @return the average cost of all lines
     */
    public static double averageCost(Stream<String> lines) {
        //

        return lines
                .mapToDouble(line -> {
                    String[] columns = line.split(",");
                    if (columns.length >= 13) {
                        try {
                            return Double.parseDouble(columns[12]);
                        } catch (NumberFormatException e) {
                            return 0.0;
                        }
                    }
                    return 0.0;
                })
                .average()
                .orElse(0.0);

    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\tamma\\Desktop\\ProgramierPraktikum\\src\\Data\\NaturalGasBilling.csv";
        Stream<String> linesStream = fileLines(filePath);
       // linesStream.forEach(System.out::println);
      // System.out.println(averageCost(linesStream));
       System.out.println(countCleanEnergyLevy(linesStream));
       orderByInvoiceDateDesc(fileLines(filePath)).forEach(System.out::println);


    }

    // Aufgabe 3) c)
    public static long countCleanEnergyLevy(Stream<String> lines) {
        // TODO

        return lines
                .map(line -> {
                    String[] columns = line.split(",");
                    if (columns.length >= 11) {
                        try {
                            double cleanEnergyLevy = Double.parseDouble(columns[10]);
                            return cleanEnergyLevy <= 0.0;
                        } catch (NumberFormatException e) {
                            return true;
                        }
                    }
                    return true;
                })
                .filter(Boolean::booleanValue)
                .count();
    }

    // Aufgabe 3) d)
    // TODO:
    //  1. Create record "NaturalGasBilling".
    //  2. Implement static method: "Stream<NaturalGasBilling> orderByInvoiceDateDesc(Stream<String> stream)".
    public record NaturalGasBilling(String invoiceDate, String FromDate, String ToDate, int BillingDays,
                                    double BilledGJ, double BasicCharge,
                                    double DeliveryCharges, double SNT, double CommodityCharges, double Tax,
                                    double CleanEnergyLevy, double CarbornTax, double Amount) {
    }

    public static Stream<NaturalGasBilling> orderByInvoiceDateDesc(Stream<String> stream) {
        return stream
                .skip(1)
                .map(line -> {
                            String[] columns = line.split(",");
                            if (columns.length >= 6) {
                                try {
                                    String invoiceDate = columns[0];
                                    String FromDate = columns[1];
                                    String ToDate = columns[2];
                                    int BillingDays = Integer.parseInt(columns[3]);
                                    double BilledGJ = Double.parseDouble(columns[4]);
                                    double BasicCharge = Double.parseDouble(columns[5]);
                                    double DeliveryCharges = Double.parseDouble(columns[6]);
                                    double SNT = Double.parseDouble(columns[7]);
                                    double CommodityCharges = Double.parseDouble(columns[8]);
                                    double Tax = Double.parseDouble(columns[9]);
                                    double CleanEnergyLevy = Double.parseDouble(columns[10]);
                                    double CarbonTax = Double.parseDouble(columns[11]);
                                    double Amount = Double.parseDouble(columns[12]);


                                    return new NaturalGasBilling(invoiceDate, FromDate, ToDate, BillingDays, BilledGJ, BasicCharge,
                                            DeliveryCharges, SNT, CommodityCharges, Tax, CleanEnergyLevy, CarbonTax, Amount);
                                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                    return null;
                                }
                            }
                            return null;
                        }
                ).filter(Objects::nonNull)
                .sorted(Comparator.comparing(NaturalGasBilling::invoiceDate).reversed());
    }

    // Aufgabe 3) e)
    // TODO: Implement object method: "Stream<Byte> toBytes()" for record "NaturalGasBilling".
    /*public Stream<Byte> toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(100);

        buffer.put(invoiceDate.getBytes());
        buffer.put(FromDate.getBytes());
        buffer.put(ToDate.getBytes());
        buffer.putInt(BillingDays);
        buffer.putDouble(BilledGJ);
        buffer.putDouble(BasicCharge);
        buffer.putDouble(DeliveryCharges);
        buffer.putDouble(SNT);
        buffer.putDouble(CommodityCharges);
        buffer.putDouble(Tax);
        buffer.putDouble(CleanEnergyLevy);
        buffer.putDouble(CarbornTax);
        buffer.putDouble(Amount);


        buffer.flip();

        return Stream.generate(buffer::get)
                .limit(buffer.remaining());
    }
*/

    // Aufgabe 3) f)
    // TODO: Implement static method: "Stream<Byte> serialize(Stream<NaturalGasBilling> stream)".

    // Aufgabe 3) g)
    // TODO: Implement static method: "Stream<NaturalGasBilling> deserialize(Stream<Byte> stream)".
    // TODO: Execute the call: "deserialize(serialize(orderByInvoiceDateDesc(fileLines(Datei aus f))))"
    // TODO: in a main Method and print the output to the console.

    // Aufgabe 3) h)
    public static Stream<File> findFilesWith(String dir, String startsWith, String endsWith, int maxFiles) {
        // TODO

        return null;
    }
}
