package com.guangzhou.station.stationmain;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.dc.baselib.mvvm.AbsLifecycleActivity;
import com.dc.commonlib.common.BaseRecyclerAdapter;
import com.dc.commonlib.common.RefreshMessage;
import com.guangzhou.station.R;
import com.guangzhou.station.playinfo.AbsPlayInfo;
import com.guangzhou.station.playinfo.PlayInfoActivity;
import com.guangzhou.station.playinfo.PlayInfosActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class StationMainActivity extends AbsLifecycleActivity<StationMainViewModel> implements View.OnClickListener {

    private RecyclerView mRecycleView, mRecycleContext;
    private MainListAdapter mMainListAdapter;
    private ThreeListAdapter mThreeListAdapter;
    private ImageView ivLeft;
    private ImageView ivRight;
    private LinearLayoutManager mLinearLayoutManager;
    private static String tag = "StationMainActivity";
    private SearchView mSearchView;
    private FrameLayout fl_searchlist, fl_back;
    private RecyclerView recycleViewList;
    private SearchListAdapter mSearchListAdapter;//搜索列表框适配器
    private boolean isClickKeyword = false;
    private boolean hasSearched = false;

    @Override
    protected Class<StationMainViewModel> getViewModel() {
        return StationMainViewModel.class;
    }

    @Override
    protected int getLayout() {
        return R.layout.station_activity_new_stationmain;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolBarlheadHide(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EventBus.getDefault().register(this);
        //去掉最上面时间、电量等
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hideBottomUIMenu();
        mRecycleView = findViewById(R.id.recycleView);
        mSearchView = findViewById(R.id.searchView);
//        mSearchView.setIconifiedByDefault(false);
        fl_searchlist = findViewById(R.id.fl_searchlist);
        fl_back = findViewById(R.id.fl_back);
        recycleViewList = findViewById(R.id.recycleViewList);
        recycleViewList.setLayoutManager(new LinearLayoutManager(this));
        ivLeft = findViewById(R.id.iv_left);
        ivRight = findViewById(R.id.iv_right);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        mRecycleContext = findViewById(R.id.recycle_context);
        mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        mRecycleView.setAdapter(mMainListAdapter = new MainListAdapter(this, null, -1));
        mRecycleContext.setLayoutManager(new LinearLayoutManager(this));
        mRecycleContext.setAdapter(mThreeListAdapter = new ThreeListAdapter(this, null, -1));
        recycleViewList.setAdapter(mSearchListAdapter = new SearchListAdapter(this, null, -1));
        //Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MSYH.TTC");
        //Typeface typefaceSelected = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/MSYH.TTC");
        mMainListAdapter.addOnItemClickListener(new MainListAdapter.OnItemClickListener() {
            @Override
            public void onItemsClick(List<ProjectListBean.DirectoryListBean.ShowListBean> showListBeans, int position) {
                if (null != showListBeans && null != mThreeListAdapter) {
                    mThreeListAdapter.addListBeanList(showListBeans);
                }
            }
        });

        mSearchListAdapter.addOnItemClickListener(new SearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemsClick(KeywordListBean.ListBean listBeans, int position) {
                // 选中关键词，输入到搜索框开始搜索

                String search = listBeans.name;
                if (!search.isEmpty()) {
                    isClickKeyword = true;
                    mSearchView.setQuery(search, false);
                    // 清空rv，隐藏
                    if (recycleViewList.getChildCount() > 0 ) {
                        recycleViewList.removeAllViews();
                        mSearchListAdapter.notifyDataSetChanged();
                        fl_searchlist.setVisibility(View.GONE);
                        fl_back.setVisibility(View.GONE);
                    }
                    // 搜索详情
                    mViewModel.getSearchDetailsData(listBeans.id, search);
                    hasSearched = true;
                    // 释放焦点，回收键盘
                    mSearchView.clearFocus();
                }
            }
        });


        mViewModel.toFetchListSaverData();
        mMainListAdapter.setDefSelect(0);
        mThreeListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                mViewModel.conversionData(mThreeListAdapter, position);
            }
        });
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int firstItem = manager.findFirstCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        ivRight.setVisibility(View.INVISIBLE);
                        ivLeft.setVisibility(View.VISIBLE);
                    } else if (firstItem == 0) {
                        ivRight.setVisibility(View.VISIBLE);
                        ivLeft.setVisibility(View.INVISIBLE);
                    } else if (firstItem > 0 && lastVisibleItem < (totalItemCount - 1)) {
                        ivRight.setVisibility(View.VISIBLE);
                        ivLeft.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0) {
                    //大于0表示正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }
        });
        initSearch();

    }

    private void initSearch() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("StationMainActivity", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 文本框发生变化，调用关键字查询
                if (!isClickKeyword) {
                    if(newText.isEmpty()) {
                        if (recycleViewList.getChildCount() > 0 ) {
                            recycleViewList.removeAllViews();
                            mSearchListAdapter.notifyDataSetChanged();
                            fl_searchlist.setVisibility(View.GONE);

                        }
                    } else {
                            mViewModel.getKeywordListData(newText);
                    }
                }
                isClickKeyword = false;


                return true;
            }
        });
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StationMainActivity", "search");
                fl_back.setVisibility(View.VISIBLE);
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("StationMainActivity", "close");
                // 清除搜索框，恢复初始数据
                mViewModel.toFetchListSaverData();
                mMainListAdapter.setDefSelect(0);
                fl_back.setVisibility(View.GONE);
                return false;
            }
        });


        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//        TextView textView = mSearchView.findViewById(id);
//        textView.setTextColor(getResources().getColor(R.color.white));
//        mSearchView.isFocusable();
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fl_back.setVisibility(View.VISIBLE);
                } else {
                    fl_back.setVisibility(View.GONE);
                }
            }
        });


//        mSearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RefreshMessage refreshMessage) {
        if (refreshMessage != null && refreshMessage.refresh) {
            mViewModel.toFetchListSaverData();
        }
    }

    @Override
    protected void initData() {
        registerSubscriber(StationMainViewModel.EVENT_KEY_GETLIST, List.class).observe(this, new Observer<List>() {

            @Override
            public void onChanged(List list) {
                if (list != null) {
                    fillData(list);
                }


            }
        });

        registerSubscriber(StationMainViewModel.EVENT_CONVERSATION, List.class).observe(this, new Observer<List>() {

            @Override
            public void onChanged(List list) {
                if (list != null && !list.isEmpty()) {
                    Object o = list.get(0);
                    if (o instanceof AbsPlayInfo) {
                        AbsPlayInfo absPlayInfo = (AbsPlayInfo) o;
                        boolean auto = absPlayInfo.play_type == 1;
//                        PlayInfoActivity.startActivity(StationMainActivity.this, list, auto);
                        PlayInfosActivity.startActivity(StationMainActivity.this, list, auto);
                    }
                }
            }
        });

        registerSubscriber(StationMainViewModel.EVEVT_KEYWORD_GETLIST, List.class).observe(this, new Observer<List>() {

            @Override
            public void onChanged(List list) {
                if (list != null) {
//                    fillData(list);
                    fillKeywordData(list);
                }


            }
        });

        registerSubscriber(StationMainViewModel.EVENT_SEARCH_DETAILS, List.class).observe(this, new Observer<List>() {

            @Override
            public void onChanged(List list) {
                if (list != null) {
                    fillData(list);
                    mMainListAdapter.setDefSelect(0);
                }

            }
        });

    }


    private void fillData(List<ProjectListBean.DirectoryListBean> listBeans) {
        LogUtils.dTag("StationMainActivity", listBeans.toString());
        if (null != mMainListAdapter && null != listBeans && !listBeans.isEmpty()) {
            List<AbsStationData> ll = (List<AbsStationData>) (Object) listBeans;
            mMainListAdapter.setList(ll);
            if (mMainListAdapter.getList().size() < 4) {
                ivRight.setVisibility(View.INVISIBLE);
            }
            ProjectListBean.DirectoryListBean directoryListBean = mMainListAdapter.notifySelect(0);
            if (null != directoryListBean && null != mThreeListAdapter && null != directoryListBean.showList) {
                mThreeListAdapter.addListBeanList(directoryListBean.showList);
            }
        }
//        checkAdapterItems();
    }

    private void fillKeywordData(List<KeywordListBean.ListBean> list){
        LogUtils.dTag("StationMainActivity", list.toString());
        if (null != mSearchListAdapter && null != list && !list.isEmpty()) {
            List<KeywordListBean.ListBean> ll = (List<KeywordListBean.ListBean>) (Object) list;
            mSearchListAdapter.setList(ll);
            fl_searchlist.setVisibility(View.VISIBLE);
//            fl_back.setVisibility(View.VISIBLE);
        }
    }

    private void checkAdapterItems() {
        if (null != mMainListAdapter && !mMainListAdapter.getList().isEmpty()) {
            int count = getCurrentVisibilyCount((LinearLayoutManager) mRecycleView.getLayoutManager(), mRecycleView);
            LogUtils.d(tag, "当前可见条目:" + count);
            if (count > 0 && count < mMainListAdapter.getList().size()) {

                ivLeft.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.VISIBLE);
            } else {
                ivLeft.setVisibility(View.INVISIBLE);
                ivRight.setVisibility(View.INVISIBLE);
            }
        } else {
            ivLeft.setVisibility(View.INVISIBLE);
            ivRight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public int getCurrentVisibilyCount(LinearLayoutManager manager, RecyclerView mRecyclerView) {
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        return lastVisibleItemPosition - firstVisibleItemPosition;

    }

    public void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int left = mRecyclerView.getChildAt(n - firstItem).getLeft();
//            int left = mRecyclerView.getChildAt(n).getLeft();
            mRecyclerView.scrollBy(left, 0);
        } else {
            mRecyclerView.scrollToPosition(n);
        }


    }

    int totalPage = 0;

    private int getTatalPage() {
        int count = mMainListAdapter.getList().size();
        if (count % 4 == 0) {
            //整除
            totalPage = count / 4;
        } else {
            totalPage = (count / 4) + 1;
        }
        return totalPage;
    }

    int currentPage = 0;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_left) {
            --currentPage;
            checkPage();
            int pre = getPrePosition(currentPage);
            LogUtils.d(tag, "pre" + pre);
            MoveToPosition((LinearLayoutManager) mRecycleView.getLayoutManager(), mRecycleView, pre);
        } else if (v.getId() == R.id.iv_right) {
            ++currentPage;
            checkPage();
            int next = getNextPosition(currentPage);
            LogUtils.d(tag, "next" + next);
            MoveToPosition((LinearLayoutManager) mRecycleView.getLayoutManager(), mRecycleView, next);
        }
    }

    private int getPrePosition(int currentPage) {

        int nextPosition = 4 * currentPage;

        return nextPosition;
    }

    private int getNextPosition(int currentPage) {
        int nextPosition = 0;
        if ((4 * currentPage) > mMainListAdapter.getList().size()) {
            int y = mMainListAdapter.getList().size() % 4;
            nextPosition = mMainListAdapter.getList().size() - y;
        } else {
            nextPosition = 4 * currentPage;
        }
        return nextPosition;
    }

    private void checkPage() {
        int totalPage = getTatalPage();
        if (currentPage >= totalPage - 1) {
            currentPage = totalPage - 1;
            ivRight.setVisibility(View.INVISIBLE);
            ivLeft.setVisibility(View.VISIBLE);
        } else if (currentPage <= 0) {
            currentPage = 0;
            ivLeft.setVisibility(View.INVISIBLE);
            ivRight.setVisibility(View.VISIBLE);
        } else {
            ivLeft.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏虚拟按键
     */
    private void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN;
            _window.setAttributes(params);

        }
    }

}
