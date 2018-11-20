import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Quake extends Entity {


    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;



    public Quake(String id, Point position,
                  List<PImage> images,
                  int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        resourceLimit = 0;
        resourceCount = 0;
    }


    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Action action)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(world,this);
    }


    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) {

                scheduler.scheduleEvent(entity,
                        ActionActivity.createActivityAction(world, this, imageStore),
                        entity.getActionPeriod());
                scheduler.scheduleEvent(entity,
                        ActionAnimation.createAnimationAction(entity,QUAKE_ANIMATION_REPEAT_COUNT),
                        entity.getAnimationPeriod());

    }



}

