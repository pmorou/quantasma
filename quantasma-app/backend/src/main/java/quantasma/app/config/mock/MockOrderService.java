package quantasma.app.config.mock;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import quantasma.core.NullOrderService;
import quantasma.core.OrderService;

@Component
@Profile("mock")
public class MockOrderService extends NullOrderService implements OrderService {
}
