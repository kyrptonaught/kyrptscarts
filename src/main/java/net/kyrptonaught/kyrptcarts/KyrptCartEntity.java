package net.kyrptonaught.kyrptcarts;

import net.kyrptonaught.kyrptcarts.mixin.BlockEntityAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;


public class KyrptCartEntity extends AbstractMinecartEntity implements Inventory {
    private static final TrackedData<NbtCompound> BE_NBT = DataTracker.registerData(KyrptCartEntity.class, TrackedDataHandlerRegistry.TAG_COMPOUND);
    private BlockEntity blockEntity;

    protected KyrptCartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    protected KyrptCartEntity(World world) {
        this(KyrptCartsMod.cart_entity_type, world);

    }

    public KyrptCartEntity(World world, double x, double y, double z) {
        super(KyrptCartsMod.cart_entity_type, world, x, y, z);

    }
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BE_NBT,new NbtCompound());
    }

    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (world.isClient) return ActionResult.PASS;

        BlockHitResult hit = new BlockHitResult(this.getPos(), Direction.UP, this.getBlockPos(), false);
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else if (this.hasCustomBlock()) {
            BlockState heldBlock = this.getContainedBlock();
            heldBlock.getBlock().onUse(heldBlock, this.world, fakeBlockPos(), player, hand, hit);
            return ActionResult.SUCCESS;
        } else if (player.getStackInHand(hand).getItem() instanceof BlockItem) {
            BlockState insertedBlock = ((BlockItem) player.getStackInHand(hand).getItem()).getBlock().getPlacementState(new ItemPlacementContext(player, hand, player.getStackInHand(hand), hit));
            this.setCustomBlock(insertedBlock);
            if (insertedBlock.getBlock() instanceof BlockEntityProvider) {
                addBE(insertedBlock);
            }
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
    private void addBE(BlockState insertedBlock) {
        blockEntity = ((BlockEntityProvider) insertedBlock.getBlock()).createBlockEntity(fakeBlockPos(), insertedBlock);
        blockEntity.setWorld(this.world);
        this.getDataTracker().set(BE_NBT, blockEntity.writeNbt(new NbtCompound()));
    }
    public BlockEntity getBlockEntity() {
        if (blockEntity == null && this.hasCustomBlock() && getContainedBlock() instanceof BlockEntityProvider) {
            blockEntity = BlockEntity.createFromNbt(fakeBlockPos(), this.getContainedBlock(), this.getDataTracker().get(BE_NBT));
            if (blockEntity != null)
                blockEntity.setWorld(this.world);
        }
        //bad everytick hack
        if (blockEntity != null && world.isClient)
            blockEntity.readNbt(this.getDataTracker().get(BE_NBT));
        return blockEntity;
    }
    @Override
    public void tick() {
        super.tick();
        if(this.hasCustomBlock()) {
            if (getBlockEntity() != null) {
                ((BlockEntityAccessor) blockEntity).setPos(fakeBlockPos());
                BlockEntityTicker<BlockEntity> ticker = this.getContainedBlock().getBlockEntityTicker(this.world, (BlockEntityType<BlockEntity>) blockEntity.getType());
                if (ticker != null)
                    ticker.tick(this.world, fakeBlockPos(), this.getContainedBlock(), blockEntity);
                //bad everytick hack
                if (!world.isClient)
                    this.dataTracker.set(BE_NBT, blockEntity.writeNbt(new NbtCompound()));
            }
            //kinda derpy
            if (world.isClient) {
                getContainedBlock().getBlock().randomDisplayTick(getContainedBlock(), world, fakeBlockPos(), new Random());
            }
        }

    }
    public void onActivatorRail(int x, int y, int z, boolean powered) {
        if (powered && this.hasCustomBlock())
            if (getContainedBlock().getProperties().contains(DispenserBlock.FACING))
                getStateWithCorrectedDirection().neighborUpdate(world, fakeBlockPos(), getContainedBlock().getBlock(), fakeBlockPos(), false);
    }

    @Override
    protected void moveOnRail(BlockPos pos, BlockState state) {
        super.moveOnRail(pos, state);
    }

    public BlockState getStateWithCorrectedDirection() {
        BlockState state = getContainedBlock();
        Direction dir = state.get(DispenserBlock.FACING);
        if (dir == Direction.DOWN || dir == Direction.UP) return state;
        float rotation = getHorizontalFacing().asRotation() + dir.asRotation();
        return state.with(DispenserBlock.FACING, Direction.fromRotation(rotation));
    }
    @Override
    public Type getMinecartType() {
        return KyrptCartsMod.KYRPT_CART_TYPE;
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("benbt")) {
            this.dataTracker.set(BE_NBT, nbt.getCompound("benbt"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (getBlockEntity() != null) {
            NbtCompound beNBT = new NbtCompound();
            blockEntity.writeNbt(beNBT);
            nbt.put("benbt", beNBT);
            this.dataTracker.set(BE_NBT, beNBT);
        }
    }

    @Override
    public int size() {
        return getStoredInventory().size();
    }

    @Override
    public boolean isEmpty() {
        return getStoredInventory().isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return getStoredInventory().getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return getStoredInventory().removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return getStoredInventory().removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getStoredInventory().setStack(slot, stack);
    }

    @Override
    public void markDirty() {
        getStoredInventory().markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return getStoredInventory().canPlayerUse(player);
    }

    @Override
    public void clear() {
        getStoredInventory().clear();
    }

    public BlockPos fakeBlockPos() {
        return new BlockPosWBE(getBlockPos(), this);
    }

    public Inventory getStoredInventory() {
        if (getBlockEntity()  instanceof Inventory)
            return (Inventory) blockEntity;
        if (getContainedBlock() instanceof InventoryProvider)
            return ((InventoryProvider) getContainedBlock()).getInventory(getContainedBlock(), world, fakeBlockPos());
        return new SimpleInventory(0);
    }
}