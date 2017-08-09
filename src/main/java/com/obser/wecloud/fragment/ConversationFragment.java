package com.obser.wecloud.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.obser.wecloud.R;
import com.obser.wecloud.activity.MessageActivity;
import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.core.ChatTransDataEventImpl;
import com.obser.wecloud.core.ClientCoreSDK;
import com.obser.wecloud.fixtures.Avatars;
import com.obser.wecloud.fixtures.DialogsFixtures;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.util.Date;

/**
 * Created by Obser on 2017/8/3.
 */

public class ConversationFragment extends Fragment implements DateFormatter.Formatter, DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog>{

    private DialogsList mDialogsList;
    private DialogsListAdapter<Dialog> dialogsListAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        initView(view);
        initData();
        initAdapter();
        initClientSDK();
//        initData();
//        updateUI();
//        refreshUIListener();
        return view;
    }

    private void initClientSDK() {
        ClientCoreSDK.getInstance().init(this.getContext());
        ClientCoreSDK.getInstance().setChatTransDataEvent(new ChatTransDataEventImpl());
        ClientCoreSDK.getInstance().getChatTransDataEvent().setDialogsListAdapter(dialogsListAdapter);
    }

    private void initAdapter() {
        //If you using another library - write here your way to load image
        dialogsListAdapter = new DialogsListAdapter<>(R.layout.item_dialog_fragment, new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //If you using another library - write here your way to load image
                Picasso.with(ConversationFragment.this.getActivity()).load(url).into(imageView);
            }
        });

//        dialogsListAdapter.setItems(DialogsFixtures.getDialogs());

        dialogsListAdapter.setOnDialogClickListener(this);
        dialogsListAdapter.setOnDialogLongClickListener(this);
        dialogsListAdapter.setDatesFormatter(this);

        mDialogsList.setAdapter(dialogsListAdapter);
    }

    private void initData() {
        Avatars.init(this.getContext());
    }


    private void initView(View view) {
        mDialogsList = (DialogsList) view.findViewById(R.id.dialogsList);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        MessageActivity.open(this.getActivity(), dialog);
//        this.getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
//        Toast.makeText(this.getActivity(), "Hello", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return DateFormatter.format(date, DateFormatter.Template.TIME);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else if (DateFormatter.isCurrentYear(date)) {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }
}
