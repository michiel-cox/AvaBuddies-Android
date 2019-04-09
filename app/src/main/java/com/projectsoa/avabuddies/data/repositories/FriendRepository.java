package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendResponse;
import com.projectsoa.avabuddies.data.services.FriendService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class FriendRepository {
    private FriendService friendService;
    private LoginRepository loginRepository;
    private UserRepository userRepository;

    public FriendRepository(FriendService friendService, LoginRepository loginRepository, UserRepository userRepository) {

        this.friendService = friendService;
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
    }

    private Single<List<String>> getIdsFromFriends(Single<List<FriendResponse>> single){
        return single.map(friends -> {
            // Convert FriendResponse to the id of the friend.

            List<String> friendIds = new ArrayList<>();
            if(!loginRepository.isLoggedIn()) return friendIds;

            User currentUser = loginRepository.getLoggedInUser().getUser();
            for (FriendResponse friendResponse : friends) {
                String friendId;
                if(currentUser.getId().equals(friendResponse.friend1)){
                    friendId = friendResponse.friend2;
                }else{
                    friendId = friendResponse.friend1;
                }

                friendIds.add(friendId);
            }

            return friendIds;
        });
    }

    private Single<List<User>> getUsersFromIds(Single<List<String>> single){
        return single.flatMap(friendIds -> {
            // Retrieve user objects
            List<Single<User>> singles = new ArrayList<>();
            for (String friendId : friendIds) {
                singles.add(userRepository.getUser(friendId));
            }
            return Single.mergeDelayError(singles).toList();
        });
    }

    public Single<List<User>> getFriends(){
        return getUsersFromIds(getIdsFromFriends(friendService.fetchFriends().map(friendsResponse -> friendsResponse.friends)));
    }

    public Single<List<User>> getRequests(){
        return getUsersFromIds(getIdsFromFriends(friendService.fetchRequests().map(requestsResponse -> requestsResponse.requests)));
    }

    public Single<ConnectionStatus> getConnectionStatus(String friendId){
        return friendService.fetchConnections().map(connectionsResponse -> connectionsResponse.connections).map(friendResponses -> {

            // Find the FriendResponse related to `userId`
            FriendResponse friendResponse = null;
            for (FriendResponse response : friendResponses) {
                if(friendId.equals(response.friend1) || friendId.equals(response.friend2))
                {
                    friendResponse = response;
                }
            }

            if(friendResponse == null) return ConnectionStatus.UNKNOWN;
            if(friendResponse.confirmed) return ConnectionStatus.ACCEPTED;
            if(friendId.equals(friendResponse.friend1)) return ConnectionStatus.RECEIVED;
            return ConnectionStatus.SEND;
        });
    }

    public Completable request(String friendId){
        return friendService.doRequest(friendId).ignoreElement();
    }

    public Completable cancelRequest(String friendId){
        return friendService.doCancelRequest(friendId).ignoreElement();
    }

    public Completable denyRequest(String friendId){
        return friendService.doDenyRequest(friendId).ignoreElement();
    }

    public Completable acceptRequest(String friendId){
        return friendService.doAcceptRequest(friendId).ignoreElement();
    }

    public enum ConnectionStatus {
        SEND,
        RECEIVED,
        ACCEPTED,
        UNKNOWN,
    }
}
