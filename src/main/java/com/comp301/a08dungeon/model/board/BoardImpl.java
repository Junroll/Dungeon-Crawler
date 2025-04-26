package com.comp301.a08dungeon.model.board;

import com.comp301.a08dungeon.model.pieces.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BoardImpl implements Board {
  private final Piece[][] board;
  private final ArrayList<Piece> enemiesList;
  private final ArrayList<CollisionResult> collisionResults = new ArrayList<>();
  private final int totalSpots;
  private final Random random = new Random();
  private Hero hero;
  private Exit exit;
  private boolean hardMode = false;

  public BoardImpl(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Board dimensions are invalid");
    }

    this.board = new Piece[height][width];
    this.enemiesList = new ArrayList<>();
    this.totalSpots = width * height;
  }

  public BoardImpl(Piece[][] board) {
    if (board == null) {
      throw new IllegalArgumentException("Invalid Board");
    }

    this.board = board;
    this.enemiesList = new ArrayList<>();
    this.totalSpots = board.length * board[0].length;

    populateBoardFields();
  }

  @Override
  public void init(int enemies, int treasures, int walls) {
    // Clearing the entire board.
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        board[i][j] = null;
      }
    }
    this.enemiesList.clear();

    // Check required num spots
    int requiredSpots = enemies + treasures + walls + 1 + 1;
    if (requiredSpots > this.totalSpots) {
      throw new IllegalArgumentException("Not enough spots on the board");
    }

    // Initialize pieces
    initializePieces(enemies, treasures, walls);
  }

  @Override
  public int getWidth() {
    return this.board[0].length;
  }

  @Override
  public int getHeight() {
    return this.board.length;
  }

  @Override
  public Piece get(Posn posn) {
    return this.board[posn.getRow()][posn.getCol()];
  }

  @Override
  public void set(Piece p, Posn newPos) {
    this.board[newPos.getRow()][newPos.getCol()] = p;
  }

  @Override
  public void setHardMode(boolean mode) {
    this.hardMode = mode;
  }

  @Override
  public CollisionResult moveHero(int drow, int dcol) {
    collisionResults.clear(); // Clear previous results each move

    // 1) Remember old position
    Posn heroOldPosition = hero.getPosn();

    // 2) Compute the candidate new position
    Posn heroNewPosition =
        new Posn(heroOldPosition.getRow() + drow, heroOldPosition.getCol() + dcol);

    // 3) Invalid move? off‐board or into a Wall then do nothing
    if (heroNewPosition.getRow() < 0
        || heroNewPosition.getRow() >= board.length
        || heroNewPosition.getCol() < 0
        || heroNewPosition.getCol() >= board[0].length
        || get(heroNewPosition) instanceof Wall) {
      return new CollisionResult(0, CollisionResult.Result.CONTINUE);
    }

    // 4) Capture whatever was at the new spot (could be Treasure, Exit, Enemy, or null)
    Piece destinationPiece = get(heroNewPosition);

    // 5) Move the hero on the board
    set(null, heroOldPosition);
    hero.setPosn(heroNewPosition);
    set(hero, heroNewPosition);

    // 6) Handle the collision
    CollisionResult heroCollisionResult = hero.collide(destinationPiece);
    collisionResults.add(heroCollisionResult);

    if (heroCollisionResult.getResults() == CollisionResult.Result.GAME_OVER) {
      return heroCollisionResult;
    }
    if (heroCollisionResult.getResults() == CollisionResult.Result.NEXT_LEVEL) {
      // hero reached exit ⇒ leave hero on cell until level reload
      return heroCollisionResult;
    }

    // 7) On CONTINUE (treasure or empty), let enemies move and then aggregate results
    moveEnemies();
    int totalPoints = 0;
    for (CollisionResult outcome : collisionResults) {
      switch (outcome.getResults()) {
        case GAME_OVER:
          return new CollisionResult(totalPoints, CollisionResult.Result.GAME_OVER);
        case NEXT_LEVEL:
          return new CollisionResult(totalPoints, CollisionResult.Result.NEXT_LEVEL);
        default: // CONTINUE
          totalPoints += outcome.getPoints();
      }
    }
    return new CollisionResult(totalPoints, CollisionResult.Result.CONTINUE);
  }

  // Helper method for random co-ord generation
  public Posn getValidRandomPosn() {
    int Row = random.nextInt(board.length);
    int Col = random.nextInt(board[0].length);

    if (board[Row][Col] != null) {
      return getValidRandomPosn();
    }

    return new Posn(Row, Col);
  }

  // Helper method to initialize all the pieces
  public void initializePieces(int enemies, int treasures, int walls) {
    // Creating Enemies
    for (int i = 0; i < enemies; i++) {
      Enemy enemy = new Enemy();
      Posn newPosn = getValidRandomPosn();
      enemy.setPosn(newPosn);
      enemiesList.add(enemy);

      set(enemy, newPosn);
    }

    // Creating Treasures
    for (int i = 0; i < treasures; i++) {
      Piece treasure = new Treasure();
      Posn newPosn = getValidRandomPosn();
      treasure.setPosn(newPosn);

      set(treasure, newPosn);
    }

    // Creating Walls
    for (int i = 0; i < walls; i++) {
      Piece wall = new Wall();
      Posn newPosn = getValidRandomPosn();
      wall.setPosn(newPosn);

      set(wall, newPosn);
    }

    // Creating Hero
    hero = new Hero();
    Posn heroPosn = getValidRandomPosn();
    hero.setPosn(heroPosn);

    set(hero, heroPosn);

    // Creating Exit
    exit = new Exit();
    Posn exitPosn = getValidRandomPosn();
    exit.setPosn(exitPosn);

    set(exit, exitPosn);
  }

  private Posn getNewPositionForEnemy(Posn cur, Direction direction) {
    switch (direction) {
      case Direction.UP -> {
        return new Posn(cur.getRow() - 1, cur.getCol());
      }
      case Direction.DOWN -> {
        return new Posn(cur.getRow() + 1, cur.getCol());
      }
      case Direction.LEFT -> {
        return new Posn(cur.getRow(), cur.getCol() - 1);
      }
      case Direction.RIGHT -> {
        return new Posn(cur.getRow(), cur.getCol() + 1);
      }
      default -> {
        return cur;
      }
    }
  }

  //Helper to appropriately move enemies to their new positions based on difficulty
  private void moveEnemies() {
    if (hardMode) {
      moveEnemiesTowardHero();
    }
    else {
      moveEnemiesRandomly();
    }
  }

  // Helper to move the enemies randomly to their new positions
  private void moveEnemiesRandomly() {
    for (Piece p : enemiesList) {
      Enemy enemy = (Enemy) p;
      Posn cur = enemy.getPosn();
      Piece collided = null;
      boolean moved = false;
      ArrayList<Direction> choices =
          new ArrayList<>(List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
      Collections.shuffle(choices, random);

      for (Direction dir : choices) {
        Posn np = getNewPositionForEnemy(cur, dir);
        // bounds + instanceof block checks in one shot:
        if (np.getRow() < 0
            || np.getRow() >= board.length
            || np.getCol() < 0
            || np.getCol() >= board[0].length) {
          continue;
        }
        Piece destinationPiece = get(np);
        if (destinationPiece instanceof Wall
            || destinationPiece instanceof Exit
            || destinationPiece instanceof Enemy) {
          continue;
        }
        // capture what was there for collision
        collided = destinationPiece;
        // clear old, move, set new
        set(null, cur);
        enemy.setPosn(np);
        set(enemy, np);
        moved = true;
        break;
      }

      if (!moved) {
        // trapped or all invalid ⇒ skip
        collisionResults.add(new CollisionResult(0, CollisionResult.Result.CONTINUE));
        continue;
      }

      // Now do collide against the piece that *used* to sit there
      if (collided == null) {
        collisionResults.add(new CollisionResult(0, CollisionResult.Result.CONTINUE));
      } else {
        CollisionResult enemyCollisionResult = enemy.collide(collided);
        collisionResults.add(enemyCollisionResult);
        if (enemyCollisionResult.getResults() == CollisionResult.Result.GAME_OVER) {
          break;
        }
      }
    }
  }

  private void moveEnemiesTowardHero() {
    for (Piece p : enemiesList) {
      Enemy enemy = (Enemy) p;
      Posn cur = enemy.getPosn();
      Piece collided = null;
      boolean moved = false;

      ArrayList<Direction> choices = new ArrayList<>();

      Posn heroPos = hero.getPosn();

      // Prefer vertical moves first if needed
      if (heroPos.getRow() < cur.getRow()) {
        choices.add(Direction.UP);
      } else if (heroPos.getRow() > cur.getRow()) {
        choices.add(Direction.DOWN);
      }

      // Then horizontal moves if needed
      if (heroPos.getCol() < cur.getCol()) {
        choices.add(Direction.LEFT);
      } else if (heroPos.getCol() > cur.getCol()) {
        choices.add(Direction.RIGHT);
      }

      // Shuffle the choices to add some unpredictability when both options are available
      Collections.shuffle(choices, random);

      for (Direction dir : choices) {
        Posn np = getNewPositionForEnemy(cur, dir);

        // bounds check
        if (np.getRow() < 0 || np.getRow() >= board.length ||
                np.getCol() < 0 || np.getCol() >= board[0].length) {
          continue;
        }

        Piece destinationPiece = get(np);

        // can't move into Wall, Exit, Enemy
        if (destinationPiece instanceof Wall ||
                destinationPiece instanceof Exit ||
                destinationPiece instanceof Enemy) {
          continue;
        }

        // Move!
        collided = destinationPiece;
        set(null, cur);
        enemy.setPosn(np);
        set(enemy, np);
        moved = true;
        break;
      }

      if (!moved) {
        // trapped, couldn't move
        collisionResults.add(new CollisionResult(0, CollisionResult.Result.CONTINUE));
        continue;
      }

      // Now handle collision result
      if (collided == null) {
        collisionResults.add(new CollisionResult(0, CollisionResult.Result.CONTINUE));
      } else {
        CollisionResult enemyCollisionResult = enemy.collide(collided);
        collisionResults.add(enemyCollisionResult);
        if (enemyCollisionResult.getResults() == CollisionResult.Result.GAME_OVER) {
          break;
        }
      }
    }
  }


  private void populateBoardFields() {
    for (Piece[] pieces : board) {
      for (int j = 0; j < board[0].length; j++) {
        Piece piece = pieces[j];
        if (piece != null) {
          switch (piece) {
            case Hero hero1 -> this.hero = hero1;
            case Exit exit1 -> this.exit = exit1;
            case Enemy enemy -> this.enemiesList.add(enemy);
            default -> {}
          }
        }
      }
    }
  }

  @Override
  public String toString() {
    return board.length + "rows, " + board[0].length + "columns";
  }

  private enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    CONSTANT
  }
}
