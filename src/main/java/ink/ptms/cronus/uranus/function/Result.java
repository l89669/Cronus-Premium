package ink.ptms.cronus.uranus.function;

public class Result {

    private final String start;
    private final String center;
    private final String end;

    public Result(String start, String center, String end) {
        this.start = start;
        this.center = center;
        this.end = end;
    }

    public boolean isEmpty() {
        return start.isEmpty() && end.isEmpty();
    }

    public String replace(String after) {
        return start + after + end;
    }

    public String getStart() {
        return start;
    }

    public String getCenter() {
        return center;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Result{" +
                "start='" + start + '\'' +
                ", center='" + center + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}