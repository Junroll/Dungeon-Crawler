package com.comp301.a08dungeon.model.pieces;

public class Enemy extends APiece implements MovablePiece {

  public Enemy() {
    super("Enemy", "/images/Enemy.png");
  }

  public CollisionResult collide(Piece other) {
    if (other != null) {
      if (other.getName().equals("Treasure")) {
        other.setPosn(null);
        return new CollisionResult(0, CollisionResult.Result.CONTINUE);
      } else if (other.getName().equals("Hero")) {
        return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
      } else {
        throw new IllegalArgumentException("Unknown Piece");
      }
    }
    return new CollisionResult(0, CollisionResult.Result.CONTINUE);
  }
}
