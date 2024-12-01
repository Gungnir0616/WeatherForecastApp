package com.lhanman.lhanweather.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lhanman.lhanweather.R;
import com.lhanman.lhanweather.UI.AddcaidanActivity;
import com.lhanman.lhanweather.UI.CaidandetailActivity;
import com.lhanman.lhanweather.UI.ChangeActivity;
import com.lhanman.lhanweather.base.LazyFragment;
import com.lhanman.lhanweather.db.Redian;
import com.lhanman.lhanweather.db.RedianDbutils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShipuFragment extends LazyFragment {
    @BindView(R.id.data_list)
    ListView gvShow;
    Medicadapter medicadapter;

    @BindView(R.id.tv_select)
    TextView tvselect;
    @BindView(R.id.add)
    TextView tvadd;
    @BindView(R.id.login_et_zh)
    EditText etselect;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shipu;
    }

    @Override
    protected void loadData() {
        medicadapter = new Medicadapter(getActivity(), RedianDbutils.getInstance(getActivity()).load());
        gvShow.setAdapter(medicadapter);
        tvadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddcaidanActivity.class);
                startActivityForResult(intent,101);
            }
        });
        tvselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aa=etselect.getText().toString();
                if(TextUtils.isEmpty(aa)){
                    showToast("请检查搜索内容");
                    return;
                }else
                {      medicadapter=new Medicadapter(getActivity(), RedianDbutils.getInstance(getActivity()).loadsousuo(aa));
                    gvShow.setAdapter(medicadapter);}
            }
        });

    }

    private void showAlertDialog(Redian medic) {
        String[] items = {"查看详情", "修改", "删除"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择操作")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = items[which];
                        switch (str) {
                            case "查看详情":
                                Intent intent = new Intent(getActivity(), CaidandetailActivity.class);
                                intent.putExtra("bean", medic);
                                startActivity(intent);
                                break;
                            case "修改":
                                Intent intent1 = new Intent(getActivity(), ChangeActivity.class);
                                intent1.putExtra("bean", medic);
                                startActivityForResult(intent1, 102);
                                break;
                            case "删除":
                                RedianDbutils.getInstance(getActivity()).delete(getActivity(), medic.getId() + "");
                                medicadapter = new Medicadapter(getActivity(), RedianDbutils.getInstance(getActivity()).load());
                                gvShow.setAdapter(medicadapter);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        medicadapter=new Medicadapter(getActivity(), RedianDbutils.getInstance(getActivity()).load());
        gvShow.setAdapter(medicadapter);
    }

    class Medicadapter extends BaseAdapter {
        private Context context;
        private List<Redian> listdata;

        public Medicadapter(Context context, List<Redian> listdata) {
            this.context = context;
            this.listdata = listdata;
            if (this.listdata == null) {
                this.listdata = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return listdata.size();
        }

        @Override
        public Object getItem(int position) {
            return listdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Redian medic = listdata.get(position);
            Log.e("app", "item = " + medic.toString());

            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(context).inflate(R.layout.item_memo,null);
                viewHolder.iv_pic=convertView.findViewById(R.id.ivPic);
                viewHolder.tv_author=convertView.findViewById(R.id.tv_author);
                viewHolder.tv_tome=convertView.findViewById(R.id.tv_time);
                viewHolder.tv_text=convertView.findViewById(R.id.tvText);
                viewHolder.tv_leixing=convertView.findViewById(R.id.tv_leixing);
                viewHolder.ll=convertView.findViewById(R.id.ll);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_leixing.setText(listdata.get(position).getLeixing());
            viewHolder.tv_author.setText(listdata.get(position).getAuthor());
            viewHolder.tv_text.setText(listdata.get(position).getBiaoti());
            viewHolder.tv_tome.setText(listdata.get(position).getTimest());
            Glide.with(context).load(listdata.get(position).getImag()).into(viewHolder.iv_pic);
            viewHolder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog(listdata.get(position));
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView iv_pic;
        TextView tv_text,tv_leixing,tv_author,tv_tome;
        LinearLayout ll;
    }

}