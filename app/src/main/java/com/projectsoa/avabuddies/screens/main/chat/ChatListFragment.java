package com.projectsoa.avabuddies.screens.main.chat;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Dialog;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.screens.main.chat.Temp.DialogsFixtures;
import com.projectsoa.avabuddies.utils.Utils;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

public class ChatListFragment extends BaseFragment
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    private DialogsList dialogsList;
    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;
    protected Utils utils;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dialogsList = view.findViewById(R.id.dialogsList);
        utils = new Utils(getContext());
        initAdapter();
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        utils.showToastMessage(dialog.getDialogName());
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_chat_list;
    }

    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        dialogsAdapter.setOnDialogClickListener(this);
        dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(dialogsAdapter);
    }

    @Override
    public void onDialogClick(Dialog dialog) {
        ChatFragment fragment = ChatFragment.newInstance();
        ((MainActivity) getActivity()).loadFragment(fragment);
    }
}
