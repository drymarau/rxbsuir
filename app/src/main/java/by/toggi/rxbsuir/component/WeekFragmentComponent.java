package by.toggi.rxbsuir.component;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.fragment.WeekFragment;
import by.toggi.rxbsuir.module.WeekFragmentModule;
import dagger.Component;

@Fragment
@Component(dependencies = AppComponent.class, modules = WeekFragmentModule.class)
public interface WeekFragmentComponent {

    void inject(WeekFragment weekFragment);

}
