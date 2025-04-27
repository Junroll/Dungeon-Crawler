package com.comp301.a08dungeon.model;

import com.comp301.a08dungeon.model.board.Board;
import com.comp301.a08dungeon.model.board.BoardImpl;
import com.comp301.a08dungeon.model.board.Posn;
import com.comp301.a08dungeon.model.pieces.CollisionResult;
import com.comp301.a08dungeon.model.pieces.Piece;
import java.util.ArrayList;

public class ModelImpl implements Model {
  private final Board gameBoard;
  private final ArrayList<Observer> observers = new ArrayList<>();
  private STATUS status;
  private int currentScore = 0;
  private int highScore = 0;
  private int currentLevel = 0;
  private CollisionResult moveOutcome;
  private CollisionResult.Result gameStatus;
  private boolean hardMode = false;
  private boolean secondaryTheme = false;
  private boolean portalAffectsEnemies = false;

  public ModelImpl(int width, int height) {
    this.gameBoard = new BoardImpl(width, height);
    this.status = STATUS.END_GAME;
  }

  public ModelImpl(Board board) {
    this.gameBoard = board;
    this.status = STATUS.END_GAME;
  }

  @Override
  public int getWidth() {
    return gameBoard.getWidth();
  }

  @Override
  public int getHeight() {
    return gameBoard.getHeight();
  }

  @Override
  public Piece get(Posn p) {
    return gameBoard.get(p);
  }

  @Override
  public int getCurScore() {
    return this.currentScore;
  }

  @Override
  public int getHighScore() {
    return this.highScore;
  }

  @Override
  public int getLevel() {
    return this.currentLevel;
  }

  @Override
  public STATUS getStatus() {
    return this.status;
  }

  @Override
  public boolean getHardMode() {
    return this.hardMode;
  }

  @Override
  public void setHardMode(boolean mode) {
    this.hardMode = mode;
    gameBoard.setHardMode(mode);
  }

  @Override
  public boolean getSecondaryTheme() {
    return this.secondaryTheme;
  }

  @Override
  public void setSecondaryTheme(boolean secondaryTheme) {
    this.secondaryTheme = secondaryTheme;
    notifyObservers();
  }

  @Override
  public boolean getPortalAffectsEnemies() {
    return this.portalAffectsEnemies;
  }

  @Override
  public void setPortalAffectsEnemies(boolean mode) {
    this.portalAffectsEnemies = mode;
    gameBoard.setPortalAffectsEnemies(mode);
    notifyObservers();
  }

  @Override
  public void startGame() {
    // standard initialization and resetting
    this.status = STATUS.IN_PROGRESS;
    this.currentScore = 0;
    this.currentLevel = 1;

    try {
      gameBoard.init(currentLevel + 1, 2, 2,1);
    } catch (IllegalArgumentException e) {
      endGame();
    }
    notifyObservers();
  }

  @Override
  public void endGame() {
    this.status = STATUS.END_GAME;
    if (currentScore > highScore) {
      this.highScore = currentScore;
    }
    this.secondaryTheme = false;
    this.portalAffectsEnemies = false;
    gameBoard.setPortalAffectsEnemies(false);
    notifyObservers();
  }

  @Override
  public void moveUp() {
    this.moveOutcome = gameBoard.moveHero(-1, 0);
    this.gameStatus = moveOutcome.getResults();
    this.currentScore += moveOutcome.getPoints();

    if (gameStatus.equals(CollisionResult.Result.GAME_OVER)) {
      endGame();
    } else if (gameStatus.equals(CollisionResult.Result.NEXT_LEVEL)) {
      this.currentLevel += 1;
      try {
        gameBoard.init(currentLevel + 1, 2, 2,1);
      } catch (IllegalArgumentException e) {
        endGame();
      }
    }
    notifyObservers();
  }

  @Override
  public void moveDown() {
    this.moveOutcome = gameBoard.moveHero(1, 0);
    this.gameStatus = moveOutcome.getResults();
    this.currentScore += moveOutcome.getPoints();

    if (gameStatus.equals(CollisionResult.Result.GAME_OVER)) {
      endGame();
    } else if (gameStatus.equals(CollisionResult.Result.NEXT_LEVEL)) {
      this.currentLevel += 1;
      try {
        gameBoard.init(currentLevel + 1, 2, 2,1);
      } catch (IllegalArgumentException e) {
        endGame();
      }
    }
    notifyObservers();
  }

  @Override
  public void moveLeft() {
    this.moveOutcome = gameBoard.moveHero(0, -1);
    this.gameStatus = moveOutcome.getResults();
    this.currentScore += moveOutcome.getPoints();

    if (gameStatus.equals(CollisionResult.Result.GAME_OVER)) {
      endGame();
    } else if (gameStatus.equals(CollisionResult.Result.NEXT_LEVEL)) {
      this.currentLevel += 1;
      try {
        gameBoard.init(currentLevel + 1, 2, 2,1);
      } catch (IllegalArgumentException e) {
        endGame();
      }
    }
    notifyObservers();
  }

  @Override
  public void moveRight() {
    this.moveOutcome = gameBoard.moveHero(0, 1);
    this.gameStatus = moveOutcome.getResults();
    this.currentScore += moveOutcome.getPoints();

    if (gameStatus.equals(CollisionResult.Result.GAME_OVER)) {
      endGame();
    } else if (gameStatus.equals(CollisionResult.Result.NEXT_LEVEL)) {
      this.currentLevel += 1;
      try {
        gameBoard.init(currentLevel + 1, 2, 2,1);
      } catch (IllegalArgumentException e) {
        endGame();
      }
    }
    notifyObservers();
  }

  @Override
  public void addObserver(Observer o) {
    observers.add(o);
  }

  private void notifyObservers() {
    for (Observer o : observers) {
      o.update();
    }
  }
}
