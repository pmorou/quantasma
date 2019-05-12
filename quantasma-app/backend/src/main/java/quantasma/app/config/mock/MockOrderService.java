package quantasma.app.config.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import quantasma.core.NoOpOrderService;
import quantasma.core.OrderService;

/**
 * No-op {@code OrderService} bean
 */
@Component
@Profile("mock")
public class MockOrderService extends NoOpOrderService implements OrderService {
}
