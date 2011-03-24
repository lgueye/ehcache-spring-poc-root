package org.diveintojee.poc.ehcache.spring.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.diveintojee.poc.ehcache.spring.domain.Product;
import org.diveintojee.poc.ehcache.spring.domain.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {Constants.TESTS_CONTEXT})
public class ProductServiceTest {

   private static final int PRODUCTS_INITIAL_SIZE = 5;
   @Autowired
   private ProductService productService;

   @Autowired
   private CacheManager cacheManager;

   /**
    * 
    */
   @Before
   public void before() {

      assertNotNull(productService);

      assertNotNull(cacheManager);

      setUpNewProductRepository();

      cacheManager.getCache(ProductService.PRODUCT_CACHE_NAME).flush();

   }

   /**
    * 
    */
   @Test
   public void testList() {

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 0);

      List<Product> products = productService.list();

      assertNotNull(products);

      assertEquals(PRODUCTS_INITIAL_SIZE, products.size());

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 5);

   }

   /**
    * 
    */
   @Test
   public void testFindByDescription() {

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 0);

      Product p = new Product();

      String name = "doliprane";

      String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
            + "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

      p.setName(name);

      p.setDescription(description);

      productService.add(p);

      assertEquals(PRODUCTS_INITIAL_SIZE + 1, productService.list().size());

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, PRODUCTS_INITIAL_SIZE + 1);

      String term = "description";

      assertEquals(PRODUCTS_INITIAL_SIZE, productService.findByDescription(term).size());

   }

   /**
    * 
    */
   @Test
   public void testAdd() {

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 0);

      Product product = new Product();

      String name = "doliprane";

      String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
            + "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

      product.setName(name);

      product.setDescription(description);

      Long productId = productService.add(product);

      assertNotNull(productService.get(productId));

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 1);

      assertCacheContains(ProductService.PRODUCT_CACHE_NAME, product);
   }

   /**
    * 
    */
   @Test
   public void testUpdate() {

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 0);

      Product product = new Product();

      String name = "doliprane";

      String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
            + "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

      product.setName(name);

      product.setDescription(description);

      Long productId = productService.add(product);

      product = productService.get(productId);

      assertNotNull(product);

      String newName = "prozac";

      product.setName(newName);

      String newDescription = "Ce médicament est un antidépresseur de la famille des inhibiteurs de la recapture de la sérotonine.";

      newDescription += "Il est utilisé chez l'adulte dans le traitement : -des états dépressifs ; \n-des troubles obsessionnels compulsifs ;";

      newDescription += "\n- de la boulimie (en complément d'une psychothérapie). ;";

      product.setDescription(newDescription);

      productService.update(product);

      product = productService.get(productId);

      assertEquals(newDescription, product.getDescription());

      assertEquals(newName, product.getName());

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 1);

      assertCacheContains(ProductService.PRODUCT_CACHE_NAME, product);

   }

   /**
    * 
    */
   @Test
   public void testGet() {

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 0);

      Product product = new Product();

      String name = "doliprane";

      String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
            + "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

      product.setName(name);

      product.setDescription(description);

      Long productId = productService.add(product);

      product = productService.get(productId);

      assertNotNull(product);

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 1);

      assertCacheContains(ProductService.PRODUCT_CACHE_NAME, product);

   }

   /**
    * 
    */
   @Test
   public void testDelete() {

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 0);

      Product product = new Product();

      String name = "doliprane";

      String description = "Ce médicament est un antalgique et un antipyrétique qui contient du paracétamol."
            + "\nIl est utilisé pour faire baisser la fièvre et dans le traitement des affections douloureuses.";

      product.setName(name);

      product.setDescription(description);

      Long productId = productService.add(product);

      assertNotNull(productService.get(productId));

      productService.delete(productId);

      assertNull(productService.get(productId));

      assertCacheSize(ProductService.PRODUCT_CACHE_NAME, 0);

   }

   /**
    * 
    */
   private void setUpNewProductRepository() {

      productService.clear();

      for (int i = 0; i < PRODUCTS_INITIAL_SIZE; i++) {

         Product p = new Product();

         String name = "name" + i;

         String description = "description" + i;

         p.setName(name);

         p.setDescription(description);

         productService.add(p);

      }

   }

   @SuppressWarnings("unchecked")
   private <T> void assertCacheSize(String cacheName, int expectedSize) {

      if (StringUtils.isEmpty(cacheName))
         throw new IllegalArgumentException("Cache name is required");

      Cache cache = cacheManager.getCache(cacheName);

      assertNotNull(cache);

      assertNotNull(cache.getKeys());

      if (cache.getKeys().size() == 0) {

         assertEquals(0, expectedSize);

         return;

      }

      assertNotNull(cache.getKeys().get(0));

      Element element = cache.get(cache.getKeys().get(0));

      assertNotNull(element);

      if (element.getValue() == null) {

         assertEquals(0, expectedSize);

         return;

      }

      assertNotNull(element.getValue());

      if (expectedSize == 1) {

         assertTrue(!(element.getValue() instanceof Collection));

         return;

      }

      List<T> fromCache = (List<T>) element.getValue();

      assertEquals(expectedSize, fromCache.size());

   }

   @SuppressWarnings("unchecked")
   private <T> void assertCacheContains(String cacheName, T obj) {

      if (StringUtils.isEmpty(cacheName))
         throw new IllegalArgumentException("Cache name is required");

      if (obj == null)
         throw new IllegalArgumentException("Not null object is required");

      Cache cache = cacheManager.getCache(cacheName);

      assertNotNull(cache);

      assertNotNull(cache.getKeys());

      assertNotNull(cache.getKeys().get(0));

      Element element = cache.get(cache.getKeys().get(0));

      assertNotNull(element);

      assertNotNull(element.getValue());

      if (element.getValue() instanceof List) {

         List<T> fromCache = (List<T>) element.getValue();

         assertNotNull(fromCache);

         assertTrue(fromCache.size() > 0);

         assertTrue(fromCache.contains(obj));

      } else {

         assertEquals(obj, element.getValue());

      }

   }
}
