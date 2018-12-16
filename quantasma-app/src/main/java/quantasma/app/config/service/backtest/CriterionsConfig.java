package quantasma.app.config.service.backtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ta4j.core.AnalysisCriterion;
import quantasma.core.analysis.criterion.FinishDepositCriterion;
import quantasma.core.analysis.criterion.ProfitLossCriterion;
import quantasma.core.analysis.criterion.ProfitLossPipsCriterion;
import quantasma.core.analysis.criterion.TradesCountCriterion;

@Configuration
public class CriterionsConfig {

    @Bean
    public AnalysisCriterion finishDeposit() {
        return new FinishDepositCriterion(100, 0.0001); // dummy values
    }

    @Bean
    public AnalysisCriterion profitLoss() {
        return new ProfitLossCriterion(0.0001); // dummy value
    }

    @Bean
    public AnalysisCriterion profitLossPips() {
        return new ProfitLossPipsCriterion(0.0001); // dummy value
    }

    @Bean
    public AnalysisCriterion tradesCount() {
        return new TradesCountCriterion();
    }
}
