package org.czareg;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;

import java.io.Reader;

public class Problem {

    public static void main(String[] args) throws Exception {
        Thread.sleep(5000L);
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            System.out.println("Connected via MyBatis.");

            ProbablyBuggedResultHandler probablyBuggedResultHandler = new ProbablyBuggedResultHandler();
            ResultHandlerTranslator<Columns> resultHandlerTranslator = new ResultHandlerTranslator(probablyBuggedResultHandler);

            // Start huge query but don’t consume fully
            session.select("org.czareg.HugeMapper.hugeQuery", resultHandlerTranslator);
            System.out.println("Executed huge query (not consuming fully).");
            probablyBuggedResultHandler.done();

            // Fire second query before first is "done"
            Integer one = session.selectOne("org.czareg.HugeMapper.smallQuery");
            System.out.println("Executed small query result: " + one);

            System.out.println("Sleeping for observation...");
            Thread.sleep(600_000); // 10 min for VisualVM/JMX
        }
    }
}
