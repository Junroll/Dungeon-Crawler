package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.board.Posn;
import com.comp301.a08dungeon.model.pieces.Piece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class GameView implements FXComponent {
    private final Controller playerController;
    private final Model model;

    public GameView(Controller playerController, Model model) {
        this.playerController = playerController;
        this.model = model;
    }

    @Override
    public Parent render() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("base-screen");

        //Make Game title and put it at the top
        Label title = new Label("Samyak's Dungeon Crawler");
        title.getStyleClass().add("game-title");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        //Make Grid and put it in the center.
        GridPane board = new GridPane();
        board.getStyleClass().add("game-board");
        board.setPadding(new Insets(0));
        board.setHgap(0);
        board.setVgap(0);
        root.setCenter(board);

        //Adjust column and row constraints so board fills space
        double tileSize = 100; // Pick a default you like for now, can be dynamic later

        for (int i = 0; i < model.getWidth(); i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / model.getWidth());
            colConst.setMinWidth(20);
            colConst.setPrefWidth(Region.USE_COMPUTED_SIZE);
            board.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < model.getHeight(); i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / model.getHeight());
            rowConst.setMinHeight(20);
            rowConst.setPrefHeight(Region.USE_COMPUTED_SIZE);
            board.getRowConstraints().add(rowConst);
        }

        //Make right VBox to hold scores,rules, toggles, and controls HAVEN'T ADDED TO ROOT
        VBox rightPanel = new VBox();
        rightPanel.setSpacing(20);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setAlignment(Pos.TOP_CENTER);

        //Make Labels for Scores, Difficulty level, and Portal rule, and Portal Explanation
        Label highScore = new Label("High Score: " + model.getHighScore());
        Label score = new Label("Current Score: " + model.getCurScore());

        Label difficulty;
        if (model.getHardMode()) {difficulty = new Label("Difficulty: Hard");}
        else {difficulty = new Label("Difficulty: Easy");}


        Label portalExplanation = new Label(
                "Portals randomly teleport the Hero to an empty tile. Watch your step!");
        portalExplanation.setWrapText(true);
        portalExplanation.setStyle("-fx-font-size: 18px; -fx-text-alignment: center;");
        portalExplanation.setMaxWidth(250);

        Label portalRule;
        if (model.getPortalAffectsEnemies()) {portalRule = new Label("Enemies can use Portals");}
        else {portalRule = new Label("Enemies cannot use Portals");}

        //Make button toggle for portal rules
        Button togglePortalUseButton = new Button("Toggle Portal Rules");
        togglePortalUseButton.getStyleClass().add("control-button");
        togglePortalUseButton.setOnAction(e -> {
            if (model.getPortalAffectsEnemies()) {playerController.disablePortalAffectsEnemies();}
            else {playerController.enablePortalAffectsEnemies();}
        });

        //Make button toggle for changing theme
        Button toggleThemeButton = new Button("Toggle Theme");
        toggleThemeButton.getStyleClass().add("control-button");
        toggleThemeButton.setOnAction(e -> {
            if (model.getSecondaryTheme()) {playerController.disableSecondaryTheme();}
            else {playerController.enableSecondaryTheme();}
        });

        //Add styling to the labels
        highScore.getStyleClass().add("score-label");
        score.getStyleClass().add("score-label");
        difficulty.getStyleClass().add("score-label");
        portalExplanation.getStyleClass().add("score-label");
        portalRule.getStyleClass().add("score-label");

        //VBox to put the scores one on top the other + difficulty level
        VBox infoBox = new VBox(highScore,score,difficulty,portalExplanation,
                portalRule,togglePortalUseButton,toggleThemeButton);
        infoBox.setSpacing(15);
        infoBox.setAlignment(Pos.CENTER);

        //Make GridPane for Control Buttons
        GridPane controls = new GridPane();
        controls.setHgap(10);
        controls.setVgap(10);
        controls.setAlignment(Pos.CENTER);

        //Make the buttons to add to the GridPane
        Button up = new Button("↑");
        Button down = new Button("↓");
        Button left = new Button("←");
        Button right = new Button("→");

        //Set min button size
        up.setMinSize(50,50);
        down.setMinSize(50,50);
        left.setMinSize(50,50);
        right.setMinSize(50,50);

        //Apply Styling to the buttons
        up.getStyleClass().add("control-button");
        down.getStyleClass().add("control-button");
        left.getStyleClass().add("control-button");
        right.getStyleClass().add("control-button");

        //Add functionality to the buttons
        up.setOnAction(e -> playerController.moveUp());
        down.setOnAction(e -> playerController.moveDown());
        left.setOnAction(e -> playerController.moveLeft());
        right.setOnAction(e -> playerController.moveRight());

        //Add the buttons to the GridPane. REMEMBERING GridPane IS [COL][ROW]
        controls.add(up,1,0);
        controls.add(left,0,1);
        controls.add(down,1,1);
        controls.add(right,2,1);

        controls.setAlignment(Pos.BOTTOM_CENTER);

        //Add everything to right panel
        rightPanel.getChildren().addAll(infoBox, new Region() /*Spacer*/,controls);
        VBox.setVgrow(rightPanel.getChildren().get(1),Priority.ALWAYS); //Setting Spacer to get vertical grow priority
        //Add rightPanel to the root's right section
        root.setRight(rightPanel);

        //Setting up the board
        for (int row = 0; row < model.getHeight(); row++) {
            for (int col = 0; col < model.getWidth(); col++) {
                Posn pos = new Posn(row, col);
                Piece piece = model.get(pos);

                StackPane cell;
                if (piece != null) {
                    cell = createTile(piece,board);
                }
                else {
                    cell = new StackPane();
                }
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);
                cell.getStyleClass().add("board-tile");
                board.add(cell,col,row);
            }
        }
        return root;
    }

    private StackPane createTile(Piece piece,GridPane board) {
        String path = piece.getResourcePath();

        if (model.getSecondaryTheme()) {
            int dotIndex = path.lastIndexOf('.');
            path = path.substring(0, dotIndex) + "1" + path.substring(dotIndex);
        } //Handling for the Secondary Theme

        Image image  = new Image(path);
        ImageView pieceImage = new ImageView(image);
        StackPane cell = new StackPane();

        pieceImage.fitWidthProperty().bind(board.widthProperty().divide(model.getWidth()));
        pieceImage.fitHeightProperty().bind(board.heightProperty().divide(model.getHeight()));
        pieceImage.setPreserveRatio(true);
        pieceImage.setSmooth(true);

        cell.getChildren().add(pieceImage);
        StackPane.setAlignment(pieceImage, Pos.CENTER); // just in case

        return cell;
    }
}
