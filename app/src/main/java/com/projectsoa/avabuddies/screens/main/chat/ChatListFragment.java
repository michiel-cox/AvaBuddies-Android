package com.projectsoa.avabuddies.screens.main.chat;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.projectsoa.avabuddies.R;
import com.projectsoa.avabuddies.core.base.BaseFragment;
import com.projectsoa.avabuddies.data.models.Dialog;
import com.projectsoa.avabuddies.data.repositories.DialogRepository;
import com.projectsoa.avabuddies.screens.main.MainActivity;
import com.projectsoa.avabuddies.utils.Utils;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChatListFragment extends BaseFragment
        implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {

    @Inject
    protected DialogRepository dialogRepository;
    @BindView(R.id.dialogsList)
    private DialogsList dialogsList;
    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;
    protected Utils utils;
    protected List<Dialog> dialogList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utils = new Utils(getContext());
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogRepository.getList().subscribe(dialogs -> {
            runOnUiThread(() -> {
                this.dialogList = dialogs;
                initAdapter();
            });

        }, throwable -> runOnUiThread(() -> utils.showToastError(getString(R.string.error_chats))));
    }

    @Override
    public void onDialogLongClick(Dialog dialog) {
        //utils.showToastMessage(dialog.getDialogName());
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_chat_list;
    }

    private void initAdapter() {
        dialogsAdapter.setItems(dialogList);

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
