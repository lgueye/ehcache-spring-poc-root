/**
 * 
 */
package org.diveintojee.poc.ehcache.spring.domain;

/**
 * @author louis.gueye@gmail.com
 */
public class Product extends AbstractPersistableEntity {

   /**
    * 
    */
   private static final long serialVersionUID = 6117126134583209714L;

   private String name;

   private String description;

   /**
    * @return
    */
   public String getName() {
      return name;
   }

   /**
    * @param name
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * @return
    */
   public String getDescription() {
      return description;
   }

   /**
    * @param description
    */
   public void setDescription(String description) {
      this.description = description;
   }

}
