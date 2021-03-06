package net.arathain.familiarsorcery;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class UnfamiliarUtil {
    public static EntityHitResult hitscanEntity(World world, LivingEntity user, double distance, Predicate<Entity> targetPredicate){
        Vec3d vec3d = user.getCameraPosVec(1);
        Vec3d vec3d2 = user.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 16, vec3d2.y * 16, vec3d2.z * 16);
        double squareDistance = Math.pow(distance, 2);
        return ProjectileUtil.getEntityCollision(world, user, vec3d, vec3d3, user.getBoundingBox().stretch(vec3d2.multiply(squareDistance)).expand(1.0D, 1.0D, 1.0D), targetPredicate);
    }
    public static HitResult hitscanBlock(World world, LivingEntity user, double distance, RaycastContext.FluidHandling fluidHandling, Predicate<Block> targetPredicate){
        Vec3d vec3d = user.getCameraPosVec(1);
        Vec3d vec3d2 = user.getRotationVec(1);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 16, vec3d2.y * 16, vec3d2.z * 16);
        double squareDistance = Math.pow(distance, 2);
        return world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, fluidHandling, user));
    }
}
