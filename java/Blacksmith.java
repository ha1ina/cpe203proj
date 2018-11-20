import processing.core.PImage;
import java.util.List;
import java.util.Optional;


public class Blacksmith extends Entity{


    public Blacksmith(String id, Point position,
                  List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        resourceLimit = 0;
        resourceCount = 0;
    }

        public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Action action){
        }



    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) { }
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




}
