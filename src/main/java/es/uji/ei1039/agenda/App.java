package es.uji.ei1039.agenda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public final class App extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(final Stage primaryStage) {
        log.debug("Starting application");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Address Book");

        initRootLayout();
        showContactOverview();
    }

    private void initRootLayout() {
        log.debug("Loading Root Layout");
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/uji/ei1039/agenda/views/RootLayout.fxml"));
            rootLayout = loader.load();

            final @NotNull Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (final IOException e) {
            log.error("Error loading 'RootLayout.fxml'", e);
        }
    }

    private void showContactOverview() {
        log.debug("Loading Contact Overview");
        try {
            final @NotNull FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/uji/ei1039/agenda/views/ContactOverview.fxml"));
            final @NotNull BorderPane contactOverview = loader.load();

            rootLayout.setCenter(contactOverview);
        } catch (final IOException e) {
            log.error("Error loading 'ContactOverview.fxml'", e);
        }
    }

    public static void main(final @NotNull String[] args) {
        log.debug("Launching application");
        Application.launch(args);
        log.debug("Exiting application");
    }
}
