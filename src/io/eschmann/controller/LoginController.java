package io.eschmann.controller;

import io.eschmann.model.Opponent;
import io.eschmann.model.Player;
import io.eschmann.net.server.ReceiverServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    protected Player player;
    protected Stage primaryStage;
    protected ReceiverServer server;

    @FXML
    public TextField myIpText;

    @FXML
    public TextField usernameInput;

    @FXML
    public TextField joinInput;

    public void onNewGameAction(ActionEvent actionEvent) throws IOException {
        changeToGameState();
    }

    public void onJoinGameAction(ActionEvent actionEvent) throws IOException {
        changeToGameState(true);
    }

    protected void changeToGameState() throws IOException {
        changeToGameState(false);
    }

    protected void changeToGameState(boolean clickedJoin) throws IOException {
        // update username
        player.username = usernameInput.getText();

        // load new view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/game.fxml"));
        Parent root = loader.load();

        // checked if join was clicked
        Opponent opponent = null;
        if (clickedJoin) {
            String[] input = joinInput.getText().split(":");
            opponent = new Opponent(input[0], Integer.parseInt(input[1]));
        }

        // get controller
        GameController gameController = (GameController) loader.getController();
        gameController.init(player, primaryStage, server, opponent);

        // show scene
        primaryStage.getScene().setRoot(root);
        primaryStage.show();
    }

    /**
     * Updates the view with player information and makes the player and stage available to the controller.
     * @param player
     * @param stage
     */
    public void init(Player player, Stage stage, ReceiverServer server) {
        this.player = player;
        this.primaryStage = stage;
        this.server = server;

        myIpText.setText(player.ip + ":" + player.port);
        usernameInput.setText(player.username);
    }
}
