package com.spicy.food.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.spicy.food.data.Diary;
import com.spicy.food.data.DiaryLog;

public class WriteActivity extends AppCompatActivity {
    private EditText mTitle;
    private EditText mContent;
    public static final String ID = "title";
    private long id = -1;
    private Diary diary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_main);//把write_main加入setContentView
        mTitle = (EditText) findViewById(R.id.edit_title);//实例化标题栏
        mContent = (EditText) findViewById(R.id.edit_content);//实例化内容栏
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getLongExtra(ID, -1);
            diary = DiaryLog.getInstance(WriteActivity.this).getDiaryById(id);
            if (diary != null) {
                if (diary.getTitle() != null) {
                    mTitle.setText(diary.getTitle());//添加标题
                }
                if (diary.getContent() != null) {
                    mContent.setText(diary.getContent());//添加内容
                }
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 将菜单添加；如果存在，则将该项添加到操作栏中
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理动作栏项目点击这里。动作栏将自动处理HOO/UP按钮的点击，如此长在AndroidManifest.xml中指定父活动时。
        int id = item.getItemId();

        //NoStimple简化语句
        if (id == R.id.action_settings) {
            DiaryLog diaryLog = DiaryLog.getInstance(WriteActivity.this);
            String title = mTitle.getEditableText().toString();
            String content = mContent.getEditableText().toString();
            if (this.id == -1) {
                diaryLog.addUserClick(title, content);
            } else {
                if (!TextUtils.isEmpty(title)) {
                    diary.setTitle(title);//添加title
                }
                if (!TextUtils.isEmpty(content)) {
                    diary.setContent(content);//添加content
                }
                diaryLog.updateDiary(diary);
            }
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
