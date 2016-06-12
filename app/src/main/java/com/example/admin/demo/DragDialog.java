package com.example.admin.demo;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/8.
 */
public class DragDialog extends Dialog {


    private DragGridLayout showgrid;
    private DragGridLayout hidegrid;

    public DragDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DragDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public DragDialog(Context context) {
        this(context, R.style.dialog_style);

        this.setContentView(R.layout.dialog_drag);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;
        this.setCanceledOnTouchOutside(true);
        showgrid = (DragGridLayout) findViewById(R.id.showgrid);
        showgrid.setAllowDrag(true);
        hidegrid = (DragGridLayout) findViewById(R.id.hidegrid);
        hidegrid.setAllowDrag(false);

        showgrid.setItemClickListener(new DragGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(TextView v) {
                showgrid.removeView(v);
                hidegrid.addItem(v.getText().toString());
            }
        });
        hidegrid.setItemClickListener(new DragGridLayout.OnItemClickListener() {
            @Override
            public void onItemClick(TextView v) {
                hidegrid.removeView(v);
                showgrid.addItem(v.getText().toString());
            }
        });

    }
    public void setShowAndHideItems(List<String> shows,List<String> hides){

        if (shows != null && shows.size()>0){
            showgrid.setItems(shows);
        }
        if (hides != null && hides.size()>0){
            hidegrid.setItems(hides);
        }
    }

    public List<String> getShowItems(){
        List<String> datas = new ArrayList<String>();
        for (int i = 0;i<showgrid.getChildCount();i++){
            View childView = showgrid.getChildAt(i);
            datas.add(((TextView)childView).getText().toString());
        }
            return datas;
    }

}
