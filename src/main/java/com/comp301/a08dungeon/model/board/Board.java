package com.comp301.a08dungeon.model.board;

import com.comp301.a08dungeon.model.pieces.CollisionResult;
import com.comp301.a08dungeon.model.pieces.Piece;

/** Defines the public operations on the game board. */
public interface Board {

  void init(int enemies, int treasures, int walls, int portals);

  int getWidth();

  int getHeight();

  Piece get(Posn posn);

  void set(Piece p, Posn newPos);

  void setHardMode(boolean mode);

  void setPortalAffectsEnemies(boolean mode);

  CollisionResult moveHero(int drow, int dcol);
}
