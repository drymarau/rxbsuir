package by.toggi.rxbsuir.component;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.module.AppModule;
import by.toggi.rxbsuir.module.BsuirServiceModule;
import by.toggi.rxbsuir.module.DbModule;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        BsuirServiceModule.class,
        DbModule.class
})
public interface AppComponent {

    RxBsuirApplication app();

    BsuirService service();

    StorIOSQLite storIOSQLite();

}
