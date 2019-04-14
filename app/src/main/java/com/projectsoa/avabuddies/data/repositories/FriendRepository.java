package com.projectsoa.avabuddies.data.repositories;

import com.projectsoa.avabuddies.Constants;
import com.projectsoa.avabuddies.data.models.User;
import com.projectsoa.avabuddies.data.models.responses.friend.FriendResponse;
import com.projectsoa.avabuddies.data.services.FriendService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        return single.flatMap(friendIds -> userRepository.getList().map(users -> {
            // Retrieve user objects from friend Ids
            List<User> output = new ArrayList<>();
            for (User user : users) {
                if(friendIds.contains(user.getId())){
                    output.add(user);
                }
            }
            return output;
        }));
    }

    public Single<List<User>> getFriends(){
        return getUsersFromIds(getIdsFromFriends(friendService.fetchFriends().map(friendsResponse -> friendsResponse.friends)));
    }

    public Single<List<User>> getReceivedRequests(){
        return getUsersFromIds(getIdsFromFriends(friendService.fetchRequests().map(requestsResponse -> requestsResponse.requests)));
    }
    public Single<List<User>> getSendRequests(){
        return getUsersFromIds(getIdsFromFriends(friendService.fetchRequests().map(requestsResponse -> requestsResponse.ownRequests)));
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
            if(!friendId.equals(friendResponse.friend1)) return ConnectionStatus.SEND;
            if(friendResponse.validated) return ConnectionStatus.VALIDATED;
            return ConnectionStatus.RECEIVED;
        });
    }

    public Completable request(String friendId){
        return friendService.doRequest(friendId).ignoreElement();
    }

    public Completable cancelRequest(String friendId){
        return friendService.doCancelRequest(friendId).ignoreElement();
    }

    public Completable validateRequest(String friendId){
        return friendService.doValidateRequest(friendId).ignoreElement();
    }

    public Completable denyRequest(String friendId){
        return friendService.doDenyRequest(friendId).ignoreElement();
    }

    public Completable acceptRequest(String friendId){
        return friendService.doAcceptRequest(friendId).ignoreElement();
    }

    public Completable isValidRequest(String friendId, Date dateTime) {

        // Validate Time
        long msNow = new Date().getTime();
        long msThen = dateTime.getTime();
        long msDiff = msNow - msThen;
        if(msDiff > Constants.QR_VALID_SECONDS  * 1000){
            return Completable.error(new Exception("This QR code is invalid.")); // The QR Code is only an x amount of time valid.
        }

        // Validate user
        return getSendRequests().map(users -> {
            for (User user : users) {
                if(user.getId().equals(friendId)) return user;
            }
            return new Exception("This QR code is invalid.");
        }).ignoreElement();
    }

    public enum ConnectionStatus {
        SEND,
        RECEIVED,
        ACCEPTED,
        VALIDATED,
        UNKNOWN,
    }
}
