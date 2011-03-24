/**
 * 
 */
package org.diveintojee.poc.ehcache.spring.domain;

/**
 * @author louis.gueye@gmail.com
 */
public interface PersistableEntity {

   /**
    * @return
    */
   Long getId();

   /**
    * @param id
    */
   void setId(Long id);
}
