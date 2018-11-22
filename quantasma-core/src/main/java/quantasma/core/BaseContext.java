package quantasma.core;

import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.TimeSeriesDefinitionImpl;

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
        private MultipleTimeSeriesBuilder multipleTimeSeriesBuilder;

        public Builder() {
            orderService = new NullOrderService();
            strategyControl = new BaseStrategyControl();
            dataService = new BaseDataService(new MarketData());
            multipleTimeSeriesBuilder = MultipleTimeSeriesBuilder.basedOn(new TimeSeriesDefinitionImpl(BarPeriod.M1, 100_000))
                                                                 .symbols("EURUSD");
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

        public Builder withTimeSeries(MultipleTimeSeriesBuilder timeSeriesDefinition) {
            this.multipleTimeSeriesBuilder = timeSeriesDefinition;
            return this;
        }

        public Context build() {
            this.dataService = new BaseDataService(new MarketData(multipleTimeSeriesBuilder.build()));
            return new BaseContext(dataService, orderService, strategyControl);
        }
    }
}
