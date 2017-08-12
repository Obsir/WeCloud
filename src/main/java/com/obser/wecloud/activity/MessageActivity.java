package com.obser.wecloud.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.obser.wecloud.R;
import com.obser.wecloud.app.WeCloudApplication;
import com.obser.wecloud.bean.Dialog;
import com.obser.wecloud.bean.Message;
import com.obser.wecloud.bean.MessagesListProvider;
import com.obser.wecloud.bean.ProtocolMessage;
import com.obser.wecloud.bean.ChatUser;
import com.obser.wecloud.bean.User;
import com.obser.wecloud.core.ClientCoreSDK;
import com.obser.wecloud.core.LocalUDPDataSender;
import com.obser.wecloud.protocol.Protocol;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

import java.text.SimpleDateFormat;
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
    private static User mUser;
    private static Dialog mDialog;
    private static String mTo_user_ip;
    private static DialogsListAdapter<Dialog> mDialogsListAdapter;
    private TextView tv_title;
    private Button btn_group;
    private Button btn_person;

    /* 自定义 end */
    public static void open(Context context, String to_user_ip){
        context.startActivity(new Intent(context, MessageActivity.class));
        mTo_user_ip = to_user_ip;
    }


    public static void open(Context context, Dialog dialog, DialogsListAdapter<Dialog> dialogsListAdapter) {
        context.startActivity(new Intent(context, MessageActivity.class));
        mDialog = dialog;
        mDialogsListAdapter = dialogsListAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_styled_messages);
        initView();
        initData();
        initListener();
        initBar();
        initAdapter();

        input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(this);
        input.setAttachmentsListener(this);
    }

    private void initListener() {
        if(mDialog.getMode()){
            btn_group.setVisibility(View.GONE);
            btn_person.setVisibility(View.VISIBLE);
        } else {
            btn_group.setVisibility(View.VISIBLE);
            btn_person.setVisibility(View.GONE);
        }
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, GroupDetailActivity.class);
                intent.putExtra("users", mDialog.getUsers());
                startActivity(intent);
            }
        });
        btn_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, PrivateChatDetailActivity.class);
                intent.putExtra("users", mDialog.getUsers());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        messagesList = (MessagesList) findViewById(R.id.messagesList);
        btn_group = (Button) findViewById(R.id.btn_group);
        btn_person = (Button) findViewById(R.id.btn_person);
    }

    private void initData() {
        WeCloudApplication application = (WeCloudApplication) getApplication();
        mUser = application.getUser();
        imageLoader = mDialogsListAdapter.getImageLoader();
        mDialog.setUnreadCount(0);
        mDialogsListAdapter.updateItemById(mDialog);
        tv_title.setText(mDialog.getDialogName());
//         mChatUser = new ChatUser("0", UDPUtils.getIPAddress(this), "nnn", UDPUtils.getIPAddress(this), true);
//        messages = ClientCoreSDK.getInstance().getChatTransDataEvent().getMessages();
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
        final Message message = new Message(System.currentTimeMillis() + "", mUser, input.toString());
        mDialogsListAdapter.updateDialogWithMessage(mDialog.getId(), message);
        if(mDialog != null){
            boolean mode = mDialog.getMode();
            messagesAdapter.addToStart(message, true);
            List<Message> list = MessagesListProvider.getMessagesById(mDialog.getId());
            list.add(message);
            for(User user : mDialog.getUsers()){
                if(user.getIp().equals(mUser.getIp()))
                    continue;
                new LocalUDPDataSender.SendCommonDataAsync(this, Protocol.packMessage(new ProtocolMessage(mUser, mDialog.getId(), input.toString(), "text", mode)), user.getIp()){
                    @Override
                    protected void onPostExecute(Integer code) {
//                   Log.d("ConversationFragment", mDialog.getId() + ":" + input.toString());
                    }
                }.execute();
            }
        } else
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
        List<Message> messages = MessagesListProvider.getMessagesById(mDialog.getId());
        Log.d("loadMessages", messages.toString());
        if(!messages.isEmpty())
            messagesAdapter.addToEnd(messages, false);
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
//            loadMessages();
        }
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
        bar.findViewById(R.id.btn_delete).setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        bar.findViewById(R.id.btn_copy).setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    protected void loadMessages() {
//        new Handler().postDelayed(new Runnable() { //imitation of internet connection
//            @Override
//            public void run() {
//                ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
//                lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
//                if(!messages.isEmpty()){
//                    Log.d("SubList", messages.size() + "");
//                    messagesAdapter.addToEnd(messages.subList(0, messages.size() - 1), false);
//                }
//            }
//        }, 1000);
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
