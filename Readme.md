在命令行中使用jdbc

# 1. 编译
## 因为oracle jdbc没有在maven库，需要从官网下载后安装oracle jdbc jar到本地maven库
```
mvn install:install-file -Dfile=D:/localjar/ojdbc8-12.2.0.1.0.jar -DgroupId=oracle -DartifactId=oracle -Dversion=12 -Dpackaging=jar
```
如果不想自己编译，也可以直接使用target目录下的 jdbc-cli-1.0.jar

# 2.使用方式1

配置文件
```properties
db1.type=oracle
db1.url=
db1.username=
db1.password=

```
db1 为配置id ,一个配置文件可以有多个id,一次会话中只能读取一个id

type可以设置oracle/o,mysql/m,postgresql/p

url 为jdbc url
username 为用户名
password 为密码

# 3.使用方式2
使用命令行参数