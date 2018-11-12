package es.uji.ei1039.agenda;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App extends Application {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Override
    public void start(final Stage primaryStage) {
        log.debug("Hello World!");
    }

    public static void main(final String[] args) {
        Application.launch(args);
    }
}
