import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Vein extends Entity {

    private final String ORE_ID_PREFIX = "ore -- ";
    private final int ORE_CORRUPT_MIN = 20000;
    private final int ORE_CORRUPT_MAX = 30000;
    private static final String ORE_KEY = "ore";


    public Vein(String id, Point position,
                List<PImage> images,
                int actionPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        resourceCount = 0;
        resourceLimit = 0;

        this.actionPeriod = actionPeriod;

    }

    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Action action) {
        Optional<Point> openPt = findOpenAround(this.getPosition(), world);

        if (openPt.isPresent()) {
            Entity ore = Create.createOre(ORE_ID_PREFIX + this.getId(),
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore, world);
            ore.scheduleActions(scheduler, ore, imageStore, world);
        }

        scheduler.scheduleEvent(this,
                ActionActivity.createActivityAction(world, this, imageStore),
                this.getActionPeriod());
    }

    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) {


        scheduler.scheduleEvent(entity,
                ActionActivity.createActivityAction(world, this, imageStore),
                entity.getActionPeriod());

    }
}



