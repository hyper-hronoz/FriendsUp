package com.example.friendsup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.repository.NominationRepository;

public class NominationViewModel extends ViewModel {

    private MutableLiveData<RegisteredUser> user;
    private NominationRepository nominationRepository;

    public LiveData<RegisteredUser> getUser() {
        user = new MutableLiveData<RegisteredUser>();
        loadUser();
        return user;
    }

    private void loadUser() {
        nominationRepository = new NominationRepository(user);
        nominationRepository.getDataFromRemotes();
    }
}
