import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Entity{

    public MinerNotFull(String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }




    public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler, Action action) {
        Optional<Entity> notFullTarget = world.findNearest(world, this.getPosition(),Ore.class);
        if (!notFullTarget.isPresent() || !moveToNotFull(this, world, notFullTarget.get(), scheduler) || !transformNotFull(this, world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    ActionActivity.createActivityAction(world,this, imageStore),
                    this.getActionPeriod());
        }
    }

    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) {

        scheduler.scheduleEvent(entity, ActionActivity.createActivityAction(world, this, imageStore), actionPeriod);
        scheduler.scheduleEvent(entity, ActionAnimation.createAnimationAction(entity, 0), entity.getAnimationPeriod());

    }

    public static Point nextPositionMiner(Entity entity, WorldModel world,
                                          Point destPos)
    {
        int horiz = Integer.signum(destPos.x - entity.getPosition().x);
        Point newPos = new Point(entity.getPosition().x + horiz,
                entity.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - entity.getPosition().y);
            newPos = new Point(entity.getPosition().x,
                    entity.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = entity.getPosition();
            }
        }

        return newPos;
    }



    public boolean transformNotFull(Entity entity, WorldModel world,
                                           EventScheduler scheduler, ImageStore imageStore) {
        if (entity.getResourceCount() >= entity.getResourceLimit())
        {
            Entity miner = Create.createMinerFull(entity.getId(), entity.getResourceLimit(),
                    entity.getPosition(), entity.getActionPeriod(), entity.getAnimationPeriod(),
                    entity.getImages());

            world.removeEntity(world, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner,world);
            ((MinerFull)miner).scheduleActions(scheduler, miner, imageStore,world);

            return true;
        }

        return false;
    }

    public boolean moveToNotFull(Entity miner, WorldModel world,
                                        Entity target, EventScheduler scheduler)
    {
        if (miner.getPosition().adjacent(target.getPosition()))
        {
            miner.setResourceCount( miner.getResourceCount() + 1);
            world.removeEntity(world, target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(miner, world, target.getPosition());

            if (!miner.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(miner, world, nextPos);
            }
            return false;
        }
    }




}
