package geoves.grimeandgold.datagen.util;

import com.mojang.datafixers.util.Function6;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.PropertyValueList;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public class C6<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>, T6 extends Comparable<T6>> extends PropertyDispatch<V> {
    private final Property<T1> property1;
    private final Property<T2> property2;
    private final Property<T3> property3;
    private final Property<T4> property4;
    private final Property<T5> property5;
    private final Property<T6> property6;

    public C6(
            final Property<T1> property1, final Property<T2> property2, final Property<T3> property3, final Property<T4> property4, final Property<T5> property5, final Property<T6> property6
    ) {
        this.property1 = property1;
        this.property2 = property2;
        this.property3 = property3;
        this.property4 = property4;
        this.property5 = property5;
        this.property6 = property6;
    }

    @Override
    public List<Property<?>> getDefinedProperties() {
        return List.of(this.property1, this.property2, this.property3, this.property4, this.property5, this.property6);
    }

    public C6<V, T1, T2, T3, T4, T5, T6> select(
            final T1 value1, final T2 value2, final T3 value3, final T4 value4, final T5 value5, final T6 value6, final V variants
    ) {
        PropertyValueList key = PropertyValueList.of(
                this.property1.value(value1), this.property2.value(value2), this.property3.value(value3), this.property4.value(value4), this.property5.value(value5), this.property6.value(value6)
        );
        this.putValue(key, variants);
        return this;
    }

    public PropertyDispatch<V> generate(final Function6<T1, T2, T3, T4, T5, T6, V> generator) {
        this.property1
                .getPossibleValues()
                .forEach(
                        value1 -> this.property2
                                .getPossibleValues()
                                .forEach(
                                        value2 -> this.property3
                                                .getPossibleValues()
                                                .forEach(
                                                        value3 -> this.property4
                                                                .getPossibleValues()
                                                                .forEach(
                                                                        value4 -> this.property5
                                                                                .getPossibleValues()
                                                                                .forEach(
                                                                                        value5 ->
                                                                                                this.property6.getPossibleValues().forEach(
                                                                                                        value6 -> this.select(
                                                                                                                value1, value2, value3, value4, value5, value6, generator.apply(value1, value2, value3, value4, value5, value6)
                                                                                                        )

                                                                                                )
                                                                                )
                                                                )
                                                )
                                )
                );
        return this;
    }

    public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>, T6 extends Comparable<T6>> C6<MultiVariant, T1, T2, T3, T4, T5, T6> initial(
            final Property<T1> property1, final Property<T2> property2, final Property<T3> property3, final Property<T4> property4, final Property<T5> property5, final Property<T6> property6
    ) {
        return new C6<>(property1, property2, property3, property4, property5, property6);
    }
}