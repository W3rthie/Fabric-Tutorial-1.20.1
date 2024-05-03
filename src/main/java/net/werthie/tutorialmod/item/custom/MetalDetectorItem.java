package net.werthie.tutorialmod.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.werthie.tutorialmod.block.ModBlocks;

public class MetalDetectorItem extends Item {
    Object[] ores = {
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.COAL_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.COPPER_ORE,
            Blocks.DEEPSLATE_IRON_ORE,
            Blocks.IRON_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.NETHER_GOLD_ORE,
            Blocks.GOLD_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.EMERALD_ORE,

            ModBlocks.DEEPSLATE_RUBY_ORE,
            ModBlocks.NETHER_RUBY_ORE,
            ModBlocks.END_STONE_RUBY_ORE,
            ModBlocks.RUBY_ORE
    };

    public MetalDetectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            BlockPos positionClicked = context.getBlockPos();
            PlayerEntity player = context.getPlayer();

            for (int index = 0; index <= positionClicked.getY() + 64; index ++) {
                BlockState state = context.getWorld().getBlockState(positionClicked.down(index));

                if (isValuableBlock(state)) {
                    outputValuableCoordinates(positionClicked.down(index), player, state.getBlock());
                    
                    break;
                }
            }
        }

        context.getStack().damage(1, context.getPlayer(), playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.getActiveHand()));

        return ActionResult.SUCCESS;
    }

    private void outputValuableCoordinates(BlockPos blockPos, PlayerEntity player, Block block) {
        String pos = "(" + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + ")";
        String message = "Found " + block.asItem().getName().getString() + " at " + pos;

        player.sendMessage(Text.literal(message), false);
    }

    private boolean isValuableBlock(BlockState state) {
        for (Object ore : ores) {
            if (state.isOf((Block) ore)) {
                return true;
            }
        }

        return false;
    }
}
