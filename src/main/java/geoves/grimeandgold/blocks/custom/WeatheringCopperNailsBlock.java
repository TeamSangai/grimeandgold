package geoves.grimeandgold.blocks.custom;

import geoves.grimeandgold.datagen.ModDamageTypes;
import geoves.grimeandgold.effects.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import static net.minecraft.world.level.block.WeatheringCopper.WeatherState.OXIDIZED;


public class WeatheringCopperNailsBlock extends Block implements WeatheringCopper {
    private final WeatherState weatherState;

    private static final VoxelShape SHAPE = Block.column(14.0F, 0.0F, 2.0F);


    public WeatheringCopperNailsBlock(WeatherState weatherState, Properties properties) {
        super(properties);
        this.weatherState = weatherState;
    }

    protected @NonNull VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (level instanceof ServerLevel serverLevel) {
            if (entity instanceof LivingEntity livingEntity) {
                int chance = level.getRandom().nextInt(12);
                if (InfectionChance() > chance) {
                    livingEntity.addEffect(new MobEffectInstance(ModEffects.FERAL_WOUND, 2400, 0));
                }
                entity.hurtServer(serverLevel, ModDamageTypes.create(level, ModDamageTypes.SHARP_FLOOR), 1);

            }
        }
        super.entityInside(state, level, pos, entity, effectApplier, isPrecise);
    }

    public int InfectionChance() {
        return switch (weatherState){
            case EXPOSED -> 2;
            case WEATHERED -> 5;
            case OXIDIZED -> 10;
            default -> 0;
        };
    }


    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }
    /**
     * @return
     */
    @Override
    public WeatherState getAge() {
        return this.weatherState;
    }
}
