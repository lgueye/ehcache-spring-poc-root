/**
 * 
 */
package org.diveintojee.poc.ehcache.spring.domain.services;

import java.util.List;

import javax.jws.WebService;

import org.diveintojee.poc.ehcache.spring.domain.Product;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;

/**
 * @author louis.gueye@gmail.com
 */
@WebService
public interface ProductService {

   String BEAN_ID = "productService";

   String PRODUCT_CACHE_NAME = "product-cache";

   String WEBSERVICE_ENDPOINT_INTERFACE = "org.diveintojee.poc.ehcache.spring.domain.services.ProductService";

   /**
    * @return
    */
   @Cacheable(cacheName = ProductService.PRODUCT_CACHE_NAME)
   List<Product> list();

   /**
    * @param term
    * @return
    */
   List<Product> findByDescription(String term);

   /**
    * @param product
    * @return
    */
   @TriggersRemove(cacheName = ProductService.PRODUCT_CACHE_NAME, removeAll = true, when = When.AFTER_METHOD_INVOCATION)
   Long add(Product product);

   /**
    * @param product
    */
   @TriggersRemove(cacheName = ProductService.PRODUCT_CACHE_NAME, removeAll = true, when = When.AFTER_METHOD_INVOCATION)
   void update(Product product);

   /**
    * @param id
    */
   @TriggersRemove(cacheName = ProductService.PRODUCT_CACHE_NAME, removeAll = true, when = When.AFTER_METHOD_INVOCATION)
   void delete(Long id);

   /**
    * @param product
    * @return
    */
   @Cacheable(cacheName = ProductService.PRODUCT_CACHE_NAME)
   Product get(Long id);

   /**
	 * 
	 */
   @TriggersRemove(cacheName = ProductService.PRODUCT_CACHE_NAME, removeAll = true, when = When.AFTER_METHOD_INVOCATION)
   void clear();

}
