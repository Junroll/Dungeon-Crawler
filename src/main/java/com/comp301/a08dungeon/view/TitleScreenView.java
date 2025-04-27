package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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

        CheckBox hardModeCheckbox = new CheckBox("Enable Hard Mode");
        hardModeCheckbox.getStyleClass().add("checkbox");

        CheckBox secondaryThemeCheckbox = new CheckBox("Enable Among Us Theme");
        secondaryThemeCheckbox.getStyleClass().add("checkbox");

        Button startButton = new Button("Start Game");
        startButton.getStyleClass().add("control-button");
        startButton.setOnAction(e -> {
            if (hardModeCheckbox.isSelected()) {playerController.enableHardMode();}
            else {playerController.disableHardMode();}

            if (secondaryThemeCheckbox.isSelected()) {playerController.enableSecondaryTheme();}
            else {playerController.disableSecondaryTheme();}

            playerController.startGame();
        });

        Label byline = new Label("By Samyak Jain");
        byline.getStyleClass().add("byline");

        root.getChildren().addAll(title,highScoreLabel,currentScoreLabel,
                hardModeCheckbox,secondaryThemeCheckbox,startButton,byline);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(15);

        return root;
    }
}