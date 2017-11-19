package io.eschmann.net.common;

import io.eschmann.model.Opponent;
import io.eschmann.model.Player;

import java.util.ArrayList;

public interface Observer {
    public void updateScoreView();
    public void addLog(String message);
    public Player newPlayerJoined(Opponent opponent, ArrayList<Opponent> opponents);
    void newPlayerAnnouncedHimself(Opponent newOpponent);
}