package net.kyrptonaught.kyrptcarts;

import net.kyrptonaught.kyrptcarts.mixin.BlockEntityAccessor;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;


public class KyrptCartEntity extends AbstractMinecartEntity implements Inventory {

    public BlockEntity blockEntity;

    protected KyrptCartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    protected KyrptCartEntity(World world) {
        this(KyrptCartsMod.cart_entity_type, world);

    }

    public KyrptCartEntity(World world, double x, double y, double z) {
        super(KyrptCartsMod.cart_entity_type, world, x, y, z);
    }

    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (world.isClient) return ActionResult.PASS;

        BlockHitResult hit = new BlockHitResult(this.getPos(), Direction.UP, this.getBlockPos(), false);
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else if (this.hasCustomBlock()) {
            BlockState heldBlock = this.getContainedBlock();
            heldBlock.getBlock().onUse(heldBlock, this.world, new BlockPosWBE(this.getBlockPos(), this), player, hand, hit);
            return ActionResult.SUCCESS;
        } else if (player.getStackInHand(hand).getItem() instanceof BlockItem) {
            BlockState insertedBlock = ((BlockItem) player.getStackInHand(hand).getItem()).getBlock().getPlacementState(new ItemPlacementContext(player, hand, player.getStackInHand(hand), hit));
            this.setCustomBlock(insertedBlock);
            if (insertedBlock.getBlock() instanceof BlockEntityProvider) {
                blockEntity = ((BlockEntityProvider) insertedBlock.getBlock()).createBlockEntity(new BlockPosWBE(this.getBlockPos(), this), insertedBlock);
                blockEntity.setWorld(this.world);
            }
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (blockEntity != null) {
            ((BlockEntityAccessor) blockEntity).setPos(new BlockPosWBE(this.getBlockPos(), this));
            BlockEntityTicker<BlockEntity> ticker = this.getContainedBlock().getBlockEntityTicker(this.world, (BlockEntityType<BlockEntity>) blockEntity.getType());
            if (ticker != null)
                ticker.tick(this.world, new BlockPosWBE(this.getBlockPos(), this), this.getContainedBlock(), blockEntity);
        }
        //kinda derpy
        if (world.isClient) {
            getContainedBlock().getBlock().randomDisplayTick(getContainedBlock(), world, new BlockPosWBE(this.getBlockPos(), this), new Random());
        }
    }

    @Override
    public Type getMinecartType() {
        return KyrptCartsMod.KYRPT_CART_TYPE;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("benbt")) {
            NbtCompound beNBT = nbt.getCompound("benbt");
            blockEntity = BlockEntity.createFromNbt(new BlockPosWBE(this.getBlockPos(), this), this.getContainedBlock(), beNBT);
            blockEntity.setWorld(this.world);
        }
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (blockEntity != null) {
            NbtCompound beNBT = new NbtCompound();
            blockEntity.writeNbt(beNBT);
            nbt.put("benbt", beNBT);
        }
        return nbt;
    }

    @Override
    public int size() {
        if (blockEntity instanceof Inventory)
            return ((Inventory) blockEntity).size();
        return 0;
    }

    @Override
    public boolean isEmpty() {
        if (blockEntity instanceof Inventory)
            return ((Inventory) blockEntity).isEmpty();
        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        if (blockEntity instanceof Inventory)
            return ((Inventory) blockEntity).getStack(slot);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if (blockEntity instanceof Inventory)
            return ((Inventory) blockEntity).removeStack(slot, amount);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if (blockEntity instanceof Inventory)
            return ((Inventory) blockEntity).removeStack(slot);
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (blockEntity instanceof Inventory)
            ((Inventory) blockEntity).setStack(slot, stack);
    }

    @Override
    public void markDirty() {
        if (blockEntity instanceof Inventory)
            ((Inventory) blockEntity).markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (blockEntity instanceof Inventory)
            return ((Inventory) blockEntity).canPlayerUse(player);
        return false;
    }

    @Override
    public void clear() {
        if (blockEntity instanceof Inventory)
            ((Inventory) blockEntity).clear();
    }
}