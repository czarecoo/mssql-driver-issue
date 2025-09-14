package org.czareg.prod;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class ResultHandlerTranslator<R> implements ResultHandler {
    private final SimpleResultHandler<R> target;
    boolean stopped = false;

    public ResultHandlerTranslator(SimpleResultHandler<R> target) {
        this.target = target;
    }

    @Override
    public void handleResult(ResultContext ctx) {
        if (stopped || ctx.isStopped()) {
            return;
        }
        boolean more = target.handleResult((R) ctx.getResultObject());
        if (!more) {
            ctx.stop();
            stopped = true;
        }
    }
}
