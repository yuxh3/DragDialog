package com.example.admin.demo;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 2016/6/8.
 */
public class DragGridLayout extends GridLayout {
    private int columnCount = 4;
    private boolean alldrag;
    private  Rect[] recs;
    private TextView dragedView;
    private OnItemClickListener mOnItemClickListener;
    private Rect[] rect;


    public DragGridLayout(Context context) {
        this(context, null);
    }

    public DragGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setColumnCount(columnCount);
        this.setLayoutTransition(new LayoutTransition());

    }
    public void setItems(List<String> items){
        this.removeAllViews();
        for (String item:items){
            addItem(item);
        }
    }

    protected void addItem(String item) {
        TextView tv = newTextView();
        tv.setText(item);
        if (alldrag){
            tv.setOnLongClickListener(tv_olc);
        }else {
            tv.setOnLongClickListener(null);
        }
        this.addView(tv);
    }
    private OnLongClickListener tv_olc = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            dragedView = (TextView)v;
            v.startDrag(null,new DragShadowBuilder(v),null,0);

            return false;
        }
    };

    private TextView newTextView() {
        TextView tv = new TextView(getContext());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        int margin = 5;
        params.width = getResources().getDisplayMetrics().widthPixels
                        /getColumnCount() - 2*margin;
        params.height = LayoutParams.WRAP_CONTENT;
        params.setMargins(margin,margin,margin,margin);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.tv_bg);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick((TextView) v);
                }
            }
        });
        return tv;
    }
    public interface OnItemClickListener{
        public void onItemClick(TextView v);
    }
    public void setItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public void setAllowDrag(boolean flag){
        this.alldrag = flag;
        if (alldrag){
            this.setOnDragListener(odl);
        }else {
            this.setOnDragListener(null);
        }
    }
    public OnDragListener odl = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    if (dragedView != null){
                        dragedView.setEnabled(false);
                    }
                    initRects();
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    int touchIndex = getTouchIndex(event.getX(),event.getY());
                    if (touchIndex >=0 && dragedView != null && dragedView != DragGridLayout.this.getChildAt(touchIndex)){
                        DragGridLayout.this.removeAllViews();
                        DragGridLayout.this.addView(dragedView,touchIndex);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (dragedView != null){
                        dragedView.setEnabled(true);
                    }
                    break;
            }
            return true;
        }
    };

    protected int getTouchIndex(float x, float y) {
        for (int i = 0;i<rect.length;i++){
            if (rect[i].contains((int)x,(int)y));
            return i;
        }
        return -1;
    }

    protected void initRects() {
        rect = new Rect[this.getChildCount()];
        for (int i = 0;i<this.getChildCount();i++){
            View childView = this.getChildAt(i);
            rect[i] = new Rect(childView.getLeft(),childView.getTop(), childView.getRight(), childView.getBottom());
        }
    }
}
