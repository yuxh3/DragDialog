package com.example.admin.demo;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DragGridLayout grild1;
    private DragGridLayout grild2;
    private GridLayout gridLayout;
    private Rect [] rects;//创建所有gridlayout中的子控件的矩形
    private View dragedView;//被拖拽阴影的view
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);


        gridLayout = (GridLayout) findViewById(R.id.gridlayout);

        gridLayout.setOnDragListener(odp);

        grild1 = (DragGridLayout) findViewById(R.id.grild1);
        grild2 = (DragGridLayout) findViewById(R.id.grild2);


        button.setOnClickListener(btn_oc);

        grild1.setAllowDrag(true);

        List<String> item1 = new ArrayList<>();
        item1.add("上海");
        item1.add("昆山");
        item1.add("苏州");
        item1.add("无锡");
        item1.add("常州");
        item1.add("丹阳");
        item1.add("镇江");
        item1.add("马鞍山");
        item1.add("南京");
        item1.add("东莞");
        grild1.setItems(item1);

        grild2.setAllowDrag(false);
        List<String> items2 = new ArrayList<>();
        items2.add("合肥");
        items2.add("武汉");
        items2.add("东莞");
        items2.add("四川");
        items2.add("河北");
        items2.add("河南");
        items2.add("北京");
        grild2.setItems(items2);


        grild1.setItemClickListener(new DragGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(TextView v) {
                grild1.removeView(v);
                grild2.addItem(v.getText().toString());
            }
        });
        grild2.setItemClickListener(new DragGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(TextView v) {
                grild2.removeView(v);
                grild1.addItem(v.getText().toString());
            }
        });
    }
    public View.OnClickListener btc_oc = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DragGridLayout dialog = new DragGridLayout(MainActivity.this);
            List<String> items = new ArrayList<String>();
            items.add("上海");
            items.add("昆山");
            items.add("苏州");
            items.add("无锡");
            items.add("常州");
            items.add("丹阳");
            items.add("镇江");
            items.add("马鞍山");
            items.add("南京");
            List<String> items2 = new ArrayList<String>();
            items2.add("合肥");
            items2.add("武汉");
            items2.add("东莞");
            items2.add("四川");
            items2.add("河北");
            items2.add("河南");
            items2.add("北京");


        }
    };

    private OnDragListener odp = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    if (dragedView != null) {
                        dragedView.setEnabled(false);
                    }

                    //创建所有gridlayout中的子控件的矩形
                    initRects();
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.i(TAG,"MMMMMMMMMMMM");
                    //手指移动的时候，实时判断是否移动到了对应的子控件中如果移动到了，删除被拖拽的view，再添加到对应的子控件的位置
                    int touchIndex = getTouchIndex(event.getX(),event.getY());
                    if (touchIndex >= 0 && dragedView != null && dragedView != gridLayout.getChildAt(touchIndex)) {
                        gridLayout.removeView(dragedView);
                        gridLayout.addView(dragedView, touchIndex);
                        //Toast.makeText(getApplicationContext(), "nihaowa        aaaa",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (dragedView != null) {
                        dragedView.setEnabled(true);
                    }
                    break;

                default:
                    break;
            }
//			Rect rect = new Rect();
//			rect.contains(x, y)
            return true;
        }
    };

    static SparseArray<String> dragEventType = new SparseArray<String>();
    static{
        dragEventType.put(DragEvent.ACTION_DRAG_STARTED, "STARTED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENDED, "ENDED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENTERED, "ENTERED");
        dragEventType.put(DragEvent.ACTION_DRAG_EXITED, "EXITED");
        dragEventType.put(DragEvent.ACTION_DRAG_LOCATION, "LOCATION");
        dragEventType.put(DragEvent.ACTION_DROP, "DROP");
    }


    //遍历所有矩形，判断当前移动的点是否被包含，如果被包含把位置信息返回
    protected int getTouchIndex(float x, float y) {
        for (int i = 0; i < rects.length; i++) {
            if (rects[i].contains((int)x, (int)y)) {
                return i;
            }
        }
        return -1;
    }

    //创建所有gridlayout中的子控件的矩形
    protected void initRects() {
        rects = new Rect[gridLayout.getChildCount()];
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View childView = gridLayout.getChildAt(i);
            rects[i] = new Rect(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
        }
    }



    private View.OnLongClickListener tv_olc = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            // 开始一个拖拽事件
            /**
             * data、myLocalState用于拖拽的时候传递数据
             *
             * new DragShadowBuilder(v):用于创建对应的视图阴影
             */
            dragedView = v;
            v.startDrag(null, new View.DragShadowBuilder(v), null, 0);

            return false;
        }
    };

    private View.OnClickListener btn_oc = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            final DragDialog dialog  = new DragDialog(MainActivity.this);
            List<String> items = new ArrayList<String>();
            items.add("上海");
            items.add("昆山");
            items.add("苏州");
            items.add("无锡");
            items.add("常州");
            items.add("丹阳");
            items.add("镇江");
            items.add("马鞍山");
            items.add("南京");
            List<String> items2 = new ArrayList<String>();
            items2.add("合肥");
            items2.add("武汉");
            items2.add("东莞");
            items2.add("四川");
            items2.add("河北");
            items2.add("河南");
            items2.add("北京");


            dialog.setShowAndHideItems(items, items2);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface d) {
                    //当dialog隐藏的时候，把显示部分的文本数据返回来
                    List<String> showItems = dialog.getShowItems();
                    Toast.makeText(getApplicationContext(), showItems.toString(),Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
    };
    private View.OnClickListener tv_oc = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // 让gridlayout删除掉被点击的tv
            gridLayout.removeView(v);
        }
    };
}
