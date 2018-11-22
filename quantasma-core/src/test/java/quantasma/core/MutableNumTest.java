package quantasma.core;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MutableNumTest {

    @Test
    public void givenMutableNumShouldReturnItsValue() {
        // given
        final MutableNum mutableNum = MutableNum.valueOf(1.1);

        // then
        Assertions.assertThat(mutableNum.getDelegate()).isEqualTo(1.1);
    }

    @Test
    public void givenMutableNumWhenMutateShouldReturnNewValue() {
        // given
        final MutableNum mutableNum = MutableNum.valueOf(1.1);

        // when
        mutableNum.mutate(2.2);

        // then
        Assertions.assertThat(mutableNum.getDelegate()).isEqualTo(2.2);
    }
}