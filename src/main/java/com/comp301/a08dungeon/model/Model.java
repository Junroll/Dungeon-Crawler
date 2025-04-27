package com.comp301.a08dungeon.model;

import com.comp301.a08dungeon.model.board.Posn;
import com.comp301.a08dungeon.model.pieces.Piece;

public interface Model extends Subject {
  int getWidth();

  int getHeight();

  Piece get(Posn p);

  int getCurScore();

  int getHighScore();

  int getLevel();

  // Change status from IN_PROGRESS and END_GAME
  STATUS getStatus();

  boolean getHardMode();

  void setHardMode(boolean mode);

  boolean getSecondaryTheme();

  void setSecondaryTheme(boolean secondaryTheme);

  boolean getPortalAffectsEnemies();

  void setPortalAffectsEnemies(boolean mode);

  void startGame();

  void endGame();

  void moveUp();

  void moveDown();

  void moveLeft();

  void moveRight();

  enum STATUS {
    END_GAME,
    IN_PROGRESS
  }
}
