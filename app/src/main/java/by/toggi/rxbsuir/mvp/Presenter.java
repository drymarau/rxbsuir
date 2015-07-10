package by.toggi.rxbsuir.mvp;

public interface Presenter {

    void onCreate();

    void onDestroy();

    void attachView(View view);

    void detachView();

}
