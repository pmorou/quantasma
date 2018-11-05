package quantasma.trade.engine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseContext implements Context {
    private final DataService dataService;
    private final OrderService orderService;
    private final StrategyControl strategyControl;

    public BaseContext(DataService dataService, OrderService orderService, StrategyControl strategyControl) {
        this.dataService = dataService;
        this.orderService = orderService;
        this.strategyControl = strategyControl;
    }

    @Override
    public DataService getDataService() {
        return dataService;
    }

    @Override
    public OrderService getOrderService() {
        return orderService;
    }

    @Override
    public StrategyControl getStrategyControl() {
        return strategyControl;
    }

    public static class Builder {
        private DataService dataService;
        private OrderService orderService;
        private StrategyControl strategyControl;
        private Set<GroupTimeSeriesDefinition> groupTimeSeriesDefinitionSet;

        public Builder() {
            orderService = new NullOrderService();
            strategyControl = new BaseStrategyControl();
            dataService = new BaseDataService();
            groupTimeSeriesDefinitionSet = new HashSet<>();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withOrderService(OrderService orderService) {
            this.orderService = orderService;
            return this;
        }

        public Builder withDataService(DataService dataService) {
            this.dataService = dataService;
            return this;
        }

        public Builder withStrategyControl(StrategyControl strategyControl) {
            this.strategyControl = strategyControl;
            return this;
        }

        public Builder withTimeSeries(GroupTimeSeriesDefinition groupTimeSeriesDefinition) {
            this.groupTimeSeriesDefinitionSet.add(groupTimeSeriesDefinition);
            return this;
        }

        public Context build() {
            if (groupTimeSeriesDefinitionSet.size() > 0) {
                final List<MultipleTimeSeries> multipleTimeSeries = BaseMultipleTimeSeries.create((GroupTimeSeriesDefinition[]) groupTimeSeriesDefinitionSet.toArray());
                this.dataService = new BaseDataService(multipleTimeSeries);
            }
            return new BaseContext(dataService, orderService, strategyControl);
        }
    }
}
