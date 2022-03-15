import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class test {

    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(111000L);
        System.out.println(timestamp.getTime());
        System.out.println(timestamp);

    }
}
