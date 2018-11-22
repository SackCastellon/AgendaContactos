package es.uji.ei1039.agenda;

import es.uji.ei1039.agenda.controller.EditPersonController;
import es.uji.ei1039.agenda.model.Contact;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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

    public boolean showContactEditDialog(Contact contact) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EditPersonController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
