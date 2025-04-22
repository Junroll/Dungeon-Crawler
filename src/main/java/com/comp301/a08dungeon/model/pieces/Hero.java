package com.comp301.a08dungeon.model.pieces;

public class Hero extends APiece implements MovablePiece {
  public Hero() {
    super("Hero", "temp path here");
  }

  public CollisionResult collide(Piece other) {
    if (other != null) {
      if (other.getName().equals("Treasure")) {
        return new CollisionResult(((Treasure) other).getValue(), CollisionResult.Result.CONTINUE);
      } else if (other.getName().equals("Enemy")) {
        return new CollisionResult(0, CollisionResult.Result.GAME_OVER);
      } else if (other.getName().equals("Exit")) {
        return new CollisionResult(0, CollisionResult.Result.NEXT_LEVEL);
      } else {
        throw new IllegalArgumentException("Unknown Piece");
      }
    }
    return new CollisionResult(0, CollisionResult.Result.CONTINUE);
  }
}
