package com.adobe.aem.guides.wknd.core.models;

import com.adobe.aem.guides.wknd.core.models.impl.BylineImpl;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.Text;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

@Model(adaptables = SlingHttpServletRequest.class, adapters = Text.class, resourceType = "wknd/components/referencedata")
public class ReferenceDataModel implements Text {

	@ScriptVariable
	private Page currentPage;

	@Self
	@Via(type = ResourceSuperType.class)
	private Text text;

	@Inject
	private ResourceResolver resourceResolver;

	private String replacedText;

	// Add a logger for any errors
	private static final Logger LOGGER = LoggerFactory.getLogger(BylineImpl.class);

	@PostConstruct
	protected void init() {
		try {
			Resource resource = resourceResolver.getResource("/content/dam/wknd/referentiegegevens.json");
			// Asset asset = resource.adaptTo(Asset.class);
			Asset asset = DamUtil.resolveToAsset(resource);
			Resource original = asset.getOriginal();
			// String title = asset.getName();
			InputStream inputStream = original.adaptTo(InputStream.class);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

			Map<String, Object> map = (Map<String, Object>) new Gson().fromJson(br, Map.class);
			
			String textIn = text.getText();

			replacedText = textIn + " " + getValue(map, "referentiegegevens", "Opbouwgegevens", "A-regeling", "Ouderdomspensioen");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public String getValue(Map<String, Object> map, String... paths) {
		String value = recursiveIncrement(map, paths, 0);
		return value;
	}

	private String recursiveIncrement(Map<String, Object> map, String[] pathToTarget, int pathIndex) {

		final int targetDepth = pathToTarget.length - 1;
		final String key = pathToTarget[pathIndex];
		String value = "";
		if (pathIndex == targetDepth && map.containsKey(key)) {
			// assume objects at the target depth are String
			map.put(key, (String) map.get(key) + 1);
			value = (String) map.get(key) + 1;
		} else if (pathIndex == targetDepth) {
			// at the target depth, put ints
			map.put(key, 1);
		} else if (map.containsKey(key)) {
			// assume object is a map, recur into it for the next key name
			recursiveIncrement((Map<String, Object>) map.get(key), pathToTarget, pathIndex + 1);
		} else {
			// create a new map and recur into it for the next key name
			Map<String, Object> emptyMap = new HashMap<>();
			map.put(key, emptyMap);
			recursiveIncrement(emptyMap, pathToTarget, pathIndex + 1);
		}
		
		return value;
	}

	@Override
	public String getText() {
		return text.getText();
	}

	public String getReplacedText() {
		return replacedText;
	}
}