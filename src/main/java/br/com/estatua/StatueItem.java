package br.com.estatua;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/** Item que constroi a estatua ao clicar com o botao direito em um bloco. */
public class StatueItem extends Item {

    public StatueItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResultType.FAIL;

        // Chao = bloco clicado (ou o de baixo, se o clicado for mato/neve fina). A estatua fica SEMPRE em cima do chao.
        BlockPos ground = context.getClickedPos();
        if (world.getBlockState(ground).getMaterial().isReplaceable()) ground = ground.below();
        BlockPos origin = ground.above();

        // O jogador olha para "dir"; a estatua fica de frente para ele.
        Direction dir = context.getHorizontalDirection();
        List<StatueBlueprint.Voxel> voxels = StatueBlueprint.voxels(dir.getStepX(), dir.getStepZ());

        String problem = validate(world, player, context.getItemInHand(), ground, origin, voxels);
        if (problem != null) {
            if (!world.isClientSide) {
                player.displayClientMessage(new TranslationTextComponent(problem), true);
            }
            return ActionResultType.FAIL;
        }

        if (!world.isClientSide) {   // constroi apenas no servidor, uma unica vez
            for (StatueBlueprint.Voxel v : voxels) {
                BlockState state = StatuePalette.stateFor(v.symbol);
                world.setBlock(origin.offset(v.dx, v.dy, v.dz), state, 3);
            }
            world.playSound(null, origin, SoundEvents.STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
            if (!player.isCreative()) context.getItemInHand().shrink(1);
        }
        return ActionResultType.SUCCESS;
    }

    /** Devolve a chave de traducao do problema, ou null se for possivel construir. */
    private static String validate(World world, PlayerEntity player, ItemStack stack,
                                   BlockPos ground, BlockPos origin, List<StatueBlueprint.Voxel> voxels) {
        if (origin.getY() < 1 || origin.getY() + StatueBlueprint.height() > world.getMaxBuildHeight()) {
            return "message.estatuapersonalizada.altura";
        }
        if (!player.mayUseItemAt(ground, Direction.UP, stack)) {
            return "message.estatuapersonalizada.sem_permissao";
        }
        for (StatueBlueprint.Voxel v : voxels) {
            BlockPos pos = origin.offset(v.dx, v.dy, v.dz);
            if (!world.mayInteract(player, pos)) return "message.estatuapersonalizada.sem_permissao";
            BlockState existing = world.getBlockState(pos);
            boolean free = existing.isAir()
                    || (existing.getMaterial().isReplaceable() && existing.getFluidState().isEmpty());
            if (!free) return "message.estatuapersonalizada.sem_espaco";
        }
        return null;
    }
}
