package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.Dialog;
import com.projectsoa.avabuddies.data.models.responses.chat.DialogResponse;
import com.projectsoa.avabuddies.data.models.responses.chat.DialogListResponse;
import com.projectsoa.avabuddies.data.services.DialogService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class DialogRepository {
    private DialogService dialogService;

    public DialogRepository(DialogService dialogService){ this.dialogService = dialogService; }

    public Single<List<Dialog>> getList(){
        return dialogService.fetchList().map(DialogListResponse -> {
            List<Dialog> dialogs = new ArrayList<>();
            for(DialogResponse dialog : DialogListResponse.dialogs){
                dialogs.add(new Dialog(dialog));
            }
            return dialogs;
        });
    }

    public Single<Dialog> addChat(String id){
        return  dialogService.addChat(id).map(DialogResponse -> {
            return  new Dialog(DialogResponse);
        });
    }
}
