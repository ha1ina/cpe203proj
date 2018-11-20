import processing.core.PImage;

import java.util.List;


public class Ore extends Entity{

    private String BLOB_ID_SUFFIX = " -- blob";
    private final int BLOB_PERIOD_SCALE = 4;
    private final int BLOB_ANIMATION_MIN = 50;
    private final int BLOB_ANIMATION_MAX = 150;


    private final String BLOB_KEY = "blob";





    public Ore(String id, Point position,int actionPeriod,
                  List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        resourceLimit = 0;
        resourceCount = 0;

        this.actionPeriod = actionPeriod;

    }

    public void execute(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler, Action action)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(world, this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Create.createOreBlob(this.getId() + BLOB_ID_SUFFIX,
                pos, this.getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob, world);
        scheduleActions(scheduler, blob, imageStore, world);
    }

    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) {

                scheduler.scheduleEvent(entity,
                        ActionActivity.createActivityAction(world,this, imageStore),
                        entity.getActionPeriod());

    }


}
