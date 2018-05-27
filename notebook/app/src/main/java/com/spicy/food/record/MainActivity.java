package com.spicy.food.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.spicy.food.data.Diary;
import com.spicy.food.data.DiaryLog;
import com.spicy.food.record.widget.CollectionLIstItemTouchHelperCallback;
import com.spicy.food.record.widget.ItemTouchHelperExtension;
import com.spicy.food.record.widget.ListItemAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //初始化各种组件
    private TextView mEmptyTxt;//TextView显示文本给用户，并允许他们选择编辑。TextView是一个完整的文本编辑器，但是其基本类配置为不允许编辑。
    private RecyclerView recyclerView;//瀑布流布局，可滚动
    private ListItemAdapter itemAdapter;//适配器
    public ItemTouchHelperExtension mItemTouchHelper;//是一个可以给RecyclerView提供添加拖动排序与滑动删除等等操作的工具类
    public CollectionLIstItemTouchHelperCallback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//将activity_main的xml布局ID，转换为view，并填充到mContentParent上
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//实例化主显示屏
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//实例化fab按钮
        //给fab按钮设置监听器
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WriteActivity.class);//初始化intent
                startActivity(intent);//传值跳转，WriteActivity传到了MainActivity的值
            }
        });
        mEmptyTxt = (TextView) findViewById(R.id.txt_empty);//让mEmptyTxt等于主界面的记点什么吧界面
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);////初始化RecyclerView
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this); //创建LinearLayoutManager 对象
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);//设置RecyclerView 布局
        itemAdapter = new ListItemAdapter();//生成适配器的Item

        //对itemAdapter设置监听器
        itemAdapter.setClickItemListener(new ListItemAdapter.ClickItemListener() {
            @Override
            public void onClickItem(Diary item) {

                if (!mItemTouchHelper.closeOpenedPreItem()) {
                    return;
                }
                if (item == null) {
                    return;
                }

                //构建MainActivity和WriteActivity之间的监听器
                Intent intent = new Intent();//创建intent
                intent.setClass(MainActivity.this, WriteActivity.class);//添加两种activity
                intent.putExtra(WriteActivity.ID, item.getId());//把item的id值传给WriteActivity
                startActivity(intent);//由这个方法返回intent给write
            }

            @Override
            //删除设置
            public void delete(Diary item) {
                if (item == null) {
                    return;
                }
                DiaryLog diaryLog = DiaryLog.getInstance(MainActivity.this);//
                diaryLog.deleteById(item.getId());
                List<Diary> diary = diaryLog.getAllUserClick();
                if (diary == null || diary.size() == 0) {//当无笔记记录时
                    mEmptyTxt.setVisibility(View.VISIBLE);//记点什么把页面可见
                    recyclerView.setVisibility(View.GONE);//隐藏列表页面
                } else {
                    mEmptyTxt.setVisibility(View.GONE);//隐藏记点什么吧页面
                    recyclerView.setVisibility(View.VISIBLE);//显示列表页面
                }
            }

        });
        mCallback = new CollectionLIstItemTouchHelperCallback();
        mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
        mItemTouchHelper.setSwipActionInterface(itemAdapter);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        recyclerView.setAdapter(itemAdapter);
    }

    @Override
    //设置页面
    protected void onResume() {
        super.onResume();
        List<Diary> diary = DiaryLog.getInstance(this).getAllUserClick();
        if (diary == null || diary.size() == 0) {
            mEmptyTxt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            mEmptyTxt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        itemAdapter.setData(diary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 将菜单添加到菜单中；如果存在，则将该项添加到操作栏中。
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //处理动作栏项目点击这里。动作栏将自动处理HOO/UP按钮的点击，如此长在AndroidManifest.xml中指定父活动时。NoStimple简化语句

        return super.onOptionsItemSelected(item);
    }
}
