//package top.fastsql.jdbccli;
//
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.SimpleDriverDataSource;
//
///**
// * @author Chenjiazhi
// * 2018-01-29
// */
//public class Test {
//    public static void main(String[] args) {
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(
//                new oracle.jdbc.OracleDriver(),
//                "jdbc:postgresql://jdbc:oracle:thin:@58.210.23.147:1521:dbsrv2",
//                "zafkuser", "ZDHTyfb2017"
//        );
//
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        System.out.println(
//                jdbcTemplate.queryForList("select count(*) FROM sys_dict")
//        );
//
//    }
//}
