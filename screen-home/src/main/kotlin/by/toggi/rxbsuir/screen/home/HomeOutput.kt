package by.toggi.rxbsuir.screen.home

public sealed interface HomeOutput {

    public object OnStudentGroups : HomeOutput
    public object OnEmployees : HomeOutput
    public object OnBack : HomeOutput
}
