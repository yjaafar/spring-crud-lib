This is a Spring boot library, aiming to make the repetitive task, of implementing crud operations for all of your entities, become a cinch.  
Therefore some extendable powerful generic and astract classes are provided for doing all the work, that comes with implementing crud.  
Features:  
generic Crud-Service Layer  
generic Crud-Controller Layer  
generic Integration-Test for the Crud-Controller Layer  
generic Test for Crud-Service Layer  
generic validation Layer  
exception Handling for Crud-Controller-Layer  
support for Bidirectional Entity Relationships (i.e. @OneToMany, @ManyToOne)  
     automatically manages both sides of the bidirectional relationship (i.e. setting of backrefences) for all crud                                     operations  
     generic DTO-to-ServiceEntity-mapping layer, integrated in the generic Crud-Controller-Layer  
  
Checkout the example application for more usageinfos and deatils