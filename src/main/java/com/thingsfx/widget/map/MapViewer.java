package com.thingsfx.widget.map;

public interface MapViewer {
	
	enum MapProvider{
		GOOGLE,
		BING,

        /**
         * @deprecated Not fully supported. Only for testing purposes.
         */
		YAHOO
	}
	
	enum MapType{
		ROAD,
		SATELLITE,
		HYBRID,
		TERRAIN
	}
	
	public void setLocation(String location);

	public void setProvider(MapProvider provider);
	public MapProvider getProvider();

	public void setType(MapType type);
	public MapType getType();

	public void reload();

	public void zoomIn();
	public void zoomOut();
    public void setZoom(double zoomLevel);

}
