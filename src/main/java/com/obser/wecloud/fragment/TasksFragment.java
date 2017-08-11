package com.obser.wecloud.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.obser.wecloud.R;
import com.obser.wecloud.activity.MessageActivity;
import com.obser.wecloud.view.SideBar;
import com.obser.wecloud.view.SelectableRoundedImageView;



/**
 * tab 2 任务列表的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class TasksFragment extends Fragment{

    private SelectableRoundedImageView mSelectableRoundedImageView;
    private TextView mNameTextView;
    private TextView mNoFriends;
    private TextView mUnreadTextView;
    private View mHeadView;
    private EditText mSearchEditText;
    private ListView mListView;
    private SideBar mSidBar;

    private TextView mDialogTextView;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        initView(view);
        initData();
//        updateUI();
//        refreshUIListener();
        return view;
    }

    private void initData() {
        final String[] users = new String[]{
                "192.168.1.101",
                "192.168.1.100"
        };
        mListView.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_activated_1, users));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageActivity.open(TasksFragment.this.getActivity(), users[position]);
            }
        });
    }


    private void initView(View view) {
        mSearchEditText = (EditText) view.findViewById(R.id.search);
        mListView = (ListView) view.findViewById(R.id.listview);
        mNoFriends = (TextView) view.findViewById(R.id.show_no_friend);
        mSidBar = (SideBar) view.findViewById(R.id.sidrbar);
        mDialogTextView = (TextView) view.findViewById(R.id.group_dialog);
        mSidBar.setTextView(mDialogTextView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mDialogTextView != null) {
            mDialogTextView.setVisibility(View.INVISIBLE);
        }
    }

}
