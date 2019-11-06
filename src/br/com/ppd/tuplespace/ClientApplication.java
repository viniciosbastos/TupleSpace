package br.com.ppd.tuplespace;

import br.com.ppd.tuplespace.view.ChatView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Chat");
        ChatView view = new ChatView();
        Scene scene = new Scene(view, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest((v) -> view.closeServices());
        primaryStage.show();
    }
}
