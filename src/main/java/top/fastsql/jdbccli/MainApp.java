package top.fastsql.jdbccli;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.Driver;
import java.sql.SQLException;
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


        url = args[0];

        boolean isSucess = setDriverByDbType(url);
        if (!isSucess) {
            System.err.println("仅持支mysql,oracle,postgresql");
        }


        userName = args[1];

        password = args[2];


        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, url, userName, password);
        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.getDataSource().getConnection();
        System.out.println("===========SUCCESS=========");
        System.out.println("[INFO] " + url + "  ");
        while (true) {
            System.out.println();
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

    private static void printMaps(List<Map<String, Object>> maps) {
        int i = 0;
        for (Map<String, Object> map : maps) {
            i++;
            System.out.println("-> " + i + " " + map);
        }
    }

    /**
     * 根据类型设置Driver
     */
    private static boolean setDriverByDbType(String url) throws SQLException {
        if (url.contains("oracle")) {
            driver = new oracle.jdbc.OracleDriver();
            return true;
        } else if (url.contains("mysql")) {
            driver = new com.mysql.cj.jdbc.Driver();
            return true;
        } else if (url.contains("postgresql")) {
            driver = new org.postgresql.Driver();
            return true;
        } else {
            return false;
        }
    }

}
