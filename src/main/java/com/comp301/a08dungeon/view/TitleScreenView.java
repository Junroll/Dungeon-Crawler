package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TitleScreenView implements FXComponent{
    private final Controller playerController;
    private final Model model;

    public TitleScreenView(Controller playerController, Model model) {
        this.playerController = playerController;
        this.model = model;
    }

    @Override
    public Parent render() {
        VBox root = new VBox();
        root.getStyleClass().add("base-screen");

        Label title = new Label("Dungeon Crawler");
        title.getStyleClass().add("game-title");

        Label highScoreLabel = new Label("High Score: " + model.getHighScore());
        highScoreLabel.getStyleClass().add("score-label");

        Label currentScoreLabel = new Label("Last Score: " + model.getCurScore());
        currentScoreLabel.getStyleClass().add("score-label");

        Button startButton = new Button("Start Game");
        startButton.getStyleClass().add("control-button");
        startButton.setOnAction(e -> playerController.startGame());

        Label byline = new Label("By Samyak Jain");
        byline.getStyleClass().add("byline");

        root.getChildren().addAll(title, highScoreLabel, currentScoreLabel, startButton, byline);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);

        return root;
    }
}