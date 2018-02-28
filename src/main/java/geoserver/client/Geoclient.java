package geoserver.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;

/*
 * Geoserver manager: https://github.com/geosolutions-it/geoserver-manager/wiki/Various-Examples
 * How to do it through REST in postman: https://gis.stackexchange.com/questions/12970/create-a-layer-in-geoserver-using-rest
 * http://docs.geoserver.org/latest/en/user/gettingstarted/shapefile-quickstart/index.html
 * 1. Upload data (any local file is fine, zip) 
 * 2. Create workspace (can be done manually) 
 * 3. Create data store (optional)
 * 4. Publish layer 
 */
public class Geoclient {
  private final String RESTURL  = "http://localhost:8080/geoserver";
  private final String RESTUSER = "admin";
  private final String RESTPW   = "geoserver";
  
  GeoServerRESTReader reader;
  GeoServerRESTPublisher publisher;
  public Geoclient() {
    try {
      reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
  }
  
  public boolean createWorkspace(String workspace_name)
  {
     return publisher.createWorkspace(workspace_name);
  }
  
/*
 * WMS_url_pattern:
 * http://localhost:8080/geoserver/$workspace_name/wms?service=WMS&version=1.1.0&request=GetMap&layers=$workspace_name:$data_store_name&styles=&bbox=$bbox&width=506&height=768&srs=$proj&format=application/openlayers
 * http://localhost:8080/geoserver/javatest/wms?service=WMS&version=1.1.0&request=GetMap&layers=javatest:nyc_roads&styles=&bbox=984018.1663741902,207673.09513056703,991906.4970533887,219622.53973435296&width=506&height=768&srs=EPSG:2908&format=application/openlayers  
 */
  public boolean publishShapefile(String workspace_name, String data_store_name, String datasetname, 
      String data_path, String proj)
  {
     File zipFile = new File(data_path);
     try {
      return publisher.publishShp(workspace_name, data_store_name, datasetname, zipFile, proj);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return false;
  }
  

  public static void main(String[] args) {
    Geoclient client = new Geoclient();
    //client.createWorkspace("javatest");
    client.publishShapefile("javatest", "myStore", "nyc_roads", "/Users/yjiang/Downloads/nyc_roads.zip", "EPSG:2908");
//    File zipFile = new File("/Users/yjiang/Downloads/nyc_roads.zip");
//    try {
//      boolean published = client.publisher.publishShp("javatest", "myStore", "nyc_roads", zipFile, "EPSG:2908");
//      System.out.println(published);
//    } catch (FileNotFoundException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (IllegalArgumentException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
    

  }

}
