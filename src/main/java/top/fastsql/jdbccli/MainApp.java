package top.fastsql.jdbccli;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
    private static DbType dbType = null;

    private static JdbcTemplate jdbcTemplate = null;


    private enum DbType {
        ORACLE, MY_SQL, POSTGRESQL;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome jdbc-cli......");
        System.out.print("Select Mode ~ Mode 1-Use properties file; Mode 2-Input Parameters. Input 1/2 (2) :");
        String mode = new Scanner(System.in).nextLine().trim();
        if ("1".equals(mode)) {
            System.out.print("Input Config File Path (./config.properties) :");
            String file = new Scanner(System.in).nextLine().trim();
            if (file.equals("")) {
                file = "./config.properties";
            }
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(file)));

            System.out.print("Input Config  Id (db1) :");
            String id = new Scanner(System.in).next();
            String type = (String) properties.get(id + "." + "type");
            boolean success = setDriverByDbType(type);
            if (!success) {
                System.exit(0);
            }
            setUrl((String) properties.get(id + "." + "url"));
            userName = (String) properties.get(id + "." + "username");
            password = (String) properties.get(id + "." + "password");
        } else {
            String db;
            while (true) {
                System.out.print("Input DataBase Type o-Oracle/m-Mysql/p-Postgresql (p): ");
                Scanner scanner = new Scanner(System.in);
                db = scanner.nextLine();
                boolean success = setDriverByDbType(db);
                if (success) {
                    break;
                }
            }

            System.out.print("[Jdbc Url (Also Can short style~ localhost:1522/dbname OR 192.168.0.202:1521:xe )]: ");
            Scanner scanner = new Scanner(System.in);
            String simpleUrl = scanner.next();
            setUrl(simpleUrl);

            System.out.print("[Username] : ");
            userName = new Scanner(System.in).nextLine().trim();

            System.out.print("[Password] : ");
            password = new Scanner(System.in).nextLine().trim();
        }


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
    private static boolean setDriverByDbType(String db) throws SQLException {
        if ("o".equals(db) || "oracle".equals(db)) {
            driver = new oracle.jdbc.OracleDriver();
            dbType = DbType.ORACLE;
            return true;
        } else if ("m".equals(db) || "mysql".equals(db)) {
            driver = new com.mysql.cj.jdbc.Driver();
            dbType = DbType.MY_SQL;
            return true;
        } else if ("p".equals(db) || "postgresql".equals(db)) {
            driver = new org.postgresql.Driver();
            dbType = DbType.POSTGRESQL;
            return true;
        } else {
            System.out.println("Db Type Error :" + db);
            return false;
        }
    }

    private static void setUrl(String inUrl) throws SQLException {

        if (dbType.equals(DbType.ORACLE)) {
            if (inUrl.contains("jdbc:")) {
                url = inUrl;
            } else {
                url = "jdbc:oracle:thin:@{url}".replace("{url}", inUrl);
            }

        } else if (dbType.equals(DbType.MY_SQL)) {
            if (inUrl.contains("jdbc:")) {
                url = inUrl;
            } else {
                url = "jdbc:mysql://{url}?useSsl=false".replace("{url}", inUrl);
            }

        } else if (dbType.equals(DbType.POSTGRESQL)) {
            if (inUrl.contains("jdbc:")) {
                url = inUrl;
            } else {
                url = "jdbc:postgresql://{url}?stringtype=unspecified".replace("{url}", inUrl);
            }
        }
    }
}
