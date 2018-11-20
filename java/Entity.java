import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

abstract public class Entity {
   protected String id;
   protected Point position;
   protected List<PImage> images;
   protected int imageIndex;
   protected  int resourceLimit;
   protected  int resourceCount;
   protected  int actionPeriod;
   protected int animationPeriod;
   protected final Random rand = new Random();
   protected static final int ORE_REACH = 1;


   public PImage getCurrentImage()
   {
      return images.get(imageIndex);

   }
   public Point getPosition(){
      return position;
   }

   public int getAnimationPeriod() {

      return this.animationPeriod;

   }
   public void nextImage() {

      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   abstract public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Action action);

   abstract public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world);


   public Optional<Point> findOpenAround(Point pos, WorldModel world) {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (world.withinBounds(newPt) &&
                    !world.isOccupied(newPt)) {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public int getActionPeriod(){
      return actionPeriod;
   }
   public String getId(){
      return id;
   }
   public void setPos(Point pos){
      position = pos;
   }
   public List<PImage> getImages(){
      return images;
   }
   public int getImageIndex(){
      return imageIndex;
   }
   public int getResourceLimit(){
      return resourceLimit;
   }
   public int getResourceCount(){
      return resourceCount;
   }
   public void setResourceLimit(int l){
      resourceLimit = l;
   }
   public void setResourceCount(int c){
      resourceCount = c;
   }
}











