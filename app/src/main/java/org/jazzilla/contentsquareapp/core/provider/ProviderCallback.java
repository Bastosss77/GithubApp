package org.jazzilla.contentsquareapp.core.provider;

public interface ProviderCallback<T> {
    void start();
    void finish();
    void success(T res);
    void error(@RepositoryProvider.Error int error);
}
