// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package de.switajski.priebes.flexibleorders.domain;

import de.switajski.priebes.flexibleorders.domain.ArchiveItemDataOnDemand;
import de.switajski.priebes.flexibleorders.domain.ArchiveItemIntegrationTest;
import de.switajski.priebes.flexibleorders.domain.ArchiveItemService;
import de.switajski.priebes.flexibleorders.repository.ArchiveItemRepository;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ArchiveItemIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ArchiveItemIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ArchiveItemIntegrationTest: @ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ArchiveItemIntegrationTest: @Transactional;
    
    @Autowired
    ArchiveItemDataOnDemand ArchiveItemIntegrationTest.dod;
    
    @Autowired
    ArchiveItemService ArchiveItemIntegrationTest.archiveItemService;
    
    @Autowired
    ArchiveItemRepository ArchiveItemIntegrationTest.archiveItemRepository;
    
    @Test
    public void ArchiveItemIntegrationTest.testCountAllArchiveItems() {
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", dod.getRandomArchiveItem());
        long count = archiveItemService.countAllArchiveItems();
        Assert.assertTrue("Counter for 'ArchiveItem' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ArchiveItemIntegrationTest.testFindArchiveItem() {
        ArchiveItem obj = dod.getRandomArchiveItem();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to provide an identifier", id);
        obj = archiveItemService.findArchiveItem(id);
        Assert.assertNotNull("Find method for 'ArchiveItem' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ArchiveItem' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ArchiveItemIntegrationTest.testFindAllArchiveItems() {
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", dod.getRandomArchiveItem());
        long count = archiveItemService.countAllArchiveItems();
        Assert.assertTrue("Too expensive to perform a find all test for 'ArchiveItem', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ArchiveItem> result = archiveItemService.findAllArchiveItems();
        Assert.assertNotNull("Find all method for 'ArchiveItem' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ArchiveItem' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ArchiveItemIntegrationTest.testFindArchiveItemEntries() {
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", dod.getRandomArchiveItem());
        long count = archiveItemService.countAllArchiveItems();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ArchiveItem> result = archiveItemService.findArchiveItemEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ArchiveItem' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ArchiveItem' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ArchiveItemIntegrationTest.testFlush() {
        ArchiveItem obj = dod.getRandomArchiveItem();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to provide an identifier", id);
        obj = archiveItemService.findArchiveItem(id);
        Assert.assertNotNull("Find method for 'ArchiveItem' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyArchiveItem(obj);
        Integer currentVersion = obj.getVersion();
        archiveItemRepository.flush();
        Assert.assertTrue("Version for 'ArchiveItem' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ArchiveItemIntegrationTest.testUpdateArchiveItemUpdate() {
        ArchiveItem obj = dod.getRandomArchiveItem();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to provide an identifier", id);
        obj = archiveItemService.findArchiveItem(id);
        boolean modified =  dod.modifyArchiveItem(obj);
        Integer currentVersion = obj.getVersion();
        ArchiveItem merged = (ArchiveItem)archiveItemService.updateArchiveItem(obj);
        archiveItemRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ArchiveItem' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ArchiveItemIntegrationTest.testSaveArchiveItem() {
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", dod.getRandomArchiveItem());
        ArchiveItem obj = dod.getNewTransientArchiveItem(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ArchiveItem' identifier to be null", obj.getId());
        try {
            archiveItemService.saveArchiveItem(obj);
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        archiveItemRepository.flush();
        Assert.assertNotNull("Expected 'ArchiveItem' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ArchiveItemIntegrationTest.testDeleteArchiveItem() {
        ArchiveItem obj = dod.getRandomArchiveItem();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ArchiveItem' failed to provide an identifier", id);
        obj = archiveItemService.findArchiveItem(id);
        archiveItemService.deleteArchiveItem(obj);
        archiveItemRepository.flush();
        Assert.assertNull("Failed to remove 'ArchiveItem' with identifier '" + id + "'", archiveItemService.findArchiveItem(id));
    }
    
}