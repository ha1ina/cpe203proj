public class ActionActivity implements Action {
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActionActivity(Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventScheduler scheduler) {
        entity.execute(this.world, imageStore, scheduler, this);

    }
    public static ActionActivity createActivityAction(WorldModel world,Entity entity,ImageStore imageStore) {
        return new ActionActivity( entity, world, imageStore, 0);
    }
}
