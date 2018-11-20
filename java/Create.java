import processing.core.PImage;

import java.util.List;

public class Create {
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final String QUAKE_ID = "quake";
    public static Blacksmith createSmith(String id, Point position, List<PImage> images) {
        return new Blacksmith(id, position, images);
    }
    public static MinerFull createMinerFull(String id, int resourceLimit,
                                  Point position, int actionPeriod, int animationPeriod,
                                  List<PImage> images) {
        return new MinerFull(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }
    public static MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new MinerNotFull(id, position, images,
                resourceLimit,0, actionPeriod, animationPeriod);
    }
    public static  Obstacle createObstacle(String id, Point position, List<PImage> images) {
        return new Obstacle(id, position, images);
    }
    public static  Ore createOre(String id, Point position,
                         int actionPeriod, List<PImage> images)
    {
        return new Ore(id, position, actionPeriod, images);
    }
    public static  OreBlob createOreBlob(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        return new OreBlob(id, position, images, actionPeriod, animationPeriod);
    }
    public static  Entity createQuake(Point position, List<PImage> images) {
        return new Quake(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
    public static  Vein createVein(String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Vein(id, position, images, actionPeriod);
    }
}
