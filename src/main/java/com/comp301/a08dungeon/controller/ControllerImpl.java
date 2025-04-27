package com.comp301.a08dungeon.controller;

import com.comp301.a08dungeon.model.Model;

public class ControllerImpl implements Controller {
    private final Model model;

    public ControllerImpl(Model model) {
        this.model = model;
    }

    @Override
    public void moveUp() {
        model.moveUp();
    }

    @Override
    public void moveDown() {
        model.moveDown();
    }

    @Override
    public void moveLeft() {
        model.moveLeft();
    }

    @Override
    public void moveRight() {
        model.moveRight();
    }

    @Override
    public void startGame() {
        model.startGame();
    }

    @Override
    public void enableHardMode() {
        model.setHardMode(true);
    }

    @Override
    public void disableHardMode() {
        model.setHardMode(false);
    }

    @Override
    public void enablePortalAffectsEnemies() {
        model.setPortalAffectsEnemies(true);
    }

    @Override
    public void disablePortalAffectsEnemies() {model.setPortalAffectsEnemies(false);}

    @Override
    public void enableSecondaryTheme() {model.setSecondaryTheme(true);}

    @Override
    public void disableSecondaryTheme() {model.setSecondaryTheme(false);}
}
