package net.arathain.familiarsorcery.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class LivingwoodLogBlock extends PillarBlock {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.AXIS;

    public LivingwoodLogBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(3) == 0) {

            BlockPos blockPos = pos.offset(offsetPos(random));
            BlockState blockState = world.getBlockState(blockPos);
            Block block = null;
            if (canGrowIn(blockState)) {
                block = FamiliarSorceryBlocks.LIVINGWOOD_LOG;
            }


            if (block != null) {
                switch(state.get(AXIS)) {
                    case X:
                        BlockState blockState1 = block.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.X);
                        world.setBlockState(blockPos, blockState1);
                    case Z:
                        BlockState blockState2 = block.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Z);
                        world.setBlockState(blockPos, blockState2);
                    case Y:
                        BlockState blockState3 = block.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y);
                        world.setBlockState(blockPos, blockState3);
                }

            }

        }
    }

    public static boolean canGrowIn(BlockState state) {
        return state.isAir() || state.isOf(Blocks.WATER) || state.isOpaque() && state.getFluidState().getLevel() == 8;
    }

    public Direction offsetPos(Random random) {
        switch (this.getDefaultState().get(AXIS)) {
            case X:
                if (random.nextBoolean()) {
                    return Direction.EAST;
                } else {
                    return Direction.WEST;
                }
            case Z:
                if (random.nextBoolean()) {
                    return Direction.NORTH;
                } else {
                    return Direction.SOUTH;
                }
            case Y:
                if (random.nextBoolean()) {
                    return Direction.UP;
                } else {
                    return Direction.DOWN;
                }
        }
        return null;

    }
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch(rotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis)state.get(AXIS)) {
                    case X:
                        return (BlockState)state.with(AXIS, Direction.Axis.Z);
                    case Z:
                        return (BlockState)state.with(AXIS, Direction.Axis.X);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }
}
