package org.recap.route;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.etl.LoadReportEntity;
import org.recap.model.jpa.BibliographicEntity;
import org.recap.model.jpa.HoldingsEntity;
import org.recap.model.jpa.ItemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by pvsubrah on 6/24/16.
 */
public class JMSTest extends BaseTestCase {

    @Autowired
    private ProducerTemplate producer;

    @Autowired
    ConsumerTemplate consumer;

    @Autowired
    CamelContext camelContext;

    @Value("${etl.report.directory}")
    private String reportDirectoryPath;

    @Test
    public void produceAndConsumeSEDA() throws Exception {
        assertNotNull(producer);
        assertNotNull(consumer);

        producer.sendBody("seda:start", "Hello World");

        String body = consumer.receiveBody("seda:start", String.class);

        assertEquals("Hello World", body);

    }

    @Test
    public void produceAndConsumeEtlLoadQ() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            BibliographicEntity bibliographicEntity1 = new BibliographicEntity();
            bibliographicEntity1.setBibliographicId(random.nextInt());
            bibliographicEntity1.setOwningInstitutionBibId(String.valueOf(random.nextInt()));
            bibliographicEntity1.setOwningInstitutionId(1);
            bibliographicEntity1.setContent("mock content");
            bibliographicEntity1.setCreatedDate(new Date());

            HoldingsEntity holdingsEntity = new HoldingsEntity();
            holdingsEntity.setContent("mock holding content");
            holdingsEntity.setCreatedDate(new Date());
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setBarcode("b.123");
            itemEntity.setCollectionGroupId(1);
            itemEntity.setCreatedDate(new Date());
            itemEntity.setOwningInstitutionId(1);
            itemEntity.setCustomerCode("PA");
            itemEntity.setOwningInstitutionItemId("123");
            holdingsEntity.setItemEntities(Arrays.asList(itemEntity));
            bibliographicEntity1.setHoldingsEntities(Arrays.asList(holdingsEntity));

            BibliographicEntity bibliographicEntity2 = new BibliographicEntity();
            bibliographicEntity2.setBibliographicId(random.nextInt());
            bibliographicEntity2.setOwningInstitutionBibId(String.valueOf(random.nextInt()));
            bibliographicEntity2.setContent("mock conent");
            bibliographicEntity2.setCreatedDate(new Date());
            bibliographicEntity2.setOwningInstitutionId(1);
            bibliographicEntity1.setHoldingsEntities(Arrays.asList(holdingsEntity));

            ETLExchange etlExchange = new ETLExchange();
            etlExchange.setBibliographicEntities(Arrays.asList(bibliographicEntity1, bibliographicEntity2));
            etlExchange.setInstitutionEntityMap(new HashMap());
            etlExchange.setCollectionGroupMap(new HashMap());

            producer.sendBody("activemq:queue:etlLoadQ", Arrays.asList(bibliographicEntity1, bibliographicEntity2));
        }

        Thread.sleep(1000);
    }

    @Test
    public void produceAndConsumeEtlReportQ() throws Exception {
        Random random = new Random();

        LoadReportEntity loadReportEntity = new LoadReportEntity();
        String owningInstitution = String.valueOf(random.nextInt());
        loadReportEntity.setOwningInstitution(owningInstitution);
        loadReportEntity.setOwningInstitutionBibId("1111");
        loadReportEntity.setOwningInstitutionHoldingsId("2222");
        loadReportEntity.setLocalItemId("333333333");
        loadReportEntity.setItemBarcode("4444");
        loadReportEntity.setCustomerCode("PA");
        loadReportEntity.setTitle("title");
        loadReportEntity.setCollectionGroupDesignation("open");
        loadReportEntity.setCreateDateItem(new Date());
        loadReportEntity.setLastUpdatedDateItem(new Date());
        loadReportEntity.setExceptionMessage("exception");
        loadReportEntity.setErrorDescription("error");

        producer.sendBody("activemq:queue:etlReportQ", Arrays.asList(loadReportEntity));

        Thread.sleep(1000);

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String fileName = reportDirectoryPath + File.separator + owningInstitution + "_" + df.format(new Date()) + ".csv";
        File file = new File(fileName);
        assertTrue(file.exists());
    }
}
