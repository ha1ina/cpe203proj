import processing.core.PImage;

import java.util.List;


public class Obstacle extends Entity{



    public Obstacle(String id, Point position,
                  List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        resourceLimit = 0;
        resourceCount = 0;
    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Action action){}


    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) {


    }

}
