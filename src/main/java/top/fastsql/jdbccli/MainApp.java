package top.fastsql.jdbccli;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Driver;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Chenjiazhi
 * 2018-01-29
 */
public class MainApp {
    private static Driver driver = null;
    private static String url = null;
    private static String userName = null;
    private static String password = null;

    private static JdbcTemplate jdbcTemplate = null;

    public static void main(String[] args) throws Exception {
        String db;
        while (true) {
            System.out.print("[DataBase Type [o/m/p] (o-Oracle/m-Mysql/p-Postgresql )]: ");
            Scanner scanner = new Scanner(System.in);
            db = scanner.nextLine();
            if ("o".equals(db)) {
                driver = (Driver) Class.forName("oracle.jdbc.OracleDriver").newInstance();
                break;
            } else if ("m".equals(db)) {
                driver = new com.mysql.cj.jdbc.Driver();
                break;
            } else if ("p".equals(db)) {
                driver = new org.postgresql.Driver();
                break;
            } else {
                System.out.println("No this type");
            }
        }

        System.out.print("[Short Jdbc Url ( eg. localhost:1522/dbname OR 192.168.0.202:1521:xe )]: ");
        Scanner scanner = new Scanner(System.in);
        String simpleUrl = scanner.next();
        if ("o".equals(db)) {
            url = "jdbc:oracle:thin:@{url}".replace("{url}", simpleUrl);
        } else if ("m".equals(db)) {
            url = "jdbc:mysql://{url}?useSsl=false".replace("{url}", simpleUrl);
        } else if ("p".equals(db)) {
            url = "jdbc:postgresql://{url}?stringtype=unspecified".replace("{url}", simpleUrl);
        }

        System.out.print("[Username] : ");
        userName = new Scanner(System.in).nextLine().trim();

        System.out.print("[Password] : ");
        password = new Scanner(System.in).nextLine().trim();

        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, url, userName, password);
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.getDataSource().getConnection();
        System.out.println("===========SUCCESS=========");
        System.out.println("[INFO] " + url + "  ");
        while (true) {
            System.out.println("[Please input sql] : ");
            String sql = new Scanner(System.in).nextLine();
            if (sql.equals("exit")) {
                break;
            }
            try {
                if (sql.toLowerCase().trim().startsWith("delete") || sql.toLowerCase().trim().startsWith("update")
                        || sql.toLowerCase().trim().startsWith("insert")) {
                    System.out.println("Effect row : " + jdbcTemplate.update(sql));
                } else {
                    List<Map<String, Object>> rowMaps = jdbcTemplate.queryForList(sql);
                    printMaps(rowMaps);
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    static void printMaps(List<Map<String, Object>> maps) {
        int i = 0;
        for (Map<String, Object> map : maps) {
            i++;
            System.out.println("-> " + i + " " + map);
        }
    }
}
