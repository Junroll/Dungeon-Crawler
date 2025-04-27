package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.controller.ControllerImpl;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.ModelImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AppLauncher extends Application {
  @Override
  public void start(Stage stage) {
      Font.loadFont(getClass().getResource("/fonts/AmaticSC.ttf").toExternalForm(),48);


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

      stage.setScene(scene);
      stage.show();
  }
}
