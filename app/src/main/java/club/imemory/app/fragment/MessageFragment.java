package club.imemory.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import club.imemory.app.R;
import club.imemory.app.adapter.MessageAdapter;
import club.imemory.app.db.Message;
import club.imemory.app.http.HttpManager;
import club.imemory.app.json.JsonAnalyze;
import club.imemory.app.util.AppManager;
import club.imemory.app.util.SnackbarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author: 张杭
 * @Date: 2017/3/29 12:04
 */

public class MessageFragment extends Fragment implements View.OnClickListener {

    private static MessageFragment mMessageFragment = null;
    private List<Message> mMessageList = new ArrayList<>();
    private MessageAdapter adapter;
    private RecyclerView mRecyclerView;
    private EditText mSendMsg;
    private Button mSendBtn;
    private ImageView mEmoticonBtn;

    private static String MSG_API_KEY = "534dc342ad15885dffc10d7b5f813451";
    private static String MSG_URL = "http://www.tuling123.com/openapi/api";

    /**
     * 实例化MessageFragment
     *
     * @return
     */
    public static MessageFragment instanceFragment() {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        return mMessageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        mSendMsg = (EditText) view.findViewById(R.id.et_send_msg);
        mSendMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    AppManager.logI("ssssss",mMessageList.size()+"");
                    mRecyclerView.scrollToPosition(mMessageList.size()-1);
                }
            }
        });
        mSendBtn = (Button) view.findViewById(R.id.btn_send);
        mSendBtn.setOnClickListener(this);
        mEmoticonBtn = (ImageView) view.findViewById(R.id.btn_emoticon);
        mEmoticonBtn.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        //adapter = new MessageAdapter(mMessageList);
        //recyclerView.setAdapter(adapter);
        return view;
    }

    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMessageList.clear();
                List<Message> list = DataSupport.limit(20).find(Message.class);
                mMessageList.addAll(list);
                mMessageList.add(new Message(100000,"我是小黑，来撩我吧"));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter == null) {
                            adapter = new MessageAdapter(mMessageList);
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.scrollToPosition(mMessageList.size()-1);
                        } else {
                            adapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(mMessageList.size()-1);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.btn_emoticon:
                break;
        }
    }

    private void sendMsg() {
        String msg = mSendMsg.getText().toString().trim();
        mSendMsg.setText(null);
        if (TextUtils.isEmpty(msg)) {
            AppManager.showToast("消息不能为空");
        } else {
            Message message = new Message();
            message.setCode(718);
            message.setText(msg);
            message.setAvatar("");
            message.setCreatetime(new Date());
            message.setUpdatetime(new Date());
            message.save();
            mMessageList.add(message);
            if (adapter == null) {
                adapter = new MessageAdapter(mMessageList);
                mRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mMessageList.size()-1);
            }
            getTuLing(msg);
        }
    }

    private void getTuLing(String msg) {
        msg = MSG_URL + "?key=" + MSG_API_KEY + "&info=" + msg;
        HttpManager.sendOKHttpRequest(msg, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Message tulingMessage = JsonAnalyze.handleMessageResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tulingMessage != null) {
                            mMessageList.add(tulingMessage);
                            adapter.notifyDataSetChanged();
                            mRecyclerView.smoothScrollToPosition(mMessageList.size()-1);
                        } else {
                            AppManager.showToast("消息接收失败");
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppManager.showToast("消息接收失败");
                    }
                });
            }
        });
    }
    private void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }
}
