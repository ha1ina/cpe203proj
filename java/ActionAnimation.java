public class ActionAnimation implements Action {
    private Entity entity;
    private  WorldModel world;
    private  ImageStore imageStore;
    private  int repeatCount;

    public ActionAnimation(Entity entity, WorldModel world,
                          ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventScheduler scheduler){
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity, createAnimationAction(entity,
                    Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }
    public static ActionAnimation createAnimationAction(Entity entity, int repeatCount) {
        return new ActionAnimation(entity, null, null, repeatCount);
    }

}
