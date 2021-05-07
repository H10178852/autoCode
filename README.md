### 公告

本人比较懒,能自动生成绝对不手写。<br>
自动生成基本杜绝写错报错。<br>
极极极基础，只提供bean,dao,xml,service<br>
不提供controller!乱生成接口会对数据造成不可估计后果!<br>

### 前言

日常写代码，是一件非常需要耐心的事情，尤其是那些没有技术含量重复使用到的一些代码排列组合，比如Bean,dao,service简单繁杂，这个时候就会使用到一些代码自动生成神器，让我们提高效率。

### 项目介绍
  
1.下载代码
2.需要更改的地方
#### src\main\resources\auto_code\auto_code_config.properties <br>第7、11、13、19 行需要更改
#### src\main\resources\MyBatisGenerator\generator.xml <br>第22、23、24、40、41、50、55行需要更改
#### src\main\resources\application.properties <br>第7、14、15、16行需要更改
改完后方可启动
访问ip:port/autoCode/createAutoZip?tableName=表名<br>
浏览器自动下载zip包，里面包含Bean,dao,xml,service复制到项目内即可(路径可能会报错，稍稍修改下)<br>
还提供了查询所有表名<br>
查询表结构等接口<br>
其中直接生成代码不建议使用(直接生成代码可能会导致写过的代码覆盖<具体我没试过，你可以试试，后果自负>)!!<br>

### 目前正在开发<br>
#### 1.支持Oracle数据库<br>
#### 2.简易的界面预览代码<br>
