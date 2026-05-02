
package labassignment.pkg2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LabAssignment2 extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent p1 = FXMLLoader.load(getClass().getResource("/views/mainApp.fxml"));
      
        Scene scene = new Scene(p1);
        stage.setScene(scene);
        stage.setTitle("Task Managements System");
        stage.show();
    }
    
}
