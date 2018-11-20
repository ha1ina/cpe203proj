import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

final class WorldModel {
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;



   public WorldModel(int numRows, int numCols, Background defaultBackground) {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++) {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }
   public int getNumRows(){
      return numRows;
   }
   public int getNumCols(){
      return numCols;
   }
   public Set<Entity> getEntities(){
      return entities;
   }
   public void tryAddEntity(Entity entity, WorldModel world) {
      if (world.isOccupied(entity.getPosition())) {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }
      addEntity(entity,world);
   }
   public boolean withinBounds(Point pos) {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }
   public boolean isOccupied(Point pos) {
      return this.withinBounds(pos) &&
              this.getOccupancyCell(pos) != null;
   }
   public Optional<Entity> findNearest(WorldModel world, Point pos,
                                       Class c) {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : world.entities) {
         if (entity.getClass() == c) {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }
   public void addEntity(Entity entity, WorldModel world) {
      if (world.withinBounds(entity.getPosition())) {
         world.setOccupancyCell(entity.getPosition(),entity);
         world.entities.add(entity);
      }
   }
   public void moveEntity(Entity entity, WorldModel world, Point pos) {
      Point oldPos = entity.getPosition();
      if (world.withinBounds(pos) && !pos.equals(oldPos)) {
         world.setOccupancyCell(oldPos, null);
         removeEntityAt(world, pos);
         world.setOccupancyCell(pos, entity);
         entity.setPos(pos);
      }
   }
   public void removeEntity(WorldModel world,Entity entity
   ) {
      removeEntityAt(world, entity.getPosition());
   }

   public void removeEntityAt(WorldModel world, Point pos) {
      if (world.withinBounds(pos)
              && world.getOccupancyCell(pos) != null) {
         Entity entity = world.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPos(new Point(-1, -1));
         world.entities.remove(entity);
         world.setOccupancyCell(pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public void setBackground(Point pos, Background background) {
      if (withinBounds(pos)) {
         setBackgroundCell(pos, background);
      }
   }
   public Optional<Entity> getOccupant(Point pos) {
      if (this.isOccupied(pos)) {
         return Optional.of(this.getOccupancyCell(pos));
      } else {
         return Optional.empty();
      }
   }

   public Entity getOccupancyCell(Point pos) {
      return this.occupancy[pos.y][pos.x];
   }

   public void setOccupancyCell(Point pos,
                                Entity entity) {
      this.occupancy[pos.y][pos.x] = entity;
   }
   public Background getBackgroundCell(Point pos) {
      return this.background[pos.y][pos.x];
   }


   public void setBackgroundCell(Point pos,
                                 Background background) {
      this.background[pos.y][pos.x] = background;
   }
   public static Optional<Entity> nearestEntity(List<Entity> entities,
                                         Point pos) {
      if (entities.isEmpty()) {
         return Optional.empty();
      } else {
         Entity nearest = entities.get(0);
         int nearestDistance = distanceSquared(nearest.getPosition(),pos);

         for (Entity other : entities) {
            int otherDistance = distanceSquared(other.getPosition(),pos);

            if (otherDistance < nearestDistance) {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }
   public static int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p2.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }
   public void loadWorld(String filename, ImageStore imageStore) {
      try {
         Scanner in = new Scanner(new File(filename));
         load(in,imageStore);
      } catch (FileNotFoundException e) {
         System.err.println(e.getMessage());
      }
   }
   public void load(Scanner in, ImageStore imageStore) {
      int lineNumber = 0;
      while (in.hasNextLine()) {
         try {
            if (!Functions.processLine(in.nextLine(), this, imageStore)) {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         } catch (NumberFormatException e) {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         } catch (IllegalArgumentException e) {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

}






























