package by.toggi.rxbsuir.component;

import javax.inject.Singleton;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.module.AppModule;
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    RxBsuirApplication app();

}
