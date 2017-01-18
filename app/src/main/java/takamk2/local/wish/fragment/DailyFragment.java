package takamk2.local.wish.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseFragment;
import takamk2.local.wish.db.WishDBHelper;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.util.DateUtil;
import takamk2.local.wish.util.TextUtil;
import timber.log.Timber;

/**
 * Created by takamk2 on 16/12/22.
 * <p>
 * The Edit Fragment of Base Class.
 */
public class DailyFragment extends BaseFragment {

    private Button mBtNext;
    private Button mBtPrevious;
    private Button mBtReset;
    private ViewFlipper mVfContainer;

    private LayoutInflater mLayoutInflater;
    private PageManager mPageManager;

    // ---------------------------------------------------------------------------------------------
    public static DailyFragment newInstance() {
        DailyFragment fragment = new DailyFragment();
        // Todo: bundle
        // Todo: animation
        return fragment;
    }

    public static void replaceFragment(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = DailyFragment.newInstance();
        transaction.replace(R.id.fragment_container2, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // ---------------------------------------------------------------------------------------------
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_next:
                    onClickNext();
                    break;
                case R.id.bt_previous:
                    onClickPrevious();
                    break;
                case R.id.bt_reset:
                    onClickReset();
                    break;
                default:
                    break;
            }
        }
    };

    // ---------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

    @Override
    protected void bindViews(View view) {
        mBtNext = (Button) view.findViewById(R.id.bt_next);
        mBtPrevious = (Button) view.findViewById(R.id.bt_previous);
        mBtReset = (Button) view.findViewById(R.id.bt_reset);
        mVfContainer = (ViewFlipper) view.findViewById(R.id.vf_container);

        mPageManager = new PageManager(mVfContainer);
    }

    @Override
    protected void bindActions() {
        mBtNext.setOnClickListener(mOnClickListener);
        mBtPrevious.setOnClickListener(mOnClickListener);
        mBtReset.setOnClickListener(mOnClickListener);
    }

    private void onClickNext() {
        mPageManager.goToNext();
    }

    private void onClickPrevious() {
        mPageManager.goToPrevious();
    }

    private void onClickReset() {
//        mVfContainer.setInAnimation(mActivity, R.anim.left_in);
//        mVfContainer.setOutAnimation(mActivity, R.anim.right_out);
//        mVfContainer.showPrevious();
        mPageManager.goToToday();
    }

    // ---------------------------------------------------------------------------------------------
    private class Page {

        public static final int POSITION_CURRENT = 0;
        public static final int POSITION_NEXT = 1;
        public static final int POSITION_PREVIOUS = 2;
        public static final int POSITION_NONE = 3;

        private View mView;
        private String mName;
        private Calendar mDate;
        private int mPosition = POSITION_NONE;
        private int mPageId;

        public Page(View view, int pageId) {
            mView = view;
            mPageId = pageId;
        }

        public View getView() {
            return mView;
        }

        public void setDate(Calendar date) {
            mDate = date;
            updateView();
        }

        private void updateView() {
            TextView tvDate = (TextView) mView.findViewById(R.id.tv_date);
            ListView lvTasks = (ListView) mView.findViewById(R.id.lv_tasks);

            tvDate.setText(String.format("%04d/%02d/%02d", mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH) + 1, mDate.get(Calendar.DATE)));

            new UpdateViewTask(mActivity, mView, mDate).execute(mDate);
        }

        public Calendar getDate() {
            return mDate;
        }

        public void setPosition(int position) {
            mPosition = position;
        }

        public int getPosition() {
            return mPosition;
        }

        public boolean isCurrent() {
            return mPosition == POSITION_CURRENT;
        }

        public int getPageId() {
            return mPageId;
        }
    }

    // ---------------------------------------------------------------------------------------------
    private class PageManager {

        private final ViewFlipper mViewFlipper;
        private Calendar mNow;
        private List<Page> mPages = new ArrayList<>();

        // -----------------------------------------------------------------------------------------
        public PageManager(ViewFlipper viewFlipper) {
            mViewFlipper = viewFlipper;
            mNow = DateUtil.getNowDate();

            mPages.add(new Page(mLayoutInflater.inflate(R.layout.layout_daily_page, null, false), 0));
            mPages.add(new Page(mLayoutInflater.inflate(R.layout.layout_daily_page, null, false), 1));
            mPages.add(new Page(mLayoutInflater.inflate(R.layout.layout_daily_page, null, false), 2));

            reset();

            for (Page page : mPages) {
                viewFlipper.addView(page.getView());
            }
        }

        // -----------------------------------------------------------------------------------------
        public void reset() {
            mPages.get(0).setPosition(Page.POSITION_CURRENT);
            mPages.get(1).setPosition(Page.POSITION_NEXT);
            mPages.get(2).setPosition(Page.POSITION_PREVIOUS);
            setCurrentDate(mNow);
            updatePages();
        }

        public void goToNext() {
            for (Page page : mPages) {
                switch (page.getPosition()) {
                    case Page.POSITION_CURRENT:
                        page.setPosition(Page.POSITION_PREVIOUS);
                        break;
                    case Page.POSITION_NEXT:
                        page.setPosition(Page.POSITION_CURRENT);
                        break;
                    case Page.POSITION_PREVIOUS:
                        page.setPosition(Page.POSITION_NEXT);
                        break;
                }
            }
            updatePages();
            mViewFlipper.setInAnimation(mActivity, R.anim.right_in);
            mViewFlipper.setOutAnimation(mActivity, R.anim.left_out);
            mViewFlipper.showNext();
        }

        public void goToPrevious() {
            for (Page page : mPages) {
                switch (page.getPosition()) {
                    case Page.POSITION_CURRENT:
                        page.setPosition(Page.POSITION_NEXT);
                        break;
                    case Page.POSITION_NEXT:
                        page.setPosition(Page.POSITION_PREVIOUS);
                        break;
                    case Page.POSITION_PREVIOUS:
                        page.setPosition(Page.POSITION_CURRENT);
                        break;
                }
            }
            updatePages();
            mViewFlipper.setInAnimation(mActivity, R.anim.left_in);
            mViewFlipper.setOutAnimation(mActivity, R.anim.right_out);
            mViewFlipper.showPrevious();
        }

        public void goToToday() {
            reset();
            mViewFlipper.setDisplayedChild(0);
        }

        // -----------------------------------------------------------------------------------------
        private void updatePages() {
            Calendar currentDate = getCurrentPage().getDate();
            Calendar cal;
            for (Page page : mPages) {
                switch (page.getPosition()) {
                    case Page.POSITION_NEXT:
                        cal = (Calendar) currentDate.clone();
                        cal.add(Calendar.DATE, 1);
                        page.setDate(cal);
                        break;
                    case Page.POSITION_PREVIOUS:
                        cal = (Calendar) currentDate.clone();
                        cal.add(Calendar.DATE, -1);
                        page.setDate(cal);
                        break;
                    default:
                        break;
                }
            }
        }

        private void setCurrentDate(Calendar currentDate) {
            Page currentPage = getCurrentPage();
            if (currentPage != null) {
                currentPage.setDate(currentDate);
            }
            updatePages();
        }

        private Page getCurrentPage() {
            for (Page page : mPages) {
                if (page.isCurrent()) {
                    return page;
                }
            }
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------
    public class UpdateViewTask extends AsyncTask<Calendar, Void, Void> {

        private Calendar mDate;
        private Context mContext;
        private View mView;
        private final ListView mLvTasks;
        private ArrayList<Long> mTaskIdList = new ArrayList<>();
        private final ArrayAdapter mAdapter;
        private ArrayList<Task> mTasks = new ArrayList<>();

        public UpdateViewTask(Context context, View view, Calendar date) {
            mContext = context;
            mView = view;
            mDate = date;

            mLvTasks = (ListView) mView.findViewById(R.id.lv_tasks);

            mLvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Task task = (Task) mAdapter.getItem(position);
                    Timber.i("onItemClick - DEBUG: position=%d id=%d task=%s", position, id, task.isCompleted());
                    if (task.isCompleted()) {
                        task.setIsCompleted(false);
                        String selection = WishDBStore.Daily.COLUMN_TASK_ID + " = ? and " +
                                WishDBStore.Daily.COLUMN_COMPLETED + " = ?";
                        String[] selectionArgs = {
                                String.valueOf(task.getId()), String.valueOf(mDate.getTimeInMillis())
                        };
                        int cnt = mContext.getContentResolver().delete(WishDBStore.Daily.CONTENT_URI, selection, selectionArgs);
                        Timber.i("onItemClick - DEBUG: isCompleted cnt=%d", cnt);
                        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                        tvTitle.setBackgroundColor(Color.DKGRAY);
                        mTaskIdList.remove(task.getId());
                    } else {
                        task.setIsCompleted(true);
                        Timber.i("onItemClick - DEBUG: isNotCompleted");
                        ContentValues values = new ContentValues();
                        values.put(WishDBStore.Daily.COLUMN_TASK_ID, task.getId());
                        values.put(WishDBStore.Daily.COLUMN_COMPLETED, mDate.getTimeInMillis());
                        mContext.getContentResolver().insert(WishDBStore.Daily.CONTENT_URI, values);
                        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                        tvTitle.setBackgroundColor(Color.GREEN);
                        mTaskIdList.add(task.getId());
                    }
                    Cursor cursor = mContext.getContentResolver().query(WishDBStore.Daily.CONTENT_URI, new String[]{"count(*)"}, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        Timber.i("onItemClick - DEBUG: queryCount=%d", cursor.getLong(0));
                        cursor.close();
                    }
                }
            });
            mAdapter = new TasksAdapter(mContext);
            mLvTasks.setAdapter(mAdapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Calendar... calendars) {
            try {
                doTask(calendars[0]);
            } catch (Exception e) {
                cancel(true);
                Timber.w("error: %s", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Timber.i("onPostExecute - DEBUG: mTasks=" + mTasks.size());
            mAdapter.clear();
            mAdapter.addAll(mTasks);
            mAdapter.notifyDataSetChanged();
        }

        private void doTask(Calendar date) {
            Timber.i("doTask - DEBUG: START");
            loadDaily(date);
            loadTasks(date);

            // Tasks
            // sortOrder: created

        }

        private void loadDaily(Calendar date) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(mDate.getTimeInMillis());
            Timber.i("loadDaily - DEBUG: mDate=%d mDateStr=%s cal=%04d/%02d/%02d", mDate.getTimeInMillis(), String.valueOf(mDate.getTimeInMillis()), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE));
            String[] projection = {WishDBStore.Daily.COLUMN_TASK_ID};
            String selection = WishDBStore.Daily.COLUMN_COMPLETED + " = ?";
            String[] selectionArgs = {String.valueOf(mDate.getTimeInMillis())};
            String sortOrder = WishDBStore.Daily.COLUMN_TASK_ID + " asc";
            Cursor cursor = mContext.getContentResolver().query(WishDBStore.Daily.CONTENT_URI,
                    projection, selection, selectionArgs, sortOrder);
            mTaskIdList.clear();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    mTaskIdList.add(cursor.getLong(cursor.getColumnIndex(WishDBStore.Daily.COLUMN_TASK_ID)));
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            for (long id : mTaskIdList) {
                Timber.i("loadDaily - DEBUG: id=%d", id);
            }
        }

        private void loadTasks(Calendar date) {
            String[] projection = {
                    WishDBStore.Tasks.COLUMN_ID, WishDBStore.Tasks.COLUMN_TITLE,
                    WishDBStore.Tasks.COLUMN_PRICE,
            };
            String sortOrder = WishDBStore.Tasks.COLUMN_ID + " asc";
            Cursor cursor = mContext.getContentResolver().query(WishDBStore.Tasks.CONTENT_URI,
                    projection, null, null, sortOrder);
            int i = 0;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(date.getTimeInMillis());
            if (cursor != null) {
                Timber.i("loadTasks - DEBUG: mDate=%d date=%d cal=%04d/%02d/%02d", mDate.getTimeInMillis(), date.getTimeInMillis(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE));
                Timber.i("loadTasks - DEBUG: mTaskIdList=%s", mTaskIdList.toString());
                while (cursor.moveToNext()) {
                    Long id = cursor.getLong(cursor.getColumnIndex(WishDBStore.Tasks.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndex(WishDBStore.Tasks.COLUMN_TITLE));
                    Long price = cursor.getLong(cursor.getColumnIndex(WishDBStore.Tasks.COLUMN_PRICE));
                    boolean isCompleted = mTaskIdList.contains(id);
                    Timber.i("loadTasks - DEBUG: index=%d id=%d title=%s price=%d completed=%s", i, id, title, price, isCompleted);
                    Task task = new Task(id, title, price, isCompleted);
                    mTasks.add(task);
                    i++;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    // ---------------------------------------------------------------------------------------------
    private class Task {
        private Long mId;
        private String mTitle;
        private Long mPrice;
        private boolean mIsCompleted = false;

        public Task(Long id, String title, Long price, boolean isCompleted) {
            mId = id;
            mTitle = title;
            mPrice = price;
            mIsCompleted = isCompleted;
        }

        public Long getId() {
            return mId;
        }

        public String getTitle() {
            return mTitle;
        }

        public Long getPrice() {
            return mPrice;
        }

        public boolean isCompleted() {
            return mIsCompleted;
        }

        public void setIsCompleted(boolean isCompleted) {
            mIsCompleted = isCompleted;
        }
    }

    // ---------------------------------------------------------------------------------------------
    private class TasksAdapter extends ArrayAdapter<Task> {

        private final LayoutInflater mInflater;

        public TasksAdapter(Context context) {
            super(context, 0);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_daily_task_list, parent, false);
                ViewHolder holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                convertView.setTag(holder);
            }

            Task task = getItem(position);

            Timber.i("getView - DEBUG: position=%d title=%s price=%d isCompleted=%s", position, task.getTitle(), task.getPrice(), task.isCompleted());
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.tvTitle.setText(task.getTitle());
            holder.tvPrice.setText(TextUtil.convertNumToCurrency(task.getPrice()));
            if (task.isCompleted()) {
                holder.tvTitle.setBackgroundColor(Color.GREEN);
            } else {
                holder.tvTitle.setBackgroundColor(Color.DKGRAY);
            }

            return convertView;
        }

        class ViewHolder {
            TextView tvTitle;
            TextView tvPrice;
        }
    }
}
