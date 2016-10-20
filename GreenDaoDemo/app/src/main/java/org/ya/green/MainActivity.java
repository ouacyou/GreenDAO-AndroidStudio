package org.ya.green;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.ya.green.action.CompanyHandle;
import org.ya.green.action.StaffHandle;
import org.ya.green.db.Company;
import org.ya.green.db.Staff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private static CompanyHandle mainHandle;
	private static StaffHandle subHandle;
	private static List<String> mList;
	private static ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadData();
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	private void loadData() {
		mainHandle = CompanyHandle.getInstance(this);
		subHandle = StaffHandle.getInstance(this);

		if (mainHandle.loadAllComInfo().size() == 0) {
			Company company = new Company();
			company.setRegTime(new Date());
			// 添加数据
			mainHandle.addComInfo(company);
		}

		if (subHandle.loadAllStaffInfo().size() == 0) {
			Staff staff = new Staff();
			// 我直接加载了top一条数据，可以直接在数据操作中封装一个方法返回最新一条数据
			staff.setCompany(mainHandle.loadAllComInfo().get(0));
			staff.setDept("android dev");
			staff.setNoId("00001");
			staff.setName("Ouacyou");

			subHandle.addStaffInfo(staff);
		}

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private ListView fragment_list;

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			fragment_list = (ListView) rootView
					.findViewById(R.id.fragment_list);

			fragment_list.setAdapter(mAdapter);

			fragment_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(final AdapterView<?> adapter,
						View view, final int position, long arg3) {
					AlertDialog.Builder ab = new AlertDialog.Builder(
							getActivity());
					ab.setTitle("数据库操作");
					ab.setMessage("confirm delete?");

					ab.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							subHandle.delStaffInfo(subHandle.loadAllStaffInfo()
									.get(position));
							mList.clear();
							for (Staff staff : subHandle.loadAllStaffInfo()) {
								mList.add("No_" + staff.getNoId() + "---Name_"
										+ staff.getName());
							}
							mAdapter.notifyDataSetChanged();

						}
					});
					ab.create();
					ab.show();
				}
			});
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
			int sectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
			switch (sectionNum) {
			case 1:
				mList = new ArrayList<String>();
				for (Staff staff : subHandle.loadAllStaffInfo()) {
					mList.add("No_" + staff.getNoId() + "---Name_"
							+ staff.getName());
				}
				mAdapter = new ArrayAdapter<String>(getActivity(),
						R.layout.simple_list_item, mList);
				break;
			case 2:
				Staff staff = new Staff();
				// 我直接加载了top一条数据，可以直接在数据操作中封装一个方法返回最新一条数据
				staff.setCompany(mainHandle.loadAllComInfo().get(0));
				staff.setDept("android dev");
				staff.setNoId("0000" + new Random().nextInt(9));
				staff.setName("Ouacyou" + new Random().nextInt(9));

				subHandle.addStaffInfo(staff);

				break;
			default:
				break;
			}
		}
	}

}
