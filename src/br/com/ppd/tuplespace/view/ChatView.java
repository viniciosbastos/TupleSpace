package br.com.ppd.tuplespace.view;

import br.com.ppd.tuplespace.models.Environment;
import br.com.ppd.tuplespace.models.Message;
import br.com.ppd.tuplespace.models.User;
import br.com.ppd.tuplespace.service.ChatService;
import br.com.ppd.tuplespace.service.JavaSpaceService;
import br.com.ppd.tuplespace.service.ServiceUnavailable;
import br.com.ppd.tuplespace.util.ObservableStringBufferBinding;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class ChatView extends VBox {
    private TextField usernameField;
    private Button loginButton;
    private TextArea chatArea;
    private TextField messageField;
    private Label envLabel;
    private ObservableStringBufferBinding chatTextBinding;
    private JavaSpaceService service;
    private ChatService chatService;
    private User loggedUser;

    public ChatView() {
        this.service = JavaSpaceService.getInstance();
        this.setSpacing(5);
        initElements();
        this.getChildren().addAll(usernameField, loginButton, envLabel, chatArea, messageField);
    }

    private void onLogin() {
        try {
            loggedUser = this.service.searchUser(new User(this.usernameField.getText()));
            if (loggedUser != null) {
                startChatService();
                envLabel.setText("Logged on environment: " + loggedUser.environment.name);
            }
        } catch (ServiceUnavailable serviceUnavailable) {
            serviceUnavailable.printStackTrace();
        }
    }

    private void startChatService() {
        this.chatService = new ChatService(this.loggedUser, this.chatTextBinding);
        new Thread(this.chatService).start();
    }

    private void initElements() {
        usernameField = new TextField();
        usernameField.setPrefWidth(345);
        usernameField.setPromptText("Username");

        loginButton = new Button("Login");
        loginButton.setOnMouseClicked(v -> onLogin());

        envLabel = new Label("Please log in.");

        chatTextBinding = new ObservableStringBufferBinding();
        chatArea = new TextArea();
        chatArea.setPrefHeight(400);
        chatArea.textProperty().bind(this.chatTextBinding);

        messageField = new TextField();
        messageField.setPrefHeight(30);
        messageField.setPromptText("Message");
        messageField.setOnKeyTyped(v -> keyTyped(v.getCharacter()));
    }

    private void keyTyped(String code) {
        if (code.equals("\r")) {
            String message = this.messageField.getText();
            Message msg = new Message();
            msg.content = message;
            msg.sender = loggedUser.name;
            msg.env = loggedUser.environment.name;
            try {
                this.service.send(msg);
                this.messageField.clear();
            } catch (ServiceUnavailable serviceUnavailable) {
                serviceUnavailable.printStackTrace();
            }
        }
    }

    public void closeServices() {
        if (chatService != null) this.chatService.stop();
    }
}
