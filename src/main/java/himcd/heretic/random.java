package himcd.heretic;

import org.bukkit.*;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Random;

public class random {
    public static void generateTerrain(World world, Location center) {
        Random random = new Random();
        // 清除原来的地形
        int radius = 128;
        int startY =center.getBlockY()-30;
        for (int x = center.getBlockX() - radius; x <= center.getBlockX() + radius; x++) {
            for (int z = center.getBlockZ() - radius; z <= center.getBlockZ() + radius; z++) {
                for (int y = -30; y <= center.getBlockY() + 50; y++) { // 确保清除所有高度的方块
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
        center.getNearbyLivingEntities(128,livingEntity -> livingEntity.getScoreboardTags().contains("animal")).forEach(Entity::remove);
        // 使用 Simplex 噪声生成平滑的地形
        SimplexNoise simplexNoise = new SimplexNoise(random);
        double noiseScale = 0.0055555555555555555555555555555; // 噪声缩放因子，影响地形的起伏程度
        // 遍历生成地形
        for (int x = center.getBlockX() - radius; x <= center.getBlockX() + radius; x++) {
            for (int z = center.getBlockZ() - radius; z <= center.getBlockZ() + radius; z++) {
                double noiseValue = simplexNoise.noise(x * noiseScale, z * noiseScale); // 获取噪声值
                // 将噪声值转换为高度值
                int height = center.getBlockY() + (int) (noiseValue * 30); // 调整高度范围
                // 生成地形
                for (int y = startY; y <= height; y++) {
                    if (y <= startY+10) {
                        world.getBlockAt(x, y, z).setType(Material.STONE);

                    }else {
                        world.getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    if (height<=-10){
                        int i = random.nextInt(1, 4);
                        if (y==height){
                            if(i==2){
                                world.getBlockAt(x, y+1, z).setType(Material.GRAVEL);
                            }
                            if (i==3){
                                world.getBlockAt(x, y+1, z).setType(Material.SAND);
                            }
                        }
                    }
                    if (height>-10){
                        if (y==height){
                            world.getBlockAt(x, y+1, z).setType(Material.GRASS_BLOCK);
                            if (random.nextDouble() < 0.005&& y < world.getMaxHeight() - 1) {
                                placelandanimal(world, x, y+2, z, random);
                            }}
                        if (random.nextDouble() < 0.0003&& height < world.getMaxHeight() - 1) {
                            placeTree(world, x, height + 1, z, random);
                        }
                    }
                }
            }
        }
        for (int x = center.getBlockX() - radius; x <= center.getBlockX() + radius; x++) {
            for (int z = center.getBlockZ() - radius; z <= center.getBlockZ() + radius; z++) {
                for (int y =startY; y <= startY+22; y++) {
                    if (world.getBlockAt(x,y,z).getType().isAir()){
                        world.getBlockAt(x, y, z).setType(Material.WATER);
                        if (random.nextDouble() < 0.0003) {
                            placeseaanimal(world, x, y+1, z, random);
                        }
                    }
                    if (x!=0){
                        if (x%128==0){
                            world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                        }
                    }
                    if (z!=0){
                        if (z%128==0){
                            world.getBlockAt(x, y, z).setType(Material.BEDROCK);
                        }
                    }
                }
            }
        }
    }
    private static void placeTree(World world, int x, int y, int z, Random random) {
        TreeType[] treeTypes = {TreeType.BIRCH, TreeType.REDWOOD, TreeType.TALL_BIRCH, TreeType.JUNGLE, TreeType.ACACIA, TreeType.DARK_OAK, TreeType.SMALL_JUNGLE};
        TreeType treeType = treeTypes[random.nextInt(treeTypes.length)];
        int height = 4 + random.nextInt(8 - 4 + 1);
        world.generateTree(world.getBlockAt(x, y, z).getLocation(), treeType);
    }
    private static void placelandanimal(World world,int x,int y,int z,Random random){
        EntityType[] entityTypes = {EntityType.SHEEP,EntityType.COW,EntityType.CHICKEN};
        EntityType entityType = entityTypes[random.nextInt(entityTypes.length)];
        for (int c=0;c<2;c++){
            Entity entity = world.spawnEntity(new Location(world,x,y,z),entityType);
            entity.setFallDistance(0);
            entity.addScoreboardTag("animal");
        }
    }
    private static void placeseaanimal(World world,int x,int y,int z,Random random){
        EntityType[] entityTypes = {EntityType.COD,EntityType.TROPICAL_FISH,EntityType.SALMON,EntityType.PUFFERFISH};
        EntityType entityType = entityTypes[random.nextInt(entityTypes.length)];
        for (int c=0;c<5;c++){
            Entity entity = world.spawnEntity(new Location(world,x,y,z),entityType);
            entity.setFallDistance(0);
            entity.addScoreboardTag("animal");
        }
    }

    // Simplex 噪声类
    private static class SimplexNoise {
        private final int[] perm;
        private final double SQRT_3 = Math.sqrt(3.0);
        private final double F2 = 0.5 * (SQRT_3 - 1.0);
        private final double G2 = (3.0 - SQRT_3) / 6.0;
        private final double F3 = 1.0 / 3.0;
        private final double G3 = 1.0 / 6.0;

        public SimplexNoise(Random random) {
            perm = new int[512];
            int[] p = new int[256];
            for (int i = 0; i < 256; i++) {
                p[i] = i;
            }
            for (int i = 0; i < 256; i++) {
                int j = random.nextInt(256 - i) + i;
                int temp = p[i];
                p[i] = p[j];
                p[j] = temp;
                perm[i] = perm[i + 256] = p[i];
            }
        }
        public double noise(double xin, double yin) {
            double n0, n1, n2;

            double s = (xin + yin) * F2;
            int i = fastfloor(xin + s);
            int j = fastfloor(yin + s);
            double t = (i + j) * G2;
            double X0 = i - t;
            double Y0 = j - t;
            double x0 = xin - X0;
            double y0 = yin - Y0;

            int i1, j1;
            if (x0 > y0) {
                i1 = 1;
                j1 = 0;
            } else {
                i1 = 0;
                j1 = 1;
            }

            double x1 = x0 - i1 + G2;
            double y1 = y0 - j1 + G2;
            double x2 = x0 - 1.0 + 2.0 * G2;
            double y2 = y0 - 1.0 + 2.0 * G2;

            int ii = i & 255;
            int jj = j & 255;
            int gi0 = perm[ii + perm[jj]] % 12;
            int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
            int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;

            double t0 = 0.5 - x0 * x0 - y0 * y0;
            if (t0 < 0) {
                n0 = 0.0;
            } else {
                t0 *= t0;
                n0 = t0 * t0 * dot(grad3[gi0], x0, y0);
            }

            double t1 = 0.5 - x1 * x1 - y1 * y1;
            if (t1 < 0) {
                n1 = 0.0;
            } else {
                t1 *= t1;
                n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
            }

            double t2 = 0.5 - x2 * x2 - y2 * y2;
            if (t2 < 0) {
                n2 = 0.0;
            } else {
                t2 *= t2;
                n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
            }

            return 70.0 * (n0 + n1 + n2);
        }

        private int fastfloor(double x) {
            return x > 0 ? (int) x : (int) x - 1;
        }

        private double dot(int[] g, double x, double y) {
            return g[0] * x + g[1] * y;
        }

        private final int[][] grad3 = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
                {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
                {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};
    }
}
