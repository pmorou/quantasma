package quantasma.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.feature.data.historical.provider.HistoricalDataUpdater;
import quantasma.app.model.FeedBarsSettings;
import quantasma.app.model.FeedHistoricalBarsRequest;
import quantasma.app.model.FeedHistoricalBarsResponse;
import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.HistoricalDataSummaryResponse;
import quantasma.app.service.HistoricalDataService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/historical")
@Slf4j
public class HistoricalController {

    private final HistoricalDataService historicalDataService;
    private final HistoricalDataUpdater historicalDataUpdater;
    private final boolean isHistoricServiceEnabled;

    @Autowired
    public HistoricalController(HistoricalDataService historicalDataService,
                                HistoricalDataUpdater historicalDataUpdater,
                                @Value("${service.historical-data.enabled}") boolean isHistoricServiceEnabled) {
        this.historicalDataService = historicalDataService;
        this.historicalDataUpdater = historicalDataUpdater;
        this.isHistoricServiceEnabled = isHistoricServiceEnabled;
    }

    @GetMapping("data/summary")
    public HistoricalDataSummaryResponse dataSummary() {
        return new HistoricalDataSummaryResponse(historicalDataService.dataSummary()
            .stream()
            .collect(Collectors.groupingBy(HistoricalDataSummary::getSymbol)));
    }

    @PutMapping("data/feed")
    public FeedHistoricalBarsResponse feedHistoricalBars(@RequestBody FeedHistoricalBarsRequest request) {
        if (!isHistoricServiceEnabled) {
            log.info("History service disabled");
            return FeedHistoricalBarsResponse.declined();
        }
        historicalDataUpdater.update(new FeedBarsSettings(request.getSymbol(),
            request.getBarPeriod(),
            request.fromDateAsUtc(),
            request.toDateAsUtc()));
        log.info("Processing by historical service: [{}]", request);
        return FeedHistoricalBarsResponse.accepted();
    }
}
