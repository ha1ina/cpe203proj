import processing.core.PImage;

import java.util.List;
import java.util.Optional;


public class OreBlob extends Entity{

    public static final String QUAKE_KEY = "quake";



    public OreBlob(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        resourceLimit = 0;
        resourceCount = 0;
    }

    public void execute(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler, Action action)
    {
        Optional<Entity> blobTarget = world.findNearest(world,
                this.getPosition(),Vein.class);
        long nextPeriod = this.getActionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Entity quake = Create.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake,world);
                nextPeriod += this.getActionPeriod();
                scheduleActions(scheduler, quake, imageStore, world);
            }
        }

        scheduler.scheduleEvent(this,
                ActionActivity.createActivityAction(world,this, imageStore),
                nextPeriod);
    }
    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) {

                scheduler.scheduleEvent(entity,
                        ActionActivity.createActivityAction(world,this, imageStore),
                        entity.getActionPeriod());
                scheduler.scheduleEvent(entity,
                        ActionAnimation.createAnimationAction(entity,0), entity.getAnimationPeriod());


    }
    public boolean moveToOreBlob(WorldModel world,
                                 Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.getPosition())) {
            world.removeEntity(world, target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = this.nextPositionOreBlob(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(target,world,nextPos);
            }
            return false;
        }
    }
    public Point nextPositionOreBlob(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get() instanceof  Ore))) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get() instanceof Ore))) {
                newPos = this.position;
            }
        }

        return newPos;
    }

}

