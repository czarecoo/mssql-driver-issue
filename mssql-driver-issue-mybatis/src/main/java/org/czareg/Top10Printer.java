package org.czareg;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.czareg.entity.ChildAssoc;
import org.czareg.entity.Node;
import org.czareg.utils.ChildAssocMapper;
import org.czareg.utils.NodeMapper;

import java.io.InputStream;
import java.util.List;

public class Top10Printer {
    public static void main(String[] args) throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Top10Printer.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            NodeMapper nodeMapper = session.getMapper(NodeMapper.class);
            ChildAssocMapper assocMapper = session.getMapper(ChildAssocMapper.class);

            List<Node> nodes = nodeMapper.selectFirst10();
            System.out.println("=== First 10 Nodes ===");
            nodes.forEach(System.out::println);

            List<ChildAssoc> assocs = assocMapper.selectFirst10();
            System.out.println("=== First 10 ChildAssocs ===");
            assocs.forEach(a ->
                    System.out.printf("Assoc %d: parent=%d, child=%d%n",
                            a.getId(), a.getParent().getId(), a.getChild().getId())
            );
        }
    }
}
