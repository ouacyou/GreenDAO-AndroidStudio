# GreenDAO-AndroidStudio
在android studio下重新对greenDAO进行编写，了解as下如何配置及操作greenDAO以及编码过程。

![](/res/dis.gif)


####配置
GreenDAO使用需要建一个java project去生成相关的bean原理类似hibernate；<br/>
原理就是ORM(Object-Relational Mapping),意思就是在关系型数据库和对象之间做了一个映射，对象与关系之间可以相互映射，在操作数据库表时，不必再写入复杂的SQL语句，直接对对象进行操作，GreenDAO就是将对象进行持久化的过程，将表字段映射成内存中的对象属性。

![](/res/greenDAO.png)

在Eclipse中可以直接新建一个java项目，而as中需要建一个java module;
 * 在你的android项目中新建一个module(Click File > New Module. 选择Java Library and click Next)
 * 填写包名及相关信息(input the package name, etc and click Finish)
 * 在生成的module中build文件里把greenDAO的lib配置进去
 * 点击Edit Configurations可以看到如下内容，说明配置成功(Click on the run menu,Click Edit Configurations)
![](/res/config.png)

####使用
* 设计相关的bean文件，根据你自己设计的数据库表转化成你需要的包含相应字段的实体类；
![](/res/uml.png)

```
Entity company = schema.addEntity("Company");
company.addIdProperty().autoincrement();//自增
company.addDateProperty("regTime");
```
字段类型与正常db一样，也有Date，long等类型，将你需要的类型通过addXXXProperty()进行添加;
当然你的表可能会很复杂，涉及到多个主键外键相关联的表，GreenDAO提供相关方法可以设置；
一个公司对应多个员工，公司与员工表之间形成一个1...n多的关系：员工bean持有一个公司的主key(comId)
```
Property logid = logItem.addLongProperty("comId").getProperty();
logItem.addToOne(company, logid);
ToMany loglist = company.addToMany(logItem, logid);
loglist.setName("staffList");
```
 * 生成
 
生成bean的关键
```
Schema schema = new Schema(1, "package.name");//param[0]代表你的数据库db的版本号

new DaoGenerator().generateAll(schema,"your file path");//把生成的文件设置到你项目中的包中
```
【注意你的表结构发生变化时，需要将Schema类中的参数版本号也相应的升级++】

 * 数据库的CRUD
 例如添加人员
 代码处理起来还是比较简洁的
 ```
 staffDao.insert(Staff);
 
 staffDao.insertInTx(staffList);//你要批量插入的话
 ```
 查询(批量查询)
 ```
 QueryBuilder<Staff> qb = staffDao.queryBuilder();
 qb.orderDesc(Properties.Id);//倒序
  ```
  删除
  ```
  staffDao.delete(staff);
  
  //你要开除某个人按照ID
  QueryBuilder<Staff> qb = staffDao.queryBuilder();
  DeleteQuery<Staff> dq = qb.where(Properties.id.eq(no)).buildDelete();
  ```
