import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends Entity{


    public MinerFull(String id, Point position,
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


    public void execute(WorldModel world,
                                                ImageStore imageStore, EventScheduler scheduler, Action action)
    {
        Optional<Entity> fullTarget = world.findNearest(world,this.position,
                Blacksmith.class);

        if (fullTarget.isPresent() && moveToFull(this, world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this, ActionActivity.createActivityAction(world,this, imageStore), this.actionPeriod);
        }
    }
    public void scheduleActions(EventScheduler scheduler, Entity entity, ImageStore imageStore, WorldModel world) {

        scheduler.scheduleEvent(this, ActionActivity.createActivityAction(world, this, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, ActionAnimation.createAnimationAction(this, 0), this.getAnimationPeriod());

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



    public void transformFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = Create.createMinerNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(world, this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner,world);
        ((MinerNotFull)miner).scheduleActions(scheduler, miner, imageStore,world);
    }

    public boolean moveToFull(Entity miner, WorldModel world,
                                     Entity target, EventScheduler scheduler)
    {
        if (miner.getPosition().adjacent(target.getPosition()))
        {
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
