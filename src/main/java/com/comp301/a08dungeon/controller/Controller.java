package com.comp301.a08dungeon.controller;

public interface Controller {
  public void moveUp();

  public void moveDown();

  public void moveLeft();

  public void moveRight();

  public void startGame();

  public void enableHardMode();

  public void disableHardMode();

  public void enablePortalAffectsEnemies();

  public void disablePortalAffectsEnemies();

  public void enableSecondaryTheme();

  public void disableSecondaryTheme();
}
