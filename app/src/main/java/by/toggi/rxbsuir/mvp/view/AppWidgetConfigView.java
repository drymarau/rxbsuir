package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.mvp.View;

public interface AppWidgetConfigView extends View {

    void updateSyncIdList(List<SyncIdItem> syncIdMap);

}
