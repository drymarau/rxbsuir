package by.toggi.rxbsuir.mvp;

public abstract class Presenter<V extends View> {

    private V mView;

    public abstract void onCreate();

    public abstract void onDestroy();

    /**
     * Attach view.
     *
     * @param view the view
     */
    public final void attachView(V view) {
        if (view == null) {
            throw new NullPointerException("View must not be null");
        }
        mView = view;
    }

    protected final void detachView() {
        mView = null;
    }

    protected final V getView() {
        return mView;
    }

    protected final boolean isViewAttached() {
        return mView != null;
    }

    /**
     * Gets presenter tag.
     *
     * @return the tag
     */
    public String getTag() {
        return this.getClass().getSimpleName();
    }

}
