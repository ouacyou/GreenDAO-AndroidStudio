package org.ya.green;

import org.ya.green.db.DaoMaster;
import org.ya.green.db.DaoMaster.DevOpenHelper;
import org.ya.green.db.DaoSession;

import android.app.Application;
import android.content.Context;

public class WangApplication extends Application {

	private WangApplication mInstance;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();
		if (mInstance != null)
			mInstance = this;
	}

	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
					"YaC.db", null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
}
