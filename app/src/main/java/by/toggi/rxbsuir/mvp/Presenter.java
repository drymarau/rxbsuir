package by.toggi.rxbsuir.mvp;

public interface Presenter<V extends View> {

    void onCreate();

    void onDestroy();

    void attachView(V v);

    void detachView();

    String getTag();

}
