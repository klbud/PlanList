package com.future.bigblack.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.future.bigblack.R;
import com.future.bigblack.bean.PlanInfo;
import com.future.bigblack.database.PlanInfoDBHelper;
import com.future.bigblack.untils.DateUntil;

import java.util.ArrayList;
import java.util.List;

import static com.future.bigblack.R.id.cBox_is_doing;

public class MyPlanAdapter extends BaseAdapter {

    private List<PlanInfo> data;
    private LayoutInflater inflater;
    private Context context;

    public MyPlanAdapter(Context c) {
        context = c;
        inflater = LayoutInflater.from(context);
        data = new ArrayList<PlanInfo>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public PlanInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<PlanInfo> infos) {
        if (data == null) {
            data = new ArrayList<PlanInfo>();
        } else {
            data.clear();
        }
        data.addAll(infos);
        notifyDataSetChanged();
    }

    public void addData(List<PlanInfo> infos) {
        if (data == null) {
            data = new ArrayList<PlanInfo>();
        }
        data.addAll(infos);
        notifyDataSetChanged();
    }

    public void refreshData() {
        if (data == null) {
            data = new ArrayList<PlanInfo>();
        } else {
            data.clear();
        }
        data.addAll(PlanInfoDBHelper.getOneDayInfos(DateUntil.getCurrentYMD(), context));
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final PlanInfo itemInfo = data.get(position);
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_plan_content, null);
            holder.cBox_is_doing = (CheckBox) convertView.findViewById(cBox_is_doing);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(itemInfo.getContent());
        if (itemInfo.getIs_doing() == 1) {
            holder.cBox_is_doing.setChecked(false);
            if (itemInfo.getLevel() == 2) {
                TextPaint tp = holder.tv_content.getPaint();
                tp.setFakeBoldText(true);
                holder.tv_content.setTextColor(context.getResources().getColor(R.color.E56A47));
            } else {
                TextPaint tp = holder.tv_content.getPaint();
                tp.setFakeBoldText(false);
                holder.tv_content.setTextColor(context.getResources().getColor(R.color.black));
            }
        } else {
            TextPaint tp = holder.tv_content.getPaint();
            tp.setFakeBoldText(false);
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.darkgray));
            holder.cBox_is_doing.setChecked(true);
        }
        holder.cBox_is_doing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    itemInfo.setIs_doing(0);
                } else {
                    itemInfo.setIs_doing(1);
                }
                PlanInfoDBHelper.updateInfo(itemInfo, context);
                refreshData();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public CheckBox cBox_is_doing;
        public TextView tv_content;
    }
}
