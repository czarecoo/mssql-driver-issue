package org.czareg;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;
import org.czareg.entity.FilterSortNode;
import org.czareg.entity.Node;
import org.czareg.prod.ProbablyBuggedResultHandler;
import org.czareg.prod.ResultHandlerTranslator;

import java.io.Reader;

public class Problem {

    public static void main(String[] args) throws Exception {
        String resource = "mybatis-config.xml";
        Reader reader = Resources.getResourceAsReader(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            System.out.println("Connected via MyBatis.");

            ProbablyBuggedResultHandler probablyBuggedResultHandler = new ProbablyBuggedResultHandler();
            ResultHandlerTranslator<FilterSortNode> resultHandlerTranslator = new ResultHandlerTranslator(probablyBuggedResultHandler);

            // Start huge query but don’t consume fully
            session.select("org.czareg.utils.FilterSortNodeMapper.selectAll", resultHandlerTranslator);
            System.out.println("Executed huge query (not consuming fully).");
            probablyBuggedResultHandler.done();

            System.out.println("Sleeping for observation...");
            Thread.sleep(600_000); // 10 min for VisualVM/JMX
        }
    }
}
