package org.ya.green.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ya.green.WangApplication;
import org.ya.green.db.Company;
import org.ya.green.db.Staff;
import org.ya.green.db.StaffDao;
import org.ya.green.db.StaffDao.Properties;
import org.ya.green.db.DaoSession;

import android.content.Context;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 
 * 
 * @author Ya.C Wang
 * @version 1.0
 * 
 */
public class StaffHandle {

	private static StaffHandle mInstance;
	private static Context mContext;

	private StaffDao staffDao;

	public StaffHandle() {
	}

	public static StaffHandle getInstance(Context context) {

		if (mInstance == null) {
			mInstance = new StaffHandle();
			if (mContext == null) {
				mContext = context.getApplicationContext();
			}
			DaoSession daoSession = WangApplication.getDaoSession(mContext);
			mInstance.staffDao = daoSession.getStaffDao();
		}
		return mInstance;
	}

	/*
	 * 【add data】
	 */
	public void addStaffInfo(Staff staff) {
		staffDao.insert(staff);
	}

	/**
	 * add list
	 * 
	 * @param staffList
	 */
	public void addStaffList(List<Staff> staffList) {
		staffDao.insertInTx(staffList);
	}

	/*
	 * 【query data】
	 */
	public List<Staff> loadAllStaffInfo() {
		QueryBuilder<Staff> qb = staffDao.queryBuilder();
		qb.orderDesc(Properties.Id);
		return qb.list();
	}

	public void delStaffInfo(Staff staff) {
		staffDao.delete(staff);
	}

	/**
	 * 
	 * 删除数据By编号
	 * 
	 * @param no
	 */
	public void delStaffInfoByNo(String no) {
		QueryBuilder<Staff> qb = staffDao.queryBuilder();
		DeleteQuery<Staff> dq = qb.where(Properties.NoId.eq(no)).buildDelete();
		dq.executeDeleteWithoutDetachingEntities();
	}

}
