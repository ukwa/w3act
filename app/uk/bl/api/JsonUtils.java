package uk.bl.api;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import models.Collection;
import models.CollectionArea;
import models.FieldUrl;
import models.Instance;
import models.License;
import models.Organisation;
import models.QaIssue;
import models.Role;
import models.Subject;
import models.Tag;
import models.Target;
import models.Taxonomy;
import models.TaxonomyType;
import models.User;
import play.Logger;
import play.Play;
import play.libs.Json;
import uk.bl.Const;
import uk.bl.api.models.FieldModel;
import uk.bl.exception.ActException;
import uk.bl.exception.TaxonomyNotFoundException;
import uk.bl.exception.UrlInvalidException;
import uk.bl.scope.Scope;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON object management.
 */
public enum JsonUtils {

	/**
	 * This method extracts page number from the JSON in order to evaluate first
	 * and last page numbers.
	 * 
	 * @param node
	 * @param field
	 * @return page number as int
	 */
	
	INSTANCE;

}
