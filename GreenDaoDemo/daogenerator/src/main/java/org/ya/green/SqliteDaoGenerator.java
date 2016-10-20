package org.ya.green;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class SqliteDaoGenerator {

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "org.ya.green.db");

		addLog(schema);
//		schema.setDefaultJavaPackageDao("org.ya.green");
		new DaoGenerator().generateAll(schema,
				"F:/android/ya/workspace/MyGreenDaoDemo/src/org/ya/green");
	}

	private static void addLog(Schema schema) {
		Entity company = schema.addEntity("Company");
		company.addIdProperty().autoincrement();
		company.addDateProperty("regTime");

		Entity logItem = schema.addEntity("Staff");
		logItem.addIdProperty().autoincrement();
		logItem.addDateProperty("regDate");
		logItem.addStringProperty("noId");
		logItem.addStringProperty("name");
		logItem.addStringProperty("dept");

		Property logid = logItem.addLongProperty("comId").getProperty();
		logItem.addToOne(company, logid);

		ToMany loglist = company.addToMany(logItem, logid);
		loglist.setName("staffList");

	}
}