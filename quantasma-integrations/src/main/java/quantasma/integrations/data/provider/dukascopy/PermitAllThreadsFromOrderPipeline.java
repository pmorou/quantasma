package quantasma.integrations.data.provider.dukascopy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import quantasma.integrations.data.provider.dukascopy.strategy.OrderPublisher;

import java.util.function.Predicate;

/**
 * Allows any thread coming from Order pipeline
 * ({@link DukascopyOrderService} -> {@link OrderPublisher}) to be executed by Dukascopy library instead of
 * getting an exception: {@code com.dukascopy.api.JFException: Incorrect thread}
 */
public interface PermitAllThreadsFromOrderPipeline {

    /**
     * @param ignored
     * @return {@code true} if signal is going through {@link DukascopyOrderService} and then {@link OrderPublisher}
     */
    default boolean isThreadOk(long ignored) {
        final Boolean isAllowed = isInvokedFromOrderPipeline();
        if (isAllowed) {
            LogHolder.log.info("Order detected - thread accepted");
        }
        return isAllowed;
    }

    private static Boolean isInvokedFromOrderPipeline() {
        return StackWalker.getInstance().walk(
                frames -> frames.dropWhile(is(OrderPublisher.class).negate())
                                .anyMatch(is(DukascopyOrderService.class)));
    }

    private static Predicate<StackWalker.StackFrame> is(Class<?> clazz) {
        return frame -> frame.getClassName().equals(clazz.getTypeName());
    }

    static void initialize() throws CannotCompileException, NotFoundException {
        final ClassPool classPool = ClassPool.getDefault();
        final CtClass strategyTaskManager = classPool.get("com.dukascopy.api.impl.connect.StrategyTaskManager");
        strategyTaskManager.addInterface(classPool.get(PermitAllThreadsFromOrderPipeline.class.getTypeName()));
        final CtMethod isThreadOk = CtNewMethod.make("public boolean isThreadOk(long id) {\n"
                                                     + "  boolean isOrder = " + PermitAllThreadsFromOrderPipeline.class.getTypeName() + ".super.isThreadOk(id);\n"
                                                     + "  if (isOrder) {\n"
                                                     + "    return true;"
                                                     + "  }"
                                                     + "  return super.isThreadOk(id);"
                                                     + "}", strategyTaskManager);
        strategyTaskManager.addMethod(isThreadOk);
        classPool.toClass(strategyTaskManager);
    }

    @Slf4j
    class LogHolder {
    }

}