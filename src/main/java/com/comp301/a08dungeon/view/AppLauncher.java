package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.controller.ControllerImpl;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.ModelImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppLauncher extends Application {
  @Override
  public void start(Stage stage) {
      int width = 1000; //pixels
      int height = 750; //pixels
      stage.setTitle("Samyak's Dungeon Crawler");

      Model model = new ModelImpl(10,10);
      Controller playerController = new ControllerImpl(model);
      View view = new View(playerController,model,stage);
      model.addObserver(view);

      Scene scene = view.getScene();
      stage.setWidth(width);
      stage.setHeight(height);

//      scene.setOnKeyPressed(event -> {
//          switch (event.getCode()) {
//              case UP -> playerController.moveUp();
//              case DOWN -> playerController.moveDown();
//              case LEFT -> playerController.moveLeft();
//              case RIGHT -> playerController.moveRight();
//          }
//      });

      stage.setScene(scene);
      stage.show();
  }
}
