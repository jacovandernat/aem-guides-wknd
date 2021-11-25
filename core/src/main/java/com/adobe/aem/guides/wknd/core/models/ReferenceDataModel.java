package com.adobe.aem.guides.wknd.core.models;


import com.adobe.cq.wcm.core.components.models.Text;
import com.day.cq.wcm.api.Page;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Text.class, resourceType = "wknd/components/referencedata")
public class ReferenceDataModel implements Text {
 
    @ScriptVariable
    private Page currentPage;
 
    @Self 
    @Via(type = ResourceSuperType.class)
    private Text text;
    
    private String replacedText;  
    
    @PostConstruct
    protected void init() {

    	replacedText = text.getText() + "Hello World!";
    }
 
    @Override
    public String getText() {
        return text.getText();
    }
    
    public String getReplacedText() {
        return replacedText;
    }
}