# GreenDAO-AndroidStudio
在android studio下重新对greenDAO进行编写，了解as下如何配置及操作greenDAO以及编码过程。



####配置
GreenDAO使用需要建一个java project去生成相关的bean原理类似hibernate；<br/>
在Eclipse中可以直接新建一个java项目，而as中需要建一个java module;
 * 在你的android项目中新建一个module(Click File > New Module. 选择Java Library and click Next)
 * 填写包名及相关信息(input the package name, etc and click Finish)
 * 在生成的module中build文件里把greenDAO的lib配置进去
 * 点击Edit Configurations可以看到如下内容，说明配置成功(Click on the run menu,Click Edit Configurations)

####使用
* 设计相关的bean文件，根据你自己设计的数据库表转化成你需要的包含相应字段的实体类；
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
