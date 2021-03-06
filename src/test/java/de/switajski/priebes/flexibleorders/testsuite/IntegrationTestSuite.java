package de.switajski.priebes.flexibleorders.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.switajski.priebes.flexibleorders.domain.CategoryIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.CustomerIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.ProductIntegrationTest;
import de.switajski.priebes.flexibleorders.service.SpecificationIntegrationTest;
import de.switajski.priebes.flexibleorders.web.ReportItemControllerIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({
        CategoryIntegrationTest.class,
        CustomerIntegrationTest.class,
        ProductIntegrationTest.class,
        SpecificationIntegrationTest.class,
        ReportItemControllerIntegrationTest.class
})
public class IntegrationTestSuite {

}
