import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Interval {


    /* ALTERNATE PRETTY SOLUTION
    return intervals == null ? 0 : (int) Arrays.stream(intervals)
            .flatMapToInt(interval -> IntStream.range(interval[0], interval[1]))
            .distinct()
            .count();
     */

    int sumIntervals(int[][] input) {

        if (input == null) {
            return 0;
        }

        List<ValueRange> ranges = Arrays.stream(input)
                .map(interval -> ValueRange.of((long) interval[0], (long) interval[1]))
                .sorted(Comparator.comparing(ValueRange::getMinimum).thenComparing(ValueRange::getMaximum))
                .distinct()
                .collect(Collectors.toList());
        List<ValueRange> merged = mergeAllIntervals(ranges);
        return merged.stream().mapToInt(range -> (int) range.getMaximum() - (int) range.getMinimum()).sum();
    }

    private List<ValueRange> mergeAllIntervals(List<ValueRange> ranges) {
        List<ValueRange> result = new ArrayList<>();
        for (ValueRange range : ranges) {
            if (!result.isEmpty() && rangeCanBeMerged(result, range)) {
                ValueRange merged = mergeIntervals(result, range);
                result.remove(result.size() - 1);
                result.add(merged);
            } else {
                result.add(range);
            }
        }
        return result;
    }

    private boolean rangeCanBeMerged(List<ValueRange> result, ValueRange range) {
        return result.get(result.size() - 1).isValidValue(range.getMinimum());
    }

    private ValueRange mergeIntervals(List<ValueRange> result, ValueRange range) {
        return ValueRange.of(result.get(result.size() - 1).getMinimum(), Math.max(result.get(result.size() - 1).getMaximum(), range.getMaximum()));
    }
}