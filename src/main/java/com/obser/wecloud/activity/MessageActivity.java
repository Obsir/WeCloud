package com.obser.wecloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.obser.wecloud.R;
import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.bean.Message;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.core.ChatTransDataEvent;
import com.obser.wecloud.core.ClientCoreSDK;
import com.obser.wecloud.core.LocalUDPDataSender;
import com.obser.wecloud.fixtures.Avatars;
import com.obser.wecloud.fixtures.MessagesFixtures;
import com.obser.wecloud.utils.UDPUtils;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Obser on 2017/8/3.
 */

public class MessageActivity extends AppCompatActivity implements MessageInput.InputListener,
        MessageInput.AttachmentsListener,
        DateFormatter.Formatter, MessagesListAdapter.SelectionListener,
        MessagesListAdapter.OnLoadMoreListener{

    private static final int TOTAL_MESSAGES_COUNT = 100;

    protected final String senderId = "0";
    protected ImageLoader imageLoader;
    protected MessagesListAdapter<Message> messagesAdapter;

    private int selectionCount;
    private Date lastLoadedDate;
    private MessagesList messagesList;
    private View bar;
    private MessageInput input;

    /* 自定义 start */
    private User mUser;
    private static Dialog mDialog;
    private static String mTo_user_ip;
    private ArrayList<Message> messages;
    /* 自定义 end */
    public static void open(Context context, String to_user_ip){
        context.startActivity(new Intent(context, MessageActivity.class));
        mTo_user_ip = to_user_ip;
    }


    public static void open(Context context, Dialog dialog) {
        context.startActivity(new Intent(context, MessageActivity.class));
        mDialog = dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styled_messages);
        initData();
        initBar();
        messagesList = (MessagesList) findViewById(R.id.messagesList);
        initAdapter();

        input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);
    }

    private void initData() {
         mUser = new User("0", UDPUtils.getIPAddress(this), Avatars.getAvatar(), true);
        messages = ClientCoreSDK.getInstance().getChatTransDataEvent().getMessages();
    }

    private void initBar() {
        bar = findViewById(R.id.base_layout);
        bar.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesAdapter.deleteSelectedMessages();
            }
        });
        bar.findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesAdapter.copySelectedMessagesText(MessageActivity.this, getMessageStringFormatter(), true);
                Toast.makeText(MessageActivity.this, R.string.copied_message, Toast.LENGTH_SHORT).show();
            }
        });
        bar.findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        onSelectionChanged(0);
    }

    @Override
    public boolean onSubmit(CharSequence input) {
//        messagesAdapter.addToStart(
//                MessagesFixtures.getTextMessage(input.toString()), true);
        final Message message = new Message(mUser.getId(), mUser, input.toString());
        if(mDialog != null)
           new LocalUDPDataSender.SendCommonDataAsync(this, input.toString(), mDialog.getId()){
               @Override
               protected void onPostExecute(Integer code) {
                   messagesAdapter.addToStart(message, true);
                   ClientCoreSDK.getInstance().getChatTransDataEvent().getMessages().add(message);
               }
           }.execute();
        else
            new LocalUDPDataSender.SendCommonDataAsync(this, input.toString(), mTo_user_ip){
                @Override
                protected void onPostExecute(Integer code) {
                    messagesAdapter.addToStart(message, true);
                }
            }.execute();
        return true;
    }

    @Override
    public void onAddAttachments() {
//        messagesAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
    }

    @Override
    public String format(Date date) {
        if (DateFormatter.isToday(date)) {
            return getString(R.string.date_header_today);
        } else if (DateFormatter.isYesterday(date)) {
            return getString(R.string.date_header_yesterday);
        } else {
            return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
        }
    }

    private void initAdapter() {
        messagesAdapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesAdapter.enableSelectionMode(this);
        messagesAdapter.setLoadMoreListener(this);
        messagesAdapter.setDateHeadersFormatter(this);
        messagesList.setAdapter(messagesAdapter);
        ClientCoreSDK.getInstance().getChatTransDataEvent().setMessagesAdapter(messagesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        messagesAdapter.addToStart(MessagesFixtures.getTextMessage(), true);
        if(!messages.isEmpty()){
            for(Message message : messages){
                messagesAdapter.addToStart(message, true);
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.menu = menu;
//        getMenuInflater().inflate(R.menu.chat_actions_menu, menu);
//        onSelectionChanged(0);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_delete:
//                messagesAdapter.deleteSelectedMessages();
//                break;
//            case R.id.action_copy:
//                messagesAdapter.copySelectedMessagesText(this, getMessageStringFormatter(), true);
//                Toast.makeText(this, R.string.copied_message, Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
//            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        } else {
            messagesAdapter.unselectAllItems();
        }
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount < TOTAL_MESSAGES_COUNT) {
            loadMessages();
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
        bar.findViewById(R.id.btn_delete).setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        bar.findViewById(R.id.btn_copy).setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    protected void loadMessages() {
        new Handler().postDelayed(new Runnable() { //imitation of internet connection
            @Override
            public void run() {
//                ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
//                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
//                if(!messages.isEmpty()){
//                    Log.d("SubList", messages.size() + "");
//                    messagesAdapter.addToEnd(messages.subList(0, messages.size() - 1), false);
//                }
            }
        }, 1000);
    }

    private MessagesListAdapter.Formatter<Message> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<Message>() {
            @Override
            public String format(Message message) {
                String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault())
                        .format(message.getCreatedAt());

                String text = message.getText();
                if (text == null) text = "[attachment]";

                return String.format(Locale.getDefault(), "%s: %s (%s)",
                        message.getUser().getName(), text, createdAt);
            }
        };
    }
}
