package com.lhanman.lhanweather.fragment;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import com.lhanman.lhanweather.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicFragment extends Fragment implements View.OnClickListener {
    //定义三个按钮并实例化MediaPlayer
    private MediaPlayer mediaPlayer;

    protected TextView tv_start,tv_title;
    protected TextView tv_end;
    protected SeekBar seekBar;
    private AppCompatCheckBox ck;
    private Timer timer;//定时器
    private boolean isSeekbarChaning;//互斥变量，防止进度条和定时器冲突。
    private Button play,pre,next,btn_add,btn_sub;
    private Button pause;
    private Button stop;
    private ListView listview;
    private int current = 0;

    private List<String> stringList = new ArrayList<>();
    private AssetManager assetManager;

    MusicAdapter musicAdapter;
    private boolean isPause = false;
    private boolean isRandom = false;
    private AudioManager audioManager=null; //音频
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=  inflater.inflate(R.layout.fragment_music, container, false);


        audioManager=(AudioManager)getActivity().getSystemService(Service.AUDIO_SERVICE);
        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);
        stop = view.findViewById(R.id.stop);
        btn_sub = view.findViewById(R.id.btn_sub);
        btn_add = view.findViewById(R.id.btn_add);
        listview = view.findViewById(R.id.listview);
        tv_title = view.findViewById(R.id.tv_title);
        ck = view.findViewById(R.id.ck);
        pre = view.findViewById(R.id.pre);
        next = view.findViewById(R.id.next);
        tv_start = view.findViewById(R.id.tv_start);
        tv_end = view.findViewById(R.id.tv_end);
        seekBar = view.findViewById(R.id.seekbar);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        initView();//初始化进度条

        stringList.add("方大同 - 特别的人");
        stringList.add("毛不易 - 平凡的一天 (Live)");
        stringList.add("时代少年团 - 渐暖");
        stringList.add("周杰伦 - 我是如此相信");
        musicAdapter = new MusicAdapter(getActivity(),stringList);
        listview.setAdapter(musicAdapter);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isRandom){
                    int s=(int) (Math.random()*3);
                    current = s;
                    Toast.makeText(getActivity(),"开始随机播放第"+(s+1)+"首",Toast.LENGTH_SHORT).show();
                    playNum(true);
                }else {
                    Toast.makeText(getActivity(),"开始顺序播放第"+(current+1)+"首",Toast.LENGTH_SHORT).show();
                    playNum(true);
                }
            }
        });
        ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRandom = isChecked;
            }
        });
        initMediaPlayer();//初始化MediaPlayer
        return  view;
    }


    private void openAssetMusics(String mp) {
        //打开Asset目录
        assetManager = getActivity().getAssets();

        try {
            //打开音乐文件shot.mp3
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(mp);
            mediaPlayer.reset();
            //设置媒体播放器的数据资源
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            int duration2 = mediaPlayer.getDuration() / 1000;
            int position = mediaPlayer.getCurrentPosition();

            seekBar.setMax( mediaPlayer.getDuration());//将音乐总时间设置为Seekbar的最大值


            tv_start.setText(calculateTime(position / 1000));
            tv_end.setText(calculateTime(duration2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 初始化
     * */
    private void initView(){

        //绑定监听器，监听拖动到指定位置
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int duration2 = mediaPlayer.getDuration() / 1000;//获取音乐总时长
                int position = mediaPlayer.getCurrentPosition();//获取当前播放的位置
                tv_start.setText(calculateTime(position / 1000));//开始时间
                tv_end.setText(calculateTime(duration2));//总时长
            }
            /*
             * 通知用户已经开始一个触摸拖动手势。
             * */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekbarChaning = true;
            }
            /*
             * 当手停止拖动进度条时执行该方法
             * 首先获取拖拽进度
             * 将进度对应设置给MediaPlayer
             * */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekbarChaning = false;
                mediaPlayer.seekTo(seekBar.getProgress());//在当前位置播放
                tv_start.setText(calculateTime(mediaPlayer.getCurrentPosition() / 1000));
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNum(false);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNum(true);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE,
                        AudioManager.FLAG_SHOW_UI);  //调高声音
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER,
                        AudioManager.FLAG_SHOW_UI);//调低声音
            }
        });
//        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete(MediaPlayer mp) {
//                Log.e("---------","播放完成333");
//            }
//        });

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

    }

    //计算播放时间
    public String calculateTime(int time) {
        int minute;
        int second;
        if (time >= 60) {
            minute = time / 60;
            second = time % 60;
            //分钟在0~9
            if (minute < 10) {
                //判断秒
                if (second < 10) {
                    return "0" + minute + ":" + "0" + second;
                } else {
                    return "0" + minute + ":" + second;
                }
            } else {
                //分钟大于10再判断秒
                if (second < 10) {
                    return minute + ":" + "0" + second;
                } else {
                    return minute + ":" + second;
                }
            }
        } else {
            second = time;
            if (second >= 0 && second < 10) {
                return "00:" + "0" + second;
            } else {
                return "00:" + second;
            }
        }
    }
    /*
     * 初始化MediaPlayer
     * */
    private void initMediaPlayer(){
        tv_title.setText(stringList.get(0));
//        openAssetMusics("music01.mp3");
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.play:
                if (mediaPlayer == null){
                    mediaPlayer = new MediaPlayer();
                }
                if (!isPause){
                    openAssetMusics("方大同 - 特别的人.mp3");
                    mediaPlayer.start();

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(!isSeekbarChaning){
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            }
                        }
                    },0,1000);
                }else {
                    isPause = false;
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();//暂停播放
                    isPause = true;
                }
                break;
            case R.id.stop:
                if(mediaPlayer==null){
                    return;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                isPause = false;
                timer.cancel();
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                tv_start.setText("00:00");
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer!=null){
            timer.cancel();
        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();

        }
    }


    private void playNum(boolean add){
        if (add){
            if (current==3){
                current=-1;
            }
            ++current;
        }else {
            if (current==0){
                current=4;
            }
            --current;
        }
        Log.e("======",current+"");
        switch (current){
            case  0:
                tv_title.setText(stringList.get(0));
                openAssetMusics("方大同 - 特别的人.mp3");
                mediaPlayer.start();//开始播放
                break;
            case  1:
                tv_title.setText(stringList.get(1));
                openAssetMusics("毛不易 - 平凡的一天 (Live).mp3");
                mediaPlayer.start();//开始播放'
                break;
            case 2:
                tv_title.setText(stringList.get(2));
                openAssetMusics("时代少年团 - 渐暖.mp3");
                mediaPlayer.start();//开始播放'
                break;
            case  3:
                tv_title.setText(stringList.get(3));
                openAssetMusics("周杰伦 - 我是如此相信.mp3");
                mediaPlayer.start();//开始播放'
                break;
        }
    }

    public class MusicAdapter extends BaseAdapter {
        private Context context;
        private List<String> shopList;

        public MusicAdapter(Context context,List<String> shopList){
            this.context = context;
            this.shopList = shopList;
        }
        @Override
        public int getCount() {
            return shopList.size();
        }

        @Override
        public Object getItem(int position) {
            return shopList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
                holder.tv_title = convertView.findViewById(R.id.tv_title);
                holder.ll = convertView.findViewById(R.id.ll);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_title.setText(shopList.get(position));
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playNum(true);
                }
            });
            return convertView;
        }

        class ViewHolder{
            LinearLayout ll;
            TextView tv_title;
        }
    }

}