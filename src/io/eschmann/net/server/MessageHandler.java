package io.eschmann.net.server;

import io.eschmann.model.Player;
import io.eschmann.net.common.Message;
import io.eschmann.model.Opponent;
import io.eschmann.net.common.Observer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageHandler {
    private Socket socket;
    Observer observer;

    public MessageHandler(Socket socket, Observer observer) {
        this.socket = socket;
        this.observer = observer;
    }

    public void handle() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // receive message from opponent
            Message message = (Message) in.readObject();

            switch (message.type) {
                case Message.TYPE_JOIN:
                    Opponent newOpponent = message.opponent;
                    Player player = observer.newPlayerJoined(newOpponent, message.opponents);

                    Message msg = new Message(Message.TYPE_JOIN, "May i join?");
                    msg.opponent = player.toOpponent();
                    msg.opponents = player.getOpponents();

                    // respond
                    out.writeObject(msg);
                    observer.addLog(newOpponent + " joined.");
                    break;
                case Message.TYPE_ANNOUNCE:
                    Opponent anonuncedOpponent = message.opponent;
                    observer.newPlayerAnnouncedHimself(anonuncedOpponent);
                    observer.addLog(anonuncedOpponent + " joined.");
                default:
                    System.out.println("Unsupported message received...");
            }

            socket.close();
            observer.updateScoreView();
        } catch (Exception e) {
            System.out.println("MessageHandler error --> " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
