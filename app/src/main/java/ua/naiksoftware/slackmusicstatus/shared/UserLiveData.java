package ua.naiksoftware.slackmusicstatus.shared;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.slackmusicstatus.Config;
import ua.naiksoftware.slackmusicstatus.login.AuthException;
import ua.naiksoftware.slackmusicstatus.model.FetchProfileResponse;
import ua.naiksoftware.slackmusicstatus.model.FetchTeamResponse;
import ua.naiksoftware.slackmusicstatus.model.User;

/**
 * Created by naik on 07.08.17.
 */

public class UserLiveData extends LiveData<User> {

    private Disposable disposable;
    private Single<User> request;
    MutableLiveData<Throwable> errorsData;

    public UserLiveData(final String accessToken, MutableLiveData<Throwable> errorsData) {
        this.errorsData = errorsData;
        updateAccessToken(accessToken);
    }

    public void updateAccessToken(final String accessToken) {
        if (accessToken == null) request = Single.error(new AuthException());
        else {
            request = Single.zip(Single.create(new SingleOnSubscribe<FetchProfileResponse.Profile>() {
                @Override
                public void subscribe(SingleEmitter<FetchProfileResponse.Profile> e) throws Exception {
                    e.onSuccess(NetworkHelper.get(FetchProfileResponse.class,
                            String.format(Config.Slack.FETCH_PROFILE_URL, accessToken)).profile);
                }
            }), Single.create(new SingleOnSubscribe<FetchTeamResponse.Team>() {
                @Override
                public void subscribe(SingleEmitter<FetchTeamResponse.Team> e) throws Exception {
                    e.onSuccess(NetworkHelper.get(FetchTeamResponse.class,
                            String.format(Config.Slack.FETCH_TEAM_URL, accessToken)).team);
                }
            }), new BiFunction<FetchProfileResponse.Profile, FetchTeamResponse.Team, User>() {
                @Override
                public User apply(FetchProfileResponse.Profile profile, FetchTeamResponse.Team team) throws Exception {
                    return new User(accessToken, profile.firstName + ' ' + profile.lastName,
                            profile.avatar, team.name, team.icon.image_132);
                }
            });
        }
        if (disposable != null && !disposable.isDisposed()) { // Reset subscriber
            onInactive();
            onActive();
        }
    }

    @Override
    protected void onActive() {
        disposable = request.subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                postValue(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                errorsData.postValue(throwable);
            }
        });
    }

    @Override
    protected void onInactive() {
        disposable.dispose();
    }
}
