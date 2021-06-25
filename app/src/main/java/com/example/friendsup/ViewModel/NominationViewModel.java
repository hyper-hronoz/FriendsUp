package com.example.friendsup.ViewModel;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.repository.NominationRepository;

public class NominationViewModel extends ViewModel {

    private MutableLiveData<RegisteredUser> user;
    private NominationRepository nominationRepository;

    public LiveData<RegisteredUser> getUser() {
        if (user == null) {
            user = new MutableLiveData<RegisteredUser>();
            loadUser();
        }

        return user;
    }

    private void loadUser() {
        nominationRepository.getDataFromRemotes();
    }
}
