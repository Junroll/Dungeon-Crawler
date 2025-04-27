package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.Observer;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class View implements FXComponent, Observer {
  private final Controller playerController;
  private final Model model;
  private final Stage stage;
  private final Scene scene;
  private String currentStylesheet;

  public View(Controller playerController, Model model, Stage stage) {
    this.playerController = playerController;
    this.model = model;
    this.stage = stage;
    this.scene = new Scene(render());
    this.scene.getStylesheets().add("dungeon.css");

    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case UP, W -> playerController.moveUp();
        case DOWN, S -> playerController.moveDown();
        case LEFT, A -> playerController.moveLeft();
        case RIGHT, D -> playerController.moveRight();
      }
    });
  }

  public Parent render() {
    if (model.getStatus() == Model.STATUS.END_GAME) {
      return new TitleScreenView(playerController, model).render();
    } else if (model.getStatus() == Model.STATUS.IN_PROGRESS) {
      return new GameView(playerController, model).render();
    } else {
      Pane s = new StackPane();
      s.getChildren().add(new Label("An error occured."));
      return s;
    }
  }

  @Override
  public void update() {
    scene.setRoot(render());

    String desiredStylesheet = model.getSecondaryTheme() ? "amongus.css" : "dungeon.css";

    scene.getStylesheets().clear();
    scene.getStylesheets().add(desiredStylesheet);
    currentStylesheet = desiredStylesheet;

    Platform.runLater(() -> {scene.getRoot().requestFocus();});
  }

  public Scene getScene() {
    return this.scene;
  }
}