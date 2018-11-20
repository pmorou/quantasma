package quantasma.core;

import org.ta4j.core.Order;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;

public class BaseBidAskBar implements BidAskBar {

    private static final long serialVersionUID = 8038383777467488148L;

    private Duration timePeriod;
    private ZonedDateTime endTime;
    private ZonedDateTime beginTime;
    private Num bidOpenPrice = NaN.NaN;
    private Num bidMaxPrice = NaN.NaN;
    private Num bidMinPrice = NaN.NaN;
    private Num bidClosePrice = NaN.NaN;
    private Num askOpenPrice = NaN.NaN;
    private Num askMaxPrice = NaN.NaN;
    private Num askMinPrice = NaN.NaN;
    private Num askClosePrice = NaN.NaN;
    private Num amount = NaN.NaN;
    private Num volume = NaN.NaN;
    private int trades;

    public BaseBidAskBar(Duration timePeriod, ZonedDateTime endTime, Function<Number, Num> numFunction) {
        checkTimeArguments(timePeriod, endTime);
        this.timePeriod = timePeriod;
        this.endTime = endTime;
        this.beginTime = endTime.minus(timePeriod);
        this.volume = numFunction.apply(0);
        this.amount = numFunction.apply(0);
    }

    public BaseBidAskBar(ZonedDateTime endTime, double openPrice, double highPrice, double lowPrice, double closePrice, double volume, Function<Number, Num> numFunction) {
        this(endTime,
             numFunction.apply(openPrice),
             numFunction.apply(highPrice),
             numFunction.apply(lowPrice),
             numFunction.apply(closePrice),
             numFunction.apply(volume), numFunction.apply(0));
    }

    public BaseBidAskBar(ZonedDateTime endTime, String openPrice, String highPrice, String lowPrice, String closePrice, String volume, Function<Number, Num> numFunction) {
        this(endTime,
             numFunction.apply(new BigDecimal(openPrice)),
             numFunction.apply(new BigDecimal(highPrice)),
             numFunction.apply(new BigDecimal(lowPrice)),
             numFunction.apply(new BigDecimal(closePrice)),
             numFunction.apply(new BigDecimal(volume)),
             numFunction.apply(0));
    }

    public BaseBidAskBar(Duration timePeriod, ZonedDateTime endTime, String bidOpenPrice, String bidHighPrice, String bidLowPrice, String bidClosePrice,
                         String askOpenPrice, String askHighPrice, String askLowPrice, String askClosePrice, String volume, Function<Number, Num> numFunction) {
        this(timePeriod,
             endTime,
             numFunction.apply(new BigDecimal(bidOpenPrice)),
             numFunction.apply(new BigDecimal(bidHighPrice)),
             numFunction.apply(new BigDecimal(bidLowPrice)),
             numFunction.apply(new BigDecimal(bidClosePrice)),
             numFunction.apply(new BigDecimal(askOpenPrice)),
             numFunction.apply(new BigDecimal(askHighPrice)),
             numFunction.apply(new BigDecimal(askLowPrice)),
             numFunction.apply(new BigDecimal(askClosePrice)),
             numFunction.apply(new BigDecimal(volume)),
             numFunction.apply(0));
    }

    public BaseBidAskBar(Duration timePeriod, ZonedDateTime endTime, double bidOpenPrice, double bidHighPrice, double bidLowPrice, double bidClosePrice,
                         double askOpenPrice, double askHighPrice, double askLowPrice, double askClosePrice, double volume, Function<Number, Num> numFunction) {
        this(timePeriod,
             endTime,
             numFunction.apply(bidOpenPrice),
             numFunction.apply(bidHighPrice),
             numFunction.apply(bidLowPrice),
             numFunction.apply(bidClosePrice),
             numFunction.apply(askOpenPrice),
             numFunction.apply(askHighPrice),
             numFunction.apply(askLowPrice),
             numFunction.apply(askClosePrice),
             numFunction.apply(volume),
             numFunction.apply(0));
    }

    public BaseBidAskBar(ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume, Num amount) {
        this(Duration.ofDays(1), endTime, openPrice, highPrice, lowPrice, closePrice, volume, amount);
    }

    public BaseBidAskBar(Duration timePeriod, ZonedDateTime endTime, Num openPrice, Num highPrice, Num lowPrice, Num closePrice, Num volume, Num amount) {
        checkTimeArguments(timePeriod, endTime);
        this.timePeriod = timePeriod;
        this.endTime = endTime;
        this.beginTime = endTime.minus(timePeriod);
        this.bidOpenPrice = openPrice;
        this.bidMaxPrice = highPrice;
        this.bidMinPrice = lowPrice;
        this.bidClosePrice = closePrice;
        this.volume = volume;
        this.amount = amount;
    }

    public BaseBidAskBar(Duration timePeriod, ZonedDateTime endTime, Num bidOpenPrice, Num bidHighPrice, Num bidLowPrice, Num bidClosePrice,
                         Num askOpenPrice, Num askHighPrice, Num askLowPrice, Num askClosePrice, Num volume, Num amount) {
        checkTimeArguments(timePeriod, endTime);
        this.timePeriod = timePeriod;
        this.endTime = endTime;
        this.beginTime = endTime.minus(timePeriod);
        this.bidOpenPrice = bidOpenPrice;
        this.bidMaxPrice = bidHighPrice;
        this.bidMinPrice = bidLowPrice;
        this.bidClosePrice = bidClosePrice;
        this.askOpenPrice = askOpenPrice;
        this.askMaxPrice = askHighPrice;
        this.askMinPrice = askLowPrice;
        this.askClosePrice = askClosePrice;
        this.volume = volume;
        this.amount = amount;
    }

    @Override
    public Num getBidOpenPrice() {
        return bidOpenPrice;
    }

    @Override
    public Num getBidMaxPrice() {
        return bidMaxPrice;
    }

    @Override
    public Num getBidMinPrice() {
        return bidMinPrice;
    }

    @Override
    public Num getBidClosePrice() {
        return bidClosePrice;
    }

    @Override
    public Num getAskOpenPrice() {
        return askOpenPrice;
    }

    @Override
    public Num getAskMaxPrice() {
        return askMaxPrice;
    }

    @Override
    public Num getAskMinPrice() {
        return askMinPrice;
    }

    @Override
    public Num getAskClosePrice() {
        return askClosePrice;
    }

    @Override
    public Num getVolume() {
        return volume;
    }

    @Override
    public int getTrades() {
        return trades;
    }

    @Override
    public Num getAmount() {
        return amount;
    }

    @Override
    public Duration getTimePeriod() {
        return timePeriod;
    }

    @Override
    public ZonedDateTime getBeginTime() {
        return beginTime;
    }

    @Override
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void addTrade(Num tradeVolume, Num tradePrice) {
        addPrice(tradePrice);

        volume = volume.plus(tradeVolume);
        amount = amount.plus(tradeVolume.multipliedBy(tradePrice));
        trades++;
    }

    @Override
    public void addTrade(Num volume, Num bid, Num ask, Order.OrderType orderType) {
        addPrice(bid, ask);

        volume = volume.plus(volume);
        if (orderType == Order.OrderType.BUY) {
            amount = amount.plus(volume.multipliedBy(bid));
        } else {
            amount = amount.plus(volume.multipliedBy(ask));

        }
        trades++;
    }

    @Override
    public void addPrice(Num price) {
        if (isNaN(bidOpenPrice)) {
            bidOpenPrice = price;
        }

        bidClosePrice = price;
        if (isNaN(bidMaxPrice)) {
            bidMaxPrice = price;
        } else if (bidMaxPrice.isLessThan(price)) {
            bidMaxPrice = price;
        }
        if (isNaN(bidMinPrice)) {
            bidMinPrice = price;
        } else if (bidMinPrice.isGreaterThan(price)) {
            bidMinPrice = price;
        }
    }

    private boolean isNaN(Num bidOpenPrice) {
        return bidOpenPrice == NaN.NaN;
    }

    @Override
    public void addPrice(Num bid, Num ask) {
        addPrice(bid);

        if (isNaN(askOpenPrice)) {
            askOpenPrice = ask;
        }

        askClosePrice = ask;
        if (isNaN(askMaxPrice)) {
            askMaxPrice = ask;
        } else if (askMaxPrice.isLessThan(ask)) {
            askMaxPrice = ask;
        }
        if (isNaN(askMinPrice)) {
            askMinPrice = ask;
        } else if (askMinPrice.isGreaterThan(ask)) {
            askMinPrice = ask;
        }
    }

    @Override
    public String toString() {
        return String.format("{end time: %1s, bid close price: %2$f, bid open price: %3$f, bid min price: %4$f, bid max price: %5$f, "
                             + "ask close price: %6$f, ask open price: %7$f, ask min price: %8$f, ask max price: %9$f, volume: %10$f}",
                             endTime.withZoneSameInstant(ZoneId.systemDefault()),
                             bidClosePrice.doubleValue(),
                             bidOpenPrice.doubleValue(),
                             bidMinPrice.doubleValue(),
                             bidMaxPrice.doubleValue(),
                             askClosePrice.doubleValue(),
                             askOpenPrice.doubleValue(),
                             askMinPrice.doubleValue(),
                             askMaxPrice.doubleValue(),
                             volume.doubleValue());
    }

    private static void checkTimeArguments(Duration timePeriod, ZonedDateTime endTime) {
        if (timePeriod == null) {
            throw new IllegalArgumentException("Time period cannot be null");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("End time cannot be null");
        }
    }
}
