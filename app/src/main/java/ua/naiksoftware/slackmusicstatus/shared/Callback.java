package ua.naiksoftware.slackmusicstatus.shared;

/**
 * Created by naik on 24.06.17.
 */

public interface Callback<R> {

    void call(R r);
}
