package quantasma.core;

import lombok.Getter;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.bar.OneSidedBarFactory;

@Getter
public class BaseContext implements Context {
    private final DataService dataService;
    private final OrderService orderService;
    private final StrategyControl strategyControl;

    public BaseContext(DataService dataService, OrderService orderService, StrategyControl strategyControl) {
        this.dataService = dataService;
        this.orderService = orderService;
        this.strategyControl = strategyControl;
    }

    public static class Builder {
        private DataService dataService;
        private OrderService orderService;
        private StrategyControl strategyControl;

        public Builder() {
            orderService = new NullOrderService();
            strategyControl = new InMemoryStrategyControl();
            dataService = new BaseDataService(
                    MarketDataBuilder.basedOn(StructureDefinition.model(new OneSidedBarFactory())
                                                                 .resolution(TimeSeriesDefinition.unlimited(BarPeriod.M1)))
                                     .symbols("EURUSD")
                                     .build());
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

        public Builder withMarketData(MarketData marketData) {
            this.dataService = new BaseDataService(marketData);
            return this;
        }

        public Context build() {
            return new BaseContext(dataService, orderService, strategyControl);
        }
    }
}
