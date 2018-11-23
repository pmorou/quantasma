package quantasma.core;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class OrderAmountRefTest {

    @Test
    public void givenNewOrderAmountRefInstanceShouldReturn0() {
        // given
        final OrderAmountRef orderAmountRef = OrderAmountRef.instance();

        // then
        Assertions.assertThat(orderAmountRef.getDelegate()).isEqualTo(0);
    }

    @Test
    public void givenOrderAmountRefWhenSetValueTo2Point2ShouldReturn2Point2() {
        // given
        final OrderAmountRef orderAmountRef = OrderAmountRef.instance();

        // when
        orderAmountRef.setValue(2.2);

        // then
        Assertions.assertThat(orderAmountRef.getDelegate()).isEqualTo(2.2);
    }

    @Test
    public void givenOrderAmountRef2Point2WhenResetValueShouldReturn0() {
        // given
        final OrderAmountRef orderAmountRef = OrderAmountRef.instance();
        orderAmountRef.setValue(2.2);

        // when
        orderAmountRef.resetValue();

        // then
        Assertions.assertThat(orderAmountRef.getDelegate()).isEqualTo(0);
    }
}