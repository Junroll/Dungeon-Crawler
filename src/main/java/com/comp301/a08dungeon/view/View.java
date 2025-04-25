package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.Observer;
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

  public View(Controller playerController, Model model, Stage stage) {
    this.playerController = playerController;
    this.model = model;
    this.stage = stage;
    this.scene = new Scene(render());
    this.scene.getStylesheets().add("dungeon.css");
  }

  public Parent render() {
    if (model.getStatus() == Model.STATUS.END_GAME) {
      return new TitleScreenView(playerController, model).render();
    } else if (model.getStatus() == Model.STATUS.IN_PROGRESS) {
      return new GameView(playerController, model).render();
    } else {
      Pane s = new StackPane();
      s.getChildren().add(new Label("Hello, World"));
      return s;
    }
  }

  @Override
  public void update() {
    scene.setRoot(render());
  }
  public Scene getScene() {
    return this.scene;
  }
}