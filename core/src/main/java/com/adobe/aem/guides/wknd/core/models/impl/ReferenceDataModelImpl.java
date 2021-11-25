package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.ComponentContext;
import com.adobe.aem.guides.wknd.core.models.ReferenceDataModel;
import com.adobe.cq.wcm.core.components.models.Text;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ReferenceDataModel.class},
        resourceType = {ReferenceDataModelImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ReferenceDataModelImpl implements ReferenceDataModel {
    protected static final String RESOURCE_TYPE = "wknd/components/referencedata";

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private ModelFactory modelFactory;

    @ScriptVariable
    private Page currentPage;

    @ScriptVariable
    protected ComponentContext componentContext;

    @ValueMapValue
    private Text text;

    // Add a logger for any errors
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceDataModelImpl.class);

    @PostConstruct
    private void init() {
        text = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Text.class);
    }

    @Override
    public String getText() {
        return "wtf";
    }
}