在命令行中使用jdbc，可以轻松在服务器上测试jdbc

# 1. 编译
## 安装oracle jdbc
因为oracle jdbc没有在maven库，需要从官网下载后安装oracle jdbc jar到本地maven库
```
mvn install:install-file -Dfile=D:/localjar/ojdbc8-12.2.0.1.0.jar -DgroupId=oracle -DartifactId=oracle -Dversion=12 -Dpackaging=jar
```
如果不想自己编译，也可以直接使用target目录下的 jdbc-cli-1.0.jar

# 2.使用方式1
启动
```
Welcome jdbc-cli......
Select Mode ~ Mode 1-Use properties file; Mode 2-Input Parameters. Input 1/2 (2) :1
Input Config File Path (./config.properties) :D:/config.properties
Input Config  Id (db1) :db1
===========SUCCESS=========
[INFO] jdbc:postgresql://localhost:5432/picasso-work?stringtype=unspecified  

[Please input sql] : 
```
选择1 ，使用配置文件进行加载。
然后输入配置文件位置
选择配置中的配置id（一个配置文件可以包括多个id）

配置文件如下
```properties
db1.type=p
db1.url=localhost:5432/picasso-work
db1.username=developer
db1.password=password

```
db1 为配置id ,一个配置文件可以有多个id,一次会话中只能读取一个id

type可以设置oracle/o,mysql/m,postgresql/p

url 为jdbc url
username 为用户名
password 为密码

# 3.使用方式2
使用命令行参数,根据提示输入即可

```
Welcome jdbc-cli......
Select Mode ~ Mode 1-Use properties file; Mode 2-Input Parameters. Input 1/2 (2) :2
Input DataBase Type o-Oracle/m-Mysql/p-Postgresql (p): p
[Jdbc Url (Also Can short style~ localhost:1522/dbname OR 192.168.0.202:1521:xe )]: localhost:5432/picasso-work
[Username] : developer
[Password] : password
===========SUCCESS=========
[INFO] jdbc:postgresql://localhost:5432/picasso-work?stringtype=unspecified  

[Please input sql] : 
```