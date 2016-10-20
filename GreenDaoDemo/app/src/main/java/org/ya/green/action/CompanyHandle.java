package org.ya.green.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.ya.green.WangApplication;
import org.ya.green.db.Company;
import org.ya.green.db.CompanyDao;
import org.ya.green.db.CompanyDao.Properties;
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
public class CompanyHandle {

	private static CompanyHandle mInstance;
	private static Context mContext;

	private CompanyDao companyDao;

	public CompanyHandle() {
	}

	public static CompanyHandle getInstance(Context context) {

		if (mInstance == null) {
			mInstance = new CompanyHandle();
			if (mContext == null) {
				mContext = context.getApplicationContext();
			}
			DaoSession daoSession = WangApplication.getDaoSession(mContext);
			mInstance.companyDao = daoSession.getCompanyDao();
		}
		return mInstance;
	}

	/*
	 * 添加数据【add data】
	 */
	public void addComInfo(Company company) {
		companyDao.insert(company);
	}

	/*
	 * 加载数据【query data】
	 */
	public List<Company> loadAllComInfo() {
		QueryBuilder<Company> qb = companyDao.queryBuilder();
		qb.orderDesc(Properties.RegTime);
		return qb.list();
	}

}
