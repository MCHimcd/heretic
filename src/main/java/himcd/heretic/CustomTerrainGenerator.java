package himcd.heretic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class CustomTerrainGenerator {

    private static final int OCTAVES = 8;
    private static final double AMPLITUDE = 512.0;
    private static final double ROUGHNESS = 0.4;
    private static final int MIN_DEPTH = 1;
    private static final int MAX_DEPTH = 40;
    private static final double ORE_CHANCE = 0.2;
    private static final double TREE_CHANCE = 0.01;
    private static final int MIN_TREE_HEIGHT = 4;
    private static final int MAX_TREE_HEIGHT = 8;

    public static void generateTerrain(World world, int startX, int startZ) {
        Random random = new Random();

        // 根据柏林噪声算法生成地形和放置矿物
        for (int x = 0; x < 256; x++) {
            for (int z = 0; z < 256; z++) {
                double heightValue = getHeight(startX + x, startZ + z, world);
                int height = (int) (heightValue);
                for (int y = 0; y < height; y++) {
                    world.getBlockAt(startX + x, y, startZ + z).setType(Material.STONE);
                }
                world.getBlockAt(startX + x, height, startZ + z).setType(Material.GRASS_BLOCK);

                // 放置矿物
                if (random.nextDouble() < ORE_CHANCE && height >= MIN_DEPTH && height <= MAX_DEPTH) {
                    placeOres(world, startX + x, startZ + z, height, random);
                }

                // 放置树木
                if (random.nextDouble() < TREE_CHANCE && height < world.getMaxHeight() - 1) {
                    placeTree(world, startX + x, height + 1, startZ + z, random);
                }
            }
        }
    }

    private static void placeOres(World world, int x, int z, int height, Random random) {
        for (int y = MIN_DEPTH; y <= MAX_DEPTH; y++) {
            if (random.nextDouble() < ORE_CHANCE) {
                Material oreMaterial;
                double oreType = random.nextDouble();
                if (oreType < 0.1) {
                    oreMaterial = Material.DIAMOND_ORE;
                } else if (oreType < 0.3) {
                    oreMaterial = Material.COAL_ORE;
                } else {
                    oreMaterial = Material.IRON_ORE;
                }
                world.getBlockAt(x, y, z).setType(oreMaterial);
            }
        }
    }

    private static void placeTree(World world, int x, int y, int z, Random random) {
        TreeType[] treeTypes = {TreeType.BIRCH, TreeType.REDWOOD, TreeType.TALL_BIRCH, TreeType.JUNGLE, TreeType.ACACIA, TreeType.DARK_OAK, TreeType.SMALL_JUNGLE};
        TreeType treeType = treeTypes[random.nextInt(treeTypes.length)];
        int height = MIN_TREE_HEIGHT + random.nextInt(MAX_TREE_HEIGHT - MIN_TREE_HEIGHT + 1);
        world.generateTree(world.getBlockAt(x, y, z).getLocation(), treeType);
    }

    private static double getHeight(int x, int z, World world) {
        double total = 0;
        double d = 1 << OCTAVES;
        for (int i = 0; i < OCTAVES; i++) {
            double freq = (1 << i) / d;
            double amp = Math.pow(ROUGHNESS, i) * AMPLITUDE;
            total += interpolateNoise(x * freq, z * freq, world) * amp;
        }
        return total;
    }

    private static double interpolateNoise(double x, double z, World world) {
        int intX = (int) x;
        int intZ = (int) z;
        double fracX = x - intX;
        double fracZ = z - intZ;

        double v1 = smoothNoise(intX, intZ, world);
        double v2 = smoothNoise(intX + 1, intZ, world);
        double v3 = smoothNoise(intX, intZ + 1, world);
        double v4 = smoothNoise(intX + 1, intZ + 1, world);

        double i1 = interpolate(v1, v2, fracX);
        double i2 = interpolate(v3, v4, fracX);

        return interpolate(i1, i2, fracZ);
    }

    private static double interpolate(double a, double b, double x) {
        double ft = x * Math.PI;
        double f = (1 - Math.cos(ft)) * 0.5;
        return a * (1 - f) + b * f;
    }

    private static double smoothNoise(int x, int z, World world) {
        double corners = (noise(x - 1, z - 1, world) + noise(x + 1, z - 1, world) + noise(x - 1, z + 1, world) + noise(x + 1, z + 1, world)) / 16.0;
        double sides = (noise(x - 1, z, world) + noise(x + 1, z, world) + noise(x, z - 1, world) + noise(x, z + 1, world)) / 8.0;
        double center = noise(x, z, world) / 4.0;
        return corners + sides + center;
    }

    private static double noise(int x, int z, World world) {
        return (x * 5432 + z * 9871 != 0) ? world.getSeed() % (x * 5432 + z * 9871) / 65535.0 : 0.0;
    }
}